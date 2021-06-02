package bsep.tim4.hospitalApp.controller;

import bsep.tim4.hospitalApp.dto.LogConfig;
import bsep.tim4.hospitalApp.exceptions.ExistingConfigurationFolderException;
import bsep.tim4.hospitalApp.service.LogReaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping(value="api/log")
public class LogController {

    @Autowired
    private LogReaderService logReaderService;

    Logger logger = LoggerFactory.getLogger(LogController.class);

    @PostMapping(value="/add-log-folder")
    // SUPER_ADMIN
    public ResponseEntity<Void> addLogFolder(/*Principal principal*/@RequestBody LogConfig logConfig) {
        try {
            logReaderService.addConfiguration(logConfig);
            //logger.info(String.format("%s called method %s with status code %s: %s",
            //        principal.getName(), "addLogFolder", HttpStatus.OK, "authorized"));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            //logger.info(String.format("%s called method %s with status code %s: %s",
            //        principal.getName(), "addLogFolder", HttpStatus.BAD_REQUEST, "invalid folder path"));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (ExistingConfigurationFolderException e) {
            //logger.info(String.format("%s called method %s with status code %s: %s",
            //        principal.getName(), "addLogFolder", HttpStatus.BAD_REQUEST, "folder in use"));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
