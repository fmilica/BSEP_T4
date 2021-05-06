package bsep.tim4.hospitalApp.controller;

import bsep.tim4.hospitalApp.util.SignatureUtil;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.cert.CertificateException;

@RestController
@RequestMapping(value="api/log")
public class LogController {

    @Value("${trust.store}")
    private String trustStore;

    @Value("${trust.store.password}")
    private String trustStorePass;

    @PostMapping(value="/receive")
    // ADMIN
    public ResponseEntity<String> receiveLog(@RequestBody byte[] signedMessage) {
        try {
            if (SignatureUtil.checkTrustedCertificate(signedMessage, trustStore, trustStorePass)) {
                if (SignatureUtil.verifySignature(signedMessage)) {
                    System.out.println(signedMessage);
                    return new ResponseEntity<>("Potpis validan " + signedMessage, HttpStatus.OK);
                }
                return new ResponseEntity<>("Nevalidan potpis!", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("Ne verujemo ovom sertifikatu!", HttpStatus.BAD_REQUEST);
        } catch (CertificateException | CMSException | IOException | OperatorCreationException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Ne verujemo ovom sertifikatu!", HttpStatus.BAD_REQUEST);
        }
    }
}
