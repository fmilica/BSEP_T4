package bsep.tim4.hospitalApp.controller;

import bsep.tim4.hospitalApp.dto.ReportDto;
import bsep.tim4.hospitalApp.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping(value="api/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    Logger logger = LoggerFactory.getLogger(ReportController.class);

    @PostMapping
    // ADMIN
    public ResponseEntity<ReportDto> generateReport(Principal principal,
                                                    @Valid @RequestBody ReportDto reportDto) {

        reportDto = this.reportService.generateReport(reportDto);
        logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                principal.getName(), "generateReport", HttpStatus.OK, "authorized"));
        return new ResponseEntity<>(reportDto, HttpStatus.OK);
    }
}
