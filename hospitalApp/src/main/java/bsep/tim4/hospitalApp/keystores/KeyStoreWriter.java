package bsep.tim4.hospitalApp.keystores;

import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class KeyStoreWriter {
    // KeyStore je Java klasa za pisanje specijalizovanih datoteka koje se koriste za cuvanje kljuceva
    // Tri tipa entiteta koji se obicno nalaze u ovakvim datotekama su:
    // - Sertifikati koji ukljucuju javni kljuc
    // - Privatni kljucevi
    // - Tajni (simetricni) kljucevi, koji se koriste u simetricnima siframa
    private KeyStore keyStore;
    private KeyStore symKeyStore;

    public KeyStoreWriter() {
        try {
            keyStore = KeyStore.getInstance("JKS", "SUN");
            symKeyStore = KeyStore.getInstance("JCEKS");
        } catch (KeyStoreException | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    public void createKeyStore(String fileName, char[] password) {
        try {
            keyStore.load(null, password);
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            keyStore.store(fileOutputStream, password);
            fileOutputStream.close();
        } catch (NoSuchAlgorithmException | CertificateException | IOException | KeyStoreException e) {
            e.printStackTrace();
        }
    }

    public void loadKeyStore(String fileName, char[] password) {
        try {
            if (fileName != null) {
                keyStore.load(new FileInputStream(fileName), password);
            } else {
                // Ako je cilj kreirati novi KeyStore poziva se i dalje load, pri cemu je prvi parametar null
                keyStore.load(null, password);
            }
        } catch (NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSymKeyStore(String fileName, char[] password) {
        try {
            if (fileName != null) {
                symKeyStore.load(new FileInputStream(fileName), password);
            } else {
                // Ako je cilj kreirati novi KeyStore poziva se i dalje load, pri cemu je prvi parametar null
                symKeyStore.load(null, password);
            }
        } catch (NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
        }
    }

    public void saveKeyStore(String fileName, char[] password) {
        try {
            keyStore.store(new FileOutputStream(fileName), password);
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
        }
    }

    public void writePrivateKey(String alias, PrivateKey privateKey, char[] password, Certificate certificate) {
        try {
            keyStore.setKeyEntry(alias, privateKey, password, new Certificate[]{certificate});
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    public void writeCertificate(String alias, Certificate certificate) {
        try {
            keyStore.setCertificateEntry(alias, certificate);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    public void writeSecretKey(String alias, SecretKey secretKey, char[] password, String fileName) {
        try {
            symKeyStore = KeyStore.getInstance("JCEKS");
            symKeyStore.load(new FileInputStream(fileName), password);
            KeyStore.SecretKeyEntry secret
                    = new KeyStore.SecretKeyEntry(secretKey);
            KeyStore.ProtectionParameter secretPassword
                    = new KeyStore.PasswordProtection(password);
            symKeyStore.setEntry(alias, secret, secretPassword);
            symKeyStore.store(new FileOutputStream(fileName), password);
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            e.printStackTrace();
        }
    }
}
