package bsep.tim4.hospitalApp.util;

import bsep.tim4.hospitalApp.dto.LogConfig;
import bsep.tim4.hospitalApp.model.Log;
import bsep.tim4.hospitalApp.model.LogLevel;
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
import java.util.Date;

public class LogReader /*implements Runnable*/ {

    private LogConfig logConfig;

    public LogReader(LogConfig logConfig) {
        this.logConfig = logConfig;
    }

    //@SneakyThrows
    //@Override
    public void run() throws IOException, InterruptedException {
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
                String changedFile = logConfig.getPath() + File.separator + event.context();

                Long startRow = 0L;
                if (!changedFile.contains("~")) {
                    handleLog(changedFile, startRow);

                    System.out.println(
                            "Event kind:" + event.kind()
                                    + ". File affected: " + event.context() + ".");
                }
            }
            key.reset();
        }
    }

    private void handleLog(String filePath, Long startPos) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(filePath, "r");
        raf.seek(startPos);

        String line;
        while ((line = raf.readLine()) != null) {
            if (line.contains(logConfig.getFilter())) {
                Log log = parseLog(line);
            }
        }

        Long endPos = raf.getFilePointer();
        raf.close();
    }

    private Log parseLog(String logLine) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        JsonNode logTree = om.readTree(logLine);
        // spring
        String timestampString = logTree.get("@timestamp").asText();
        if (timestampString == null) {
            // keycloak
            timestampString = logTree.get("timestamp").asText();
        }
        // fromat ISO 8601
        DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(timestampString, timeFormatter);
        Date timestamp = Date.from(Instant.from(offsetDateTime));

        LogLevel level = LogLevel.valueOf(logTree.get("level").asText());
        String message = logTree.get("message").asText();

        String userId;
        String type;
        String ipAddress;
        String error;

        String[] messageParts = message.replaceAll(",", " ").split("\\s+");
        for (String part: messageParts) {
            String[] subParts = part.split("=");
            switch (subParts[0]) {
                case "userId":
                    //userId=c82402f4-5d9a-4c3b-b36e-30be69289536
                    userId = subParts[1];
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
            }
        }
        //private String source; - iz message id
        //private int status_code;
        //private boolean processed;
    }
	/*
    {
        "@timestamp":"2021-05-24T04:09:55.223+02:00",
        "@version":"1",
        "message":"User with userId=c82402f4-5d9a-4c3b-b36e-30be69289536 called method findAllCsr with statusCode=200 OK: authorized",
        "logger_name":"bsep.tim4.adminApp.pki.controller.CsrController",
        "thread_name":"https-jsse-nio-8082-exec-6",
        "level":"INFO",
        "level_value":20000
    }
    {
        "timestamp":"2021-05-31T23:50:17.873+02:00",
        "sequence":144,
        "loggerClassName":"org.jboss.logging.Logger",
        "loggerName":"org.keycloak.events",
        "level":"WARN",
        "message":"type=LOGIN_ERROR, realmId=BSEP_T4, clientId=HospitalFrontend, " +
            "userId=640d99cc-3299-4a3a-a170-dc3ebabf6775, " +
            "ipAddress=127.0.0.1, " +
            "error=invalid_user_credentials, " +
            "auth_method=openid-connect, auth_type=code, " +
            "redirect_uri=https://localhost:4201/, " +
            "code_id=fc409018-2396-4a7f-9aa7-4c0f261bbf79, " +
            "username=admin_mika, authSessionParentId=fc409018-2396-4a7f-9aa7-4c0f261bbf79, " +
            "authSessionTabId=ICA8Jnzia7g",
        "threadName":"default task-5",
        "threadId":185,
        "mdc":{
        },
        "ndc":"",
        "hostName":"desktop-jokuti3",
        "processName":"jboss-modules.jar",
        "processId":956
    }*/
}
