package bsep.tim4.hospitalApp.service;

import bsep.tim4.hospitalApp.dto.LogConfig;
import bsep.tim4.hospitalApp.dto.LogConfigList;
import bsep.tim4.hospitalApp.util.LogReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.Executor;

@Service
public class LogReaderService {

    @Value("${configuration.path}")
    private String configurationPath;

    @Autowired
    private Executor taskExecutor;

    public void readConfiguration() throws IOException, InterruptedException {
        ObjectMapper om = new ObjectMapper();
        LogConfigList logConfigList = om.readValue(Paths.get(configurationPath).toFile(), LogConfigList.class);
        for (LogConfig logConfig: logConfigList.getLogConfigList()) {
            //taskExecutor.execute(new LogReader(logConfig));
            LogReader logReader = new LogReader(logConfig);
            logReader.run();
        }
    }
}
