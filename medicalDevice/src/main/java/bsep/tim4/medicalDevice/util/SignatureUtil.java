package bsep.tim4.medicalDevice.util;

import bsep.tim4.medicalDevice.keystore.KeyStoreReader;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class SignatureUtil {

    // CMS = Cryptographic Message Syntax
    //https://www.bouncycastle.org/docs/pkixdocs1.5on/org/bouncycastle/cms/CMSSignedDataGenerator.html
    //https://www.bouncycastle.org/docs/pkixdocs1.4/org/bouncycastle/cms/CMSSignedData.html
    //https://www.baeldung.com/java-bouncy-castle
    public static byte[] signMessage(String message, String keyStore, String keyStorePass, String certificateAlias) throws OperatorCreationException, IOException, CMSException, CertificateEncodingException {
        Security.addProvider(new BouncyCastleProvider());

        // pretvaranje poruke u niz bajtova koji se mogu enkodirati
        byte[] messageBytes = message.getBytes();
        CMSTypedData cmsMessage = new CMSProcessableByteArray(messageBytes);

        // dobavljanje privatnog kljuca i sertifikata kojim se potpisuje poruka
        KeyStoreReader keyStoreReader = new KeyStoreReader();
        // sertifikat
        X509Certificate certificate = (X509Certificate) keyStoreReader.readCertificate(keyStore, keyStorePass, certificateAlias);
        // privatni kljuc
        PrivateKey privateKey = keyStoreReader.readPrivateKey(keyStore, keyStorePass, certificateAlias, keyStorePass);

        // dodavanje sertifikata kojim se dekriptuje
        List<X509Certificate> certList = new ArrayList<X509Certificate>();
        certList.add(certificate);

        // kreiranje JcaCertStore
        Store certificateStore = new JcaCertStore(certList);

        // kreiranje signer klase koja koristi privatni kljuc
        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256withRSA").build(privateKey);

        // generator digitalnog potpisa
        CMSSignedDataGenerator cmsGenerator = new CMSSignedDataGenerator();
        cmsGenerator.addSignerInfoGenerator(
                new JcaSignerInfoGeneratorBuilder(
                        new JcaDigestCalculatorProviderBuilder().setProvider("BC").build())
                        .build(contentSigner, certificate));
        cmsGenerator.addCertificates(certificateStore);

        // potpis sa podacima (true -> sadrzi i enkapsulirane podatke)
        CMSSignedData cmsSignedData = cmsGenerator.generate(cmsMessage, true);

        // potpisana poruka u ASN.1 formi
        byte[] signedMessage = cmsSignedData.getEncoded();
        return signedMessage;
    }
}
