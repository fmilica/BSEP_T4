package bsep.tim4.hospitalApp.controller;

import bsep.tim4.hospitalApp.dto.CSRDto;
import bsep.tim4.hospitalApp.service.CSRService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping(value="api/csr")
public class CSRController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private CSRService csrService;

    @Value("${super-admin.uri}")
    private String adminApplicationUri;

    private final String sendCsrUri = "/csr/receive";

    Logger logger = LoggerFactory.getLogger(CSRController.class);

    @PostMapping(value="/create")
    // ADMIN
    public ResponseEntity<Void> createCsr(Principal principal, @RequestHeader("Authorization") String token, @Valid @RequestBody CSRDto csrDto) {
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
