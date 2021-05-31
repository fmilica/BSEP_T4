package bsep.tim4.hospitalApp.controller;

import bsep.tim4.hospitalApp.util.SignatureUtil;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.cert.CertificateException;

@RestController
@RequestMapping(value="api/device-message")
public class DeviceMessageController {

    @Autowired
    RestTemplate restTemplate;

    @Value("${trust.store}")
    private String trustStore;

    @Value("${trust.store.password}")
    private String trustStorePass;

    @Value("${super-admin.uri}")
    private String adminApplicationUri;

    private final String validateCertificateUrl = "/certificate/validate/";

    @PostMapping(value="/receive")
    // UNAUTHORIZED
    // getting data from MedicalDevice application
    public ResponseEntity<String> receiveLog(@RequestBody byte[] signedMessage) {
        try {
            int serialNumber = SignatureUtil.checkTrustedCertificate(signedMessage, trustStore, trustStorePass);
            if (serialNumber != -1) {
                final String validateCertificateFullUrl = adminApplicationUri +  validateCertificateUrl + serialNumber;
                // provera da li je sertifikat revoked
                ResponseEntity<Boolean> responseEntityStr = restTemplate.
                        getForEntity(validateCertificateFullUrl, Boolean.class);
                boolean valid = responseEntityStr.getBody();
                if (valid) {
                    if (SignatureUtil.verifySignature(signedMessage)) {
                        return new ResponseEntity<>("Potpis validan " + signedMessage, HttpStatus.OK);
                    }
                    return new ResponseEntity<>("Sertifikat je revoked!", HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>("Nevalidan potpis!", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("Ne verujemo ovom sertifikatu!", HttpStatus.BAD_REQUEST);
        } catch (CertificateException | CMSException | IOException | OperatorCreationException | NullPointerException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Ne verujemo ovom sertifikatu!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    // SUPER_ADMIN, ADMIN
    public ResponseEntity<String> viewLogs() {
        return new ResponseEntity<>("Super admin i admin imaju uvid u logove -> SSO demonstracija.", HttpStatus.OK);
    }
}