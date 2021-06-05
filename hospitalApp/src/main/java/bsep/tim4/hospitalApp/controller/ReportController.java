package bsep.tim4.hospitalApp.controller;

import bsep.tim4.hospitalApp.dto.ReportDto;
import bsep.tim4.hospitalApp.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value="api/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping
    public ResponseEntity<Void> generateReport(/*Principal principal,*/
            @Valid @RequestBody ReportDto reportDto) {

        this.reportService.generateReport(reportDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
