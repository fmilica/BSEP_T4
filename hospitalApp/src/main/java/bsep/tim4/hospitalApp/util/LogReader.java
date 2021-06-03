package bsep.tim4.hospitalApp.util;

import bsep.tim4.hospitalApp.dto.LogConfig;
import bsep.tim4.hospitalApp.model.Log;
import bsep.tim4.hospitalApp.model.LogLevel;
import bsep.tim4.hospitalApp.repository.LogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    public LogReader(LogRepository logRepository, LogConfig logConfig) {
        this.logRepository = logRepository;
        this.logConfig = logConfig;
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
            Log log = parseLog(line);
            if (log != null) {
                logs.add(log);
            }
        }
        Long endPos = raf.getFilePointer();
        logConfig.setLogFilePointer(fileName, endPos);
        raf.close();

        // OVDE
        logRepository.saveAll(logs);
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
                            error = subParts[1];
                            break;
                    }
                }
            }
        }

        Log log = new Log(timestamp, level, message, source, type, ipAddress, error, statusCode);
        return log;
    }

}
