package bsep.tim4.hospitalApp.util;

import bsep.tim4.hospitalApp.dto.LogConfig;
import bsep.tim4.hospitalApp.model.*;
import bsep.tim4.hospitalApp.repository.LogAlarmRepository;
import bsep.tim4.hospitalApp.repository.LogRepository;
import bsep.tim4.hospitalApp.repository.MaliciousIpRepository;
import bsep.tim4.hospitalApp.service.KieSessionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.*;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogReader implements Runnable {

    private final LogRepository logRepository;

    private LogConfig logConfig;

    private KieSessionService kieSessionService;

    private LogAlarmRepository logAlarmRepository;

    private MaliciousIpRepository maliciousIpRepository;

    private SimpMessagingTemplate simpMessagingTemplate;

    public LogReader(LogRepository logRepository, LogConfig logConfig, KieSessionService kieSessionService,
                     LogAlarmRepository logAlarmRepository, MaliciousIpRepository maliciousIpRepository,
                     SimpMessagingTemplate simpMessagingTemplate) {
        this.logRepository = logRepository;
        this.logConfig = logConfig;
        this.kieSessionService = kieSessionService;
        this.logAlarmRepository = logAlarmRepository;
        this.maliciousIpRepository = maliciousIpRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public LogConfig getLogConfig() {
        return logConfig;
    }

    @Override
    public void run() {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(logConfig.getPath());

            path.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_MODIFY);

            WatchKey key;
            while ((key = watchService.take()) != null) {
                // citamo log fajlove svakih readInterval sekundi
                Thread.sleep(logConfig.getReadInterval() * 1000);

                for (WatchEvent<?> event : key.pollEvents()) {
                    String changedFileName = event.context().toString();
                    String changedFile = logConfig.getPath() + File.separator + event.context();

                    Long startRow = logConfig.getLogFilePointer(changedFileName);
                    if (!changedFile.contains("~")) {
                        handleLogFile(changedFile, changedFileName, startRow);
                        System.out.println(
                                "Event kind:" + event.kind()
                                        + ". File affected: " + event.context() + ".");
                    }
                }
                key.reset();
            }
        } catch (IOException | InterruptedException e) {
        }
    }

    private void handleLogFile(String filePath, String fileName, Long startPos) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(filePath, "r");
        raf.seek(startPos);

        List<Log> logs = new ArrayList<Log>();

        String line;
        while ((line = raf.readLine()) != null) {
            if (!line.equals("")) {
                Log log = parseLog(line);
                if (log != null) {
                    logs.add(log);
                }
            }
        }
        Long endPos = raf.getFilePointer();
        logConfig.setLogFilePointer(fileName, endPos);
        raf.close();

        logRepository.saveAll(logs);
        KieSession kieSession = kieSessionService.getCepSession();

        for (Log log: logs) {
            kieSession.insert(log);
            kieSession.fireAllRules();
        }

        QueryResults results = kieSession.getQueryResults("getAllLogAlarms");
        List<LogAlarm> alarms = new ArrayList<>();

        for (QueryResultsRow row : results) {
            LogAlarm alarm = (LogAlarm) row.get("$logAlarm");
            alarms.add(alarm);
        }

        saveAndSendNewAlarms(alarms);
    }

    private Log parseLog(String logLine) throws JsonProcessingException {
        // provera da li odgovara filteru
        if (logConfig.getFilter() != null) {
            Pattern pattern = Pattern.compile(logConfig.getFilter());
            Matcher matcher = pattern.matcher(logLine);
            if (!matcher.find()) {
                return null;
            }
        }
        ObjectMapper om = new ObjectMapper();
        JsonNode logTree = om.readTree(logLine);
        String timestampString;
        JsonNode timestampNode = logTree.get("@timestamp");
        if (timestampNode == null) {
            // keycloak
            timestampString = logTree.get("timestamp").asText();
        } else {
            // spring
            timestampString = timestampNode.asText();
        }
        // fromat ISO 8601
        DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(timestampString, timeFormatter);
        Date timestamp = Date.from(Instant.from(offsetDateTime));

        LogLevel level = LogLevel.valueOf(logTree.get("level").asText());
        String message = logTree.get("message").asText();

        String source = null;
        String type = null;
        String ipAddress = null;
        String error = null;
        String statusCode = null;

        String[] messageParts = message.replaceAll(",", " ").split("\\s+");
        for (String part: messageParts) {
            if (part.contains("=")) {
                String[] subParts = part.split("=");
                if (subParts.length > 0) {
                    switch (subParts[0]) {
                        case "userId":
                            //userId=c82402f4-5d9a-4c3b-b36e-30be69289536
                            source = subParts[1];
                            break;
                        case "username":
                            //username=admin_mika
                            if (source.equals("null")) {
                                source = subParts[1];
                            }
                            break;
                        case "type":
                            //type=LOGIN_ERROR
                            type = subParts[1];
                            break;
                        case "ipAddress":
                            //ipAddress=127.0.0.1
                            ipAddress = subParts[1];
                            break;
                        case "error":
                            //error=invalid_user_credentials
                            error = subParts[1];
                            break;
                        case "statusCode":
                            //statusCode=200
                            statusCode = subParts[1];
                        case "cause":
                            //TODO
                            error = subParts[1];
                            break;
                    }
                }
            }
        }

        Log log = new Log(timestamp, level, message, source, type, ipAddress, error, statusCode);
        return log;
    }

    private void saveAndSendNewAlarms(List<LogAlarm> alarms) {
        LogAlarm lastSaved = this.logAlarmRepository.findFirstByOrderByTimestampDesc();
        List<MaliciousIp> newMaliciousIps = new ArrayList<>();
        List<LogAlarm> newAlarms = new ArrayList<>();
        if (lastSaved != null) {
            for (LogAlarm alarm : alarms) {
                if (alarm.getTimestamp().after(lastSaved.getTimestamp())) {
                    newAlarms.add(logAlarmRepository.save(alarm));
                    if (alarm.getType() == LogAlarmType.NEW_BLACKLIST_IP) {
                        newMaliciousIps.add(new MaliciousIp(alarm.getSource()));
                    }
                }
            }
        } else {
            for (LogAlarm alarm : alarms) {
                newAlarms.add(logAlarmRepository.save(alarm));
                if (alarm.getType() == LogAlarmType.NEW_BLACKLIST_IP) {
                    newMaliciousIps.add(new MaliciousIp(alarm.getSource()));
                }
            }
        }
        maliciousIpRepository.saveAll(newMaliciousIps);
        this.simpMessagingTemplate.convertAndSend("/topic/logs", newAlarms);
    }
}
