package bsep.tim4.hospitalApp.service;

import bsep.tim4.hospitalApp.dto.CSRDto;

import bsep.tim4.hospitalApp.util.CSRGenerator;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.bouncycastle.openssl.PEMKeyPair;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import java.io.FileWriter;
import java.security.interfaces.RSAPrivateKey;

@Service
public class CSRService {

    @Autowired
    private KeyStoreService keyStoreService;

    private CSRGenerator csrGenerator;

    public CSRService() {
        this.csrGenerator = new CSRGenerator();
    }

    public String createCSR(CSRDto csrDto) {
        String separator = System.getProperty("file.separator");
        KeyPair keyPair = this.csrGenerator.generateKeyPair();
        // cuvanje privatnog kljuca
        keyStoreService.loadKeyStore();
        //keyStoreService.savePrivateKey(csrDto.getEmail(), keyPair.getPrivate(), csrDto.getKeyPassword(), null);
        JcaPEMWriter jcaPEMWriter = null;
        try {
            jcaPEMWriter = new JcaPEMWriter(new FileWriter("src"+ separator + "main"+ separator +"resources"+ separator +"donottouch.key"));
            jcaPEMWriter.writeObject(this.csrGenerator.getPrivateKey(), null);
            jcaPEMWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        keyStoreService.saveKeyStore();
        // generisanje CertificateSigningRequest-a
        PKCS10CertificationRequest csr = this.csrGenerator.generateCSR(csrDto);
            // vraca byte[]
            //return csr.getEncoded();
        // u ovoj formi se obicno prosledjuje
        readPrivateKey();
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

    public PrivateKeyInfo readPrivateKey() {

        String separator = System.getProperty("file.separator");
        PEMParser pemParser = null;
        try {
            pemParser = new PEMParser(new FileReader("src"+ separator + "main"+ separator +"resources"+ separator +"donottouch.key"));
//            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
//            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(pemParser.readObject());
//            RSAPrivateKey privateKey = (RSAPrivateKey) converter.getPrivateKey(privateKeyInfo);
            PEMKeyPair parsedObj = (PEMKeyPair)pemParser.readObject();
            PrivateKeyInfo privateKey = parsedObj.getPrivateKeyInfo();
            return privateKey;
        } catch (IOException | ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }
}
