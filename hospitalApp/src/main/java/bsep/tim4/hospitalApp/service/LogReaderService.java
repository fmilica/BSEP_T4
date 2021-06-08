package bsep.tim4.hospitalApp.service;

import bsep.tim4.hospitalApp.dto.LogConfig;
import bsep.tim4.hospitalApp.dto.LogConfigList;
import bsep.tim4.hospitalApp.repository.LogAlarmRepository;
import bsep.tim4.hospitalApp.repository.LogRepository;
import bsep.tim4.hospitalApp.repository.MaliciousIpRepository;
import bsep.tim4.hospitalApp.util.ACLUtil;
import bsep.tim4.hospitalApp.util.LogReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LogReaderService {

    @Value("${configuration.path}")
    private String configurationPath;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private LogRepository logRepository;
	
	@Autowired
    private KieSessionService kieSessionService;

    @Autowired
    private LogAlarmRepository logAlarmRepository;

    @Autowired
    private MaliciousIpRepository maliciousIpRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private LogConfigList logConfigList;
    private Map<String, Pair<Thread, LogReader>> threads;

    public void readConfiguration() {
        try {
            ObjectMapper om = new ObjectMapper();
            logConfigList = om.readValue(Paths.get(configurationPath).toFile(), LogConfigList.class);
            this.threads = new HashMap<>();
            for (LogConfig logConfig : logConfigList.getLogConfigList()) {
                // check if folder exists
                checkFolderPath(logConfig.getPath());
                // add ACL restraints for folder
                ACLUtil.setupACL(logConfig.getPath());
                LogReader reader = new LogReader(logRepository, logConfig,
                        kieSessionService, logAlarmRepository, maliciousIpRepository, simpMessagingTemplate);
                Thread t = taskExecutor.createThread(reader);
                t.start();
                threads.put(logConfig.getPath(), new Pair<>(t, reader));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleConfiguration(List<LogConfig> newLogConfigs) throws IOException {
        List<LogConfig> forRemoval = new ArrayList<>();
        for (LogConfig logConfig : newLogConfigs) {
            // check if folder exists
            checkFolderPath(logConfig.getPath());
            // check if folder is already in configuration
            if (logConfigList.containsFolder(logConfig.getPath())) {
                forRemoval.add(logConfig);
                // interrupt thread
                Thread t = threads.get(logConfig.getPath()).getKey();
                t.interrupt();
                threads.remove(logConfig.getPath());
                continue;
            };
            // add ACL restraints for folder
            ACLUtil.setupACL(logConfig.getPath());
            // create new thread for processing new folders
            LogReader reader = new LogReader(logRepository, logConfig,
                    kieSessionService, logAlarmRepository, maliciousIpRepository, simpMessagingTemplate);
            Thread t = taskExecutor.createThread(reader);
            t.start();
            threads.put(logConfig.getPath(), new Pair<>(t, reader));
        }
        newLogConfigs.removeAll(forRemoval);
        logConfigList.getLogConfigList().addAll(newLogConfigs);
    }

    @PreDestroy
    public void writeConfiguration() throws JsonProcessingException, FileNotFoundException {
        List<LogConfig> logConfigs = new ArrayList<>();
        for (Pair<Thread, LogReader> tr: threads.values()) {
            LogReader r = tr.getValue();
            logConfigs.add(r.getLogConfig());
        }
        LogConfigList logConfigList = new LogConfigList(logConfigs);
        ObjectMapper om = new ObjectMapper();
        String logConfigListJson = om.writerWithDefaultPrettyPrinter().writeValueAsString(logConfigList);
        PrintWriter out = new PrintWriter(configurationPath);
        out.println(logConfigListJson);
        out.close();
    }

    private void checkFolderPath(String folderPath) throws IOException {
        Path path = Paths.get(folderPath);
        if (Files.notExists(path)) {
            throw new IOException("Folder with given path does not exist.");
        }
    }
}
