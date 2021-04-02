package bsep.tim4.adminApp.pki.service;

import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class CsrService {

    public JcaPKCS10CertificationRequest readCsr(String csrString) {

        PEMParser pemParser = null;
        try {
            pemParser = new PEMParser(new StringReader(csrString));
            Object parsedObj = pemParser.readObject();
            JcaPKCS10CertificationRequest jcaPKCS10CertificationRequest = new JcaPKCS10CertificationRequest((PKCS10CertificationRequest)parsedObj);
            return jcaPKCS10CertificationRequest;
        } catch (IOException | ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }
}
