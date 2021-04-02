package bsep.tim4.adminApp.pki.controller;

import bsep.tim4.adminApp.pki.model.SubjectData;
import bsep.tim4.adminApp.pki.service.CertificateService;
import bsep.tim4.adminApp.pki.service.CsrService;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

@RestController
@RequestMapping(value="api/csr")
public class CsrController {

    @Autowired
    private CertificateService certificateService;

    @PostMapping(value="/receive")
    public ResponseEntity<String> receiveCsr(@RequestBody String csr) {
        String certificate = certificateService.generateCertificate(csr);
        System.out.println(certificate);
        return new ResponseEntity<>(certificate, HttpStatus.OK);
    }
}
