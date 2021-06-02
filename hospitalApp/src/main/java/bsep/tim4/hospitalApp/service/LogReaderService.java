package bsep.tim4.hospitalApp.service;

import bsep.tim4.hospitalApp.dto.LogConfig;
import bsep.tim4.hospitalApp.dto.LogConfigList;
import bsep.tim4.hospitalApp.exceptions.ExistingConfigurationFolderException;
import bsep.tim4.hospitalApp.repository.LogRepository;
import bsep.tim4.hospitalApp.util.LogReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

@Service
public class LogReaderService {

    @Value("${configuration.path}")
    private String configurationPath;

    @Autowired
    private Executor taskExecutor;

    @Autowired
    private LogRepository logRepository;

    private LogConfigList logConfigList;
    private List<LogReader> readers;

    public void readConfiguration() throws IOException, InterruptedException {
        ObjectMapper om = new ObjectMapper();
        logConfigList = om.readValue(Paths.get(configurationPath).toFile(), LogConfigList.class);
        this.readers = new ArrayList<>();
        for (LogConfig logConfig: logConfigList.getLogConfigList()) {
            // check if folder exists
            checkFolderPath(logConfig.getPath());
            LogReader reader = new LogReader(logRepository, logConfig);
            taskExecutor.execute(reader);
            readers.add(reader);
            //LogReader logReader = new LogReader(logRepository, logConfig);
            //logReader.run();
        }
    }

    public void addConfiguration(LogConfig logConfig) throws IOException, ExistingConfigurationFolderException {
        // check if folder exists
        checkFolderPath(logConfig.getPath());
        // check if folder is already in configuration
        if (logConfigList.containsFolder(logConfig.getPath())) {
            throw new ExistingConfigurationFolderException(logConfig.getPath());
        };
        logConfigList.getLogConfigList().add(logConfig);
        LogReader reader = new LogReader(logRepository, logConfig);
        taskExecutor.execute(reader);
        readers.add(reader);
    }

    @PreDestroy
    public void writeConfiguration() throws JsonProcessingException, FileNotFoundException {
        List<LogConfig> logConfigs = new ArrayList<>();
        for (LogReader r: readers) {
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
