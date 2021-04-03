package bsep.tim4.hospitalApp.controller;

import bsep.tim4.hospitalApp.dto.CSRDto;
import bsep.tim4.hospitalApp.service.CSRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

@RestController
@RequestMapping(value="api/csr")
public class CSRController {

    @Autowired
    private CSRService csrService;

    @Value("${admin.uri}")
    private String adminApplicationUri;

    private final String sendCsrUri = "/csr/receive";

    @PostMapping(value="/create")
    public ResponseEntity<String> createCsr(@Valid @RequestBody CSRDto csrDto) {
        String csr = csrService.createCSR(csrDto);

        final String sendCsrFullUri = adminApplicationUri +  sendCsrUri;

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request =
                new HttpEntity<>(csr);
        ResponseEntity<String> responseEntityStr = restTemplate.
                postForEntity(sendCsrFullUri, request, String.class);

        //String certificate = responseEntityStr.getBody();
        //csrService.storeCertificate(certificate);
        return new ResponseEntity<>("uspesno poslat zahtev", HttpStatus.OK);
    }
}
