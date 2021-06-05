package bsep.tim4.hospitalApp.controller;

import bsep.tim4.hospitalApp.dto.LogConfig;
import bsep.tim4.hospitalApp.dto.LogDto;
import bsep.tim4.hospitalApp.dto.LogFilterDTO;
import bsep.tim4.hospitalApp.service.LogReaderService;
import bsep.tim4.hospitalApp.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value="api/log")
public class LogController {

    @Autowired
    private LogReaderService logReaderService;

    @Autowired
    private LogService logService;

    Logger logger = LoggerFactory.getLogger(LogController.class);

    @PostMapping(value="/add-log-folder", consumes = "application/json")
    // SUPER_ADMIN
    public ResponseEntity<Void> configureLogFolders(Principal principal, @RequestBody @Valid List<LogConfig> logConfigList) {
        try {
            logReaderService.handleConfiguration(logConfigList);
            logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "addLogFolder", HttpStatus.OK, "authorized"));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "addLogFolder", HttpStatus.BAD_REQUEST, "invalid folder path"));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping(value = "/by-page")
    public ResponseEntity<Page<LogDto>> findAllLogs(Principal principal, Pageable pageable) {
        Page<LogDto> logs = logService.findAll(pageable);

        logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                principal.getName(), "findAllLogs", HttpStatus.OK, "authorized"));
        return new ResponseEntity<>(logs, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Page<LogDto>> filterAllLogs(Principal principal, Pageable pageable, @RequestBody @Valid LogFilterDTO logFilterDTO) {
        Page<LogDto> logs = logService.filterAllLogs(pageable, logFilterDTO);

        logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                principal.getName(), "filterAllLogs", HttpStatus.OK, "authorized"));
        return new ResponseEntity<>(logs, HttpStatus.OK);
    }
}
