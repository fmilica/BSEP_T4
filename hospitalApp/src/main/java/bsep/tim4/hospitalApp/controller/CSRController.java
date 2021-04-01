package bsep.tim4.hospitalApp.controller;

import bsep.tim4.hospitalApp.dto.CSRDto;
import bsep.tim4.hospitalApp.service.CSRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="csr/")
public class CSRController {

    @Autowired
    private CSRService csrService;
/*
    @PostMapping(value="createCsr")
    public ResponseEntity<> createCsr(CSRDto csrDto) {

    }*/
}
