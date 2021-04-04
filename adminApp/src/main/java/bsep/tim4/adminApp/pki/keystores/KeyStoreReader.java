package bsep.tim4.adminApp.pki.keystores;

import bsep.tim4.adminApp.pki.model.IssuerData;
import bsep.tim4.adminApp.pki.model.dto.CertificateViewDTO;
import bsep.tim4.adminApp.pki.model.enums.CertificateStatusEnum;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

import java.io.*;
import java.nio.file.Files;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class KeyStoreReader {
    // KeyStore je Java klasa za citanje specijalizovanih datoteka koje se koriste za cuvanje kljuceva
    // Tri tipa entiteta koji se obicno nalaze u ovakvim datotekama su:
    // - Sertifikati koji ukljucuju javni kljuc
    // - Privatni kljucevi
    // - Tajni kljucevi (simetricni), koji se koriste u simetricnima siframa
    private KeyStore keyStore;

    public KeyStoreReader() {
        try {
            keyStore = KeyStore.getInstance("JKS", "SUN");
        } catch (KeyStoreException | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    /**
     * Zadatak ove funkcije jeste da ucita podatke o izdavaocu i odgovarajuci privatni kljuc.
     * Ovi podaci se mogu iskoristiti da se novi sertifikati izdaju.
     *
     * @param keyStoreFile - datoteka odakle se citaju podaci
     * @param alias        - alias putem kog se identifikuje sertifikat izdavaoca
     * @param password     - lozinka koja je neophodna da se otvori key store
     * @param keyPass      - lozinka koja je neophodna da se izvuce privatni kljuc
     * @return - podatke o izdavaocu i odgovarajuci privatni kljuc
     */
    public IssuerData readIssuerFromStore(String keyStoreFile, String alias, char[] password, char[] keyPass) {
        try {
            // Datoteka se ucitava
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            keyStore.load(in, password);

            // Iscitava se sertifikat koji ima dati alias
            Certificate cert = keyStore.getCertificate(alias);

            // Iscitava se privatni kljuc vezan za javni kljuc koji se nalazi na sertifikatu sa datim aliasom
            PrivateKey privKey = (PrivateKey) keyStore.getKey(alias, keyPass);

            X500Name issuerName = new JcaX509CertificateHolder((X509Certificate) cert).getSubject();
            return new IssuerData(privKey, issuerName);
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException
                | UnrecoverableKeyException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Ucitava sertifikat is KS fajla
     */
    public Certificate readCertificate(String keyStoreFile, String keyStorePass, String alias) {
        try {
            // kreiramo instancu KeyStore
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            // ucitavamo podatke
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());

            if (ks.isKeyEntry(alias)) {
                Certificate cert = ks.getCertificate(alias);
                return cert;
            }
        } catch (KeyStoreException | NoSuchProviderException | NoSuchAlgorithmException
                | CertificateException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Ucitava privatni kljuc is KS fajla
     */
    public PrivateKey readPrivateKey(String keyStoreFile, String keyStorePass, String alias, String pass) {
        try {
            // kreiramo instancu KeyStore
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            // ucitavamo podatke
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());

            if (ks.isKeyEntry(alias)) {
                PrivateKey pk = (PrivateKey) ks.getKey(alias, pass.toCharArray());
                return pk;
            }
        } catch (KeyStoreException | NoSuchAlgorithmException | NoSuchProviderException | CertificateException
                | IOException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<IssuerData> readAllCAIssuers(String keyStoreFile, String keyStorePass, String keyPass) {
        try {
            // kreiramo instancu KeyStore
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");

            // ucitavamo podatke
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());

            //ucitavamo sve aliase koji postoje u keystore
            Enumeration<String> aliasEnumeration = keyStore.aliases();
            List<String> aliases = Collections.list(aliasEnumeration);

            //issuer data od ca
            List<IssuerData> issuerDatas = new ArrayList<>();

            //prolazimo kroz sve sertifikate u keystore i vracamo njihov issuer data
            for (String alias : aliases) {
                X509Certificate certificate = (X509Certificate) readCertificate(keyStoreFile, keyStorePass, alias);
                if (certificate.getBasicConstraints() != -1 || certificate.getKeyUsage()[5]) {
                    issuerDatas.add(readIssuerFromStore(keyStoreFile, alias, keyStorePass.toCharArray(), keyPass.toCharArray()));
                }

            }

            return issuerDatas;

        } catch (KeyStoreException | NoSuchProviderException | IOException | NoSuchAlgorithmException | CertificateException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CertificateViewDTO readAllCertificates(String keyStoreFile, String keyStorePass, String keyPass) {
        try {
            // kreiramo instancu KeyStore
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");

            // ucitavamo podatke
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            ks.load(in, keyStorePass.toCharArray());

            //ucitavamo sve aliase koji postoje u keystore
            Enumeration<String> aliasEnumeration = keyStore.aliases();
            List<String> aliases = Collections.list(aliasEnumeration);

            //prvo pronalazimo root ca
            X509Certificate rootCertificate = (X509Certificate) readCertificate(keyStoreFile, keyStorePass, "root");

            CertificateViewDTO root = new CertificateViewDTO("root", CertificateStatusEnum.ACTIVE);

            ArrayList<CertificateViewDTO> rootChildren =  new ArrayList<>();

            ArrayList<CertificateViewDTO> allEndUsers =  new ArrayList<>();

            //zatim pronalazimo sve intermidiate cas
            for (String alias : aliases) {
                if (!alias.equals("root")) {
                    Certificate certificate = readCertificate(keyStoreFile, keyStorePass, alias);
                    CertificateViewDTO endUser = new CertificateViewDTO(alias);

                    if(!isRevoked(certificate)) {
                        endUser.setStatus(CertificateStatusEnum.ACTIVE);
                    } else {
                        endUser.setStatus(CertificateStatusEnum.REVOKED);
                    }

                    rootChildren.add(endUser);
                }

            }

            root.setChildren(rootChildren);

            return root;

        } catch (KeyStoreException | NoSuchProviderException | IOException | NoSuchAlgorithmException | CertificateException | CRLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isRevoked(Certificate cer) throws IOException, CertificateException, CRLException {

        CertificateFactory factory = CertificateFactory.getInstance("X.509");

        File file = new File("src/main/resources/CRLs.crl");

        byte[] bytes = Files.readAllBytes(file.toPath());
        X509CRL crl = (X509CRL) factory.generateCRL(new ByteArrayInputStream(bytes));

        return crl.isRevoked(cer);
    }
}
