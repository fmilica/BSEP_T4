package bsep.tim4.hospitalApp.service;

import bsep.tim4.hospitalApp.data.IssuerData;
import bsep.tim4.hospitalApp.keystores.KeyStoreReader;
import bsep.tim4.hospitalApp.keystores.KeyStoreWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.File;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;

@Service
public class KeyStoreService {

    @Value("${pki.keystore-path}")
    private String keyStorePath;

    @Value("${pki.keystore-name}")
    private String keyStoreName;

    @Value("${pki.keystore-password}")
    private String keyStorePass;

    @Value("${pki.keystore-symetric-name}")
    private String symKeyKeystore;

    @Value("${pki.keystore-symetric-key-alias}")
    private String symKeyAlias;

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

    public void loadSymKeyStore() {
        String keyStoreFullName = keyStorePath + symKeyKeystore;
        File keyStoreFile = new File(keyStoreFullName);
        if (!keyStoreFile.exists()) {
            this.keyStoreWriter.createKeyStore(keyStoreFullName, keyStorePass.toCharArray());
        } else {
            this.keyStoreWriter.loadSymKeyStore(keyStoreFullName, keyStorePass.toCharArray());
        }
    }

    public Key getSymKey() {
        return this.keyStoreReader.getSymKey(symKeyAlias, keyStorePass.toCharArray(),
                keyStorePath + symKeyKeystore, keyStorePass);
    }

    public void createSymetricKey() throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        // minimum 128, 256 idealno
        generator.init(256);
        SecretKey secKey = generator.generateKey();
        this.keyStoreWriter.writeSecretKey(symKeyAlias, secKey,
                keyStorePass.toCharArray(), keyStorePath + symKeyKeystore);

    }
}
