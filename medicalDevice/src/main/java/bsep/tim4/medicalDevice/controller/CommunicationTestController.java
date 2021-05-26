package bsep.tim4.medicalDevice.controller;

import bsep.tim4.medicalDevice.model.PatientStatus;
import bsep.tim4.medicalDevice.service.PatientStatusService;
import bsep.tim4.medicalDevice.util.SignatureUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.cert.CertificateException;

@Service
public class CommunicationTestController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PatientStatusService patientStatusService;

    @Value("${hospital.uri}")
    private String hospitalApplicationUri;

    @Value("${server.ssl.key-store}")
    private String keystore;

    @Value("${server.ssl.key-store-password}")
    private String keystorePass;

    @Value("${server.ssl.key-alias}")
    private String alias;

    private final String hospitalReceivePatientStatus = "/patients/status";

    @Scheduled(fixedRate = 3000, initialDelay = 5000)
    public void sendPatientStatus() throws IOException, CertificateException, CMSException, OperatorCreationException {

        final String uri = hospitalApplicationUri +  hospitalReceivePatientStatus;

        PatientStatus patientStatus = patientStatusService.generatePatientStatus();

        ObjectMapper objectMapper = new ObjectMapper();
        String status = objectMapper.writeValueAsString(patientStatus);

        byte[] signedMessage = SignatureUtil.signMessage(status, keystore, keystorePass, alias);

        HttpEntity<byte[]> request =
                new HttpEntity<>(signedMessage);

        ResponseEntity<Void> responseEntityStr = restTemplate.
                postForEntity(uri, request, Void.class);
    }
}
