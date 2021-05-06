package bsep.tim4.hospitalApp.controller;

import bsep.tim4.hospitalApp.dto.CSRDto;
import bsep.tim4.hospitalApp.service.CSRService;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@RestController
@RequestMapping(value="api/csr")
public class CSRController {

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

        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(new FileInputStream(new ClassPathResource("truststoreHospital.jks").getFile()),
                ("HospitalSecretPass").toCharArray());

        SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();

        CloseableHttpClient httpClient = HttpClients.custom().setSSLContext(sslContext).build();
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);

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
