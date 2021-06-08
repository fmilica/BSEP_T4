package bsep.tim4.hospitalApp.controller;

import bsep.tim4.hospitalApp.dto.LogAlarmDto;
import bsep.tim4.hospitalApp.service.LogAlarmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value="api/log-alarm")
public class LogAlarmController {

    @Autowired
    private LogAlarmService logAlarmService;

    Logger logger = LoggerFactory.getLogger(LogAlarmController.class);

    @GetMapping(value = "/by-page")
    public ResponseEntity<Page<LogAlarmDto>> findAllLogAlarms(Principal principal, Pageable pageable) {
        Page<LogAlarmDto> logAlarms = logAlarmService.findAll(pageable);

        logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                principal.getName(), "findAllLogAlarms", HttpStatus.OK, "authorized"));
        return new ResponseEntity<>(logAlarms, HttpStatus.OK);
    }

    @GetMapping(value = "/types")
    public ResponseEntity<List<String>> findAllAlarmTypes(Principal principal) {
        List<String> logAlarmTypes = logAlarmService.findAllAlarmTypes();

        logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                principal.getName(), "findAllAlarmTypes", HttpStatus.OK, "authorized"));
        return new ResponseEntity<>(logAlarmTypes, HttpStatus.OK);
    }
}
