package bsep.tim4.hospitalApp.controller;

import bsep.tim4.hospitalApp.dto.CSRDto;
import bsep.tim4.hospitalApp.service.CSRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value="api/csr")
public class CSRController {

    @Autowired
    private CSRService csrService;

    @PostMapping(value="/create")
    public ResponseEntity<String> createCsr(@Valid @RequestBody CSRDto csrDto) {
        String csr = csrService.createCSR(csrDto);
        return new ResponseEntity<>(csr, HttpStatus.OK);
    }
}
