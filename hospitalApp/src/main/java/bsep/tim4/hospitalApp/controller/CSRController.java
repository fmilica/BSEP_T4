package bsep.tim4.hospitalApp.controller;

import bsep.tim4.hospitalApp.dto.CSRDto;
import bsep.tim4.hospitalApp.service.CSRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

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

    @PostMapping(value="/create")
    // ADMIN
    public ResponseEntity<Void> createCsr(@RequestHeader("Authorization") String token, @Valid @RequestBody CSRDto csrDto)
            throws KeyStoreException, NoSuchAlgorithmException, IOException, KeyManagementException, CertificateException {
        String csr = csrService.createCSR(csrDto);

        final String sendCsrFullUri = adminApplicationUri +  sendCsrUri;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<String> request =
                new HttpEntity<>(csr, headers);
        ResponseEntity<String> responseEntityStr = restTemplate.
                postForEntity(sendCsrFullUri, request, String.class);

        String csrReturn = responseEntityStr.getBody();
        System.out.println(csrReturn);
        //csrService.storeCertificate(certificate);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
