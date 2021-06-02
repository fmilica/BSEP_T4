package bsep.tim4.adminApp.pki.controller;

import bsep.tim4.adminApp.pki.model.Hospital;
import bsep.tim4.adminApp.pki.model.dto.LogConfig;
import bsep.tim4.adminApp.pki.service.HospitalService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(value="api/hospital")
@Validated
public class HospitalController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HospitalService hospitalService;

    Logger logger = LoggerFactory.getLogger(HospitalController.class);

    @GetMapping
    // SUPER ADMIN
    public ResponseEntity<List<Hospital>> findAll() {
        List<Hospital> hospitals = hospitalService.getAll();
        return new ResponseEntity<>(hospitals, HttpStatus.OK);
    }

    @PostMapping(value="/add-simulator/{hospitalId}")
    // SUPER ADMIN
    public ResponseEntity<String> addSimulator(@PathVariable @NotNull(message = "Hospital id cannot be empty")
                                                   @Positive( message = "Hospital id is invalid") Long hospitalId,
                                               @RequestBody @Valid List<LogConfig> logConfigList) {
        // vezivanje simulatora i bolnice
        // slanje konfiguracije bolnici

        String csr = csrService.createCSR(csrDto);

        final String sendCsrFullUri = adminApplicationUri +  sendCsrUri;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<String> request =
                new HttpEntity<>(csr, headers);
        ResponseEntity<String> responseEntityStr = restTemplate.
                postForEntity(sendCsrFullUri, request, String.class);

        String csrReturn = responseEntityStr.getBody();
        logger.info(String.format("%s called method %s with status code %s: %s",
                principal.getName(), "createCsr", HttpStatus.OK, "CSR created and sent"));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
