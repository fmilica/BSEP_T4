package bsep.tim4.hospitalApp.service;

import bsep.tim4.hospitalApp.dto.CSRDto;

import bsep.tim4.hospitalApp.util.CSRGenerator;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.bouncycastle.cert.X509CertificateHolder;

@Service
public class CSRService {

    @Autowired
    private KeyStoreService keyStoreService;

    private CSRGenerator csrGenerator;

    public CSRService() {
        this.csrGenerator = new CSRGenerator();
    }

    public String createCSR(CSRDto csrDto) {
        KeyPair keyPair = this.csrGenerator.generateKeyPair();
        // cuvanje privatnog kljuca
        keyStoreService.loadKeyStore();
        //keyStoreService.savePrivateKey(csrDto.getEmail(), keyPair.getPrivate(), csrDto.getKeyPassword(), null);
        keyStoreService.saveKeyStore();
        // generisanje CertificateSigningRequest-a
        PKCS10CertificationRequest csr = this.csrGenerator.generateCSR(csrDto);
            // vraca byte[]
            //return csr.getEncoded();
        // u ovoj formi se obicno prosledjuje
        return this.csrGenerator.convertCsr(csr);
    }

    public void storeCertificate(String certString) {
        PEMParser pemParser = null;
        Security.addProvider(new BouncyCastleProvider());
        try {
            // dekodiranje
            pemParser = new PEMParser(new StringReader(certString));
            Object parsedObj = pemParser.readObject();
            X509CertificateHolder certificateHolder = (X509CertificateHolder) parsedObj;
            // Builder generise sertifikat kao objekat klase X509CertificateHolder
            // potrebno je certHolder konvertovati u sertifikat, za sta se koristi certConverter
            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");

            // Konvertuje objekat u sertifikat
            X509Certificate certificate = certConverter.getCertificate(certificateHolder);
            // cuvanje u keystore
            keyStoreService.loadKeyStore();
            keyStoreService.saveCertificate("nas-sertifikat", certificate);
            keyStoreService.saveKeyStore();
        } catch (IOException | ClassCastException | CertificateException e) {
            e.printStackTrace();
        }
    }
}
