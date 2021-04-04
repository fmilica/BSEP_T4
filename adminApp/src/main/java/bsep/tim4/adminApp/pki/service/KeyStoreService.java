package bsep.tim4.adminApp.pki.service;

import bsep.tim4.adminApp.pki.keystores.KeyStoreReader;
import bsep.tim4.adminApp.pki.keystores.KeyStoreWriter;
import bsep.tim4.adminApp.pki.model.IssuerData;
import bsep.tim4.adminApp.pki.model.dto.CertificateViewDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.List;

@Service
public class KeyStoreService {

    @Value("${pki.keystore-path}")
    private String keyStorePath;

    @Value("${pki.keystore-name}")
    private String keyStoreName;

    @Value("${pki.keystore-password}")
    private String keyStorePass;

    @Value("${rootCA-key-pass}")
    private String rootCAPass;

    private final KeyStoreReader keyStoreReader;
    private final KeyStoreWriter keyStoreWriter;

    public KeyStoreService() {
        this.keyStoreReader = new KeyStoreReader();
        this.keyStoreWriter = new KeyStoreWriter();
    }

    public void createKeyStore() {
        // kreira
        this.keyStoreWriter.loadKeyStore(null, keyStorePass.toCharArray());
        // pise u fajl
        this.keyStoreWriter.saveKeyStore(keyStorePath + keyStoreName, keyStorePass.toCharArray());
    }

    public void loadKeyStore() {
        String keyStoreFullName = keyStorePath + keyStoreName;
        File keyStoreFile = new File(keyStoreFullName);
        if(!keyStoreFile.exists()){
            this.keyStoreWriter.createKeyStore(keyStoreFullName, keyStorePass.toCharArray());
        } else {
            this.keyStoreWriter.loadKeyStore(keyStoreFullName, keyStorePass.toCharArray());
        }
    }

    public void saveKeyStore() {
        this.keyStoreWriter.saveKeyStore(keyStorePath + keyStoreName, keyStorePass.toCharArray());
    }

    //mozemo i privatni kljuc dodatno da sifrujemo zato postoji rootCAPass
    public void savePrivateKey(String alias, PrivateKey privateKey, Certificate certificate) {
        this.keyStoreWriter.writePrivateKey(alias, privateKey,rootCAPass.toCharArray(), certificate);
    }

    public PrivateKey loadPrivateKey(String alias) {
        return this.keyStoreReader.readPrivateKey(
                keyStorePath + keyStoreName, keyStorePass, alias, rootCAPass);
    }

    public void saveCertificate(String alias, Certificate certificate) {
        this.keyStoreWriter.writeCertificate(alias, certificate);
    }

    public Certificate loadCertificate(String alias) {
        return this.keyStoreReader.readCertificate(
                keyStorePath + keyStoreName, keyStorePass, alias);
    }

    // dobavljanje privatnog kljuca (i podataka o vlasniku) vezanog za sertifikat sa datim aliasom
    public IssuerData loadIssuerData(String alias, String keyPassword) {
        return this.keyStoreReader.readIssuerFromStore(
                keyStorePath + keyStoreName, alias,
                        keyStorePass.toCharArray(), keyPassword.toCharArray());
    }

    public List<IssuerData> loadAllCAIssuers() {
        return this.keyStoreReader.readAllCAIssuers(keyStorePath, keyStorePass, rootCAPass);
    }

    public CertificateViewDTO loadAllCertificates() {
        return this.keyStoreReader.readAllCertificates(keyStorePath, keyStorePass, rootCAPass);
    }
}
