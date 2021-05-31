package bsep.tim4.hospitalApp.service;

import bsep.tim4.hospitalApp.dto.CSRDto;
import bsep.tim4.hospitalApp.util.CSRGenerator;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Service
public class CSRService {

    private CSRGenerator csrGenerator;

    public CSRService() {
        this.csrGenerator = new CSRGenerator();
    }

    public String createCSR(CSRDto csrDto) throws OperatorCreationException, IOException {
        PKCS10CertificationRequest csr = this.csrGenerator.generateCSR(csrDto);
        return this.csrGenerator.convertCsr(csr);
    }
}
