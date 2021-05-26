package bsep.tim4.hospitalApp.util;

import bsep.tim4.hospitalApp.keystores.KeyStoreReader;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.Store;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;

public class SignatureUtil {
    // CMS = Cryptographic Message Syntax

    public static int checkTrustedCertificate(byte[] signedMessage, String trustStore, String trustStorePass)
            throws CertificateException, CMSException, IOException {
        // dobavljanje sertifikata iz cms poruke
        X509Certificate signerCertificate = extractCertificate(signedMessage);
        // dobavljanje serijskog broja sertifikata radi provere da li je revoked
        BigInteger serialNumber = signerCertificate.getSerialNumber();
        // instanciranje keystore reader objekta i provera postojanja sertifikata u truststore bolnice
        KeyStoreReader keyStoreReader = new KeyStoreReader();
        boolean existing = keyStoreReader.checkCertificate(trustStore, trustStorePass, signerCertificate);
        if (existing) {
            return serialNumber.intValue();
        } else {
            return -1;
        }
    }

    public static boolean verifySignature(byte[] signedMessage)
            throws IOException, CMSException, OperatorCreationException, CertificateException {
        // dobavljanje CMS podataka iz enkriptovane poruke
        CMSSignedData cmsSignedData = extractCMSData(signedMessage);
        // dobavljanje podataka o potpisivacu iz CMS podataka
        SignerInformation signer = extractSignerInformation(cmsSignedData);
        // dobavljanje sertifikata cijim privatnim kljucem je potpisana poruka
        X509Certificate signerCertificate = extractCertificate(signedMessage);
        // proveravamo da li je potpis dobar
        // potpisali smo privatnim kljucem vezanim za sertifikat
        // proveravamo javnim kljucem koji se nalazi u sertifikatu
        boolean verified = signer.verify(new JcaSimpleSignerInfoVerifierBuilder()
                        .build(signerCertificate.getPublicKey()));
        return verified;
    }

    public static String readSignedData(byte[] signedMessage) throws IOException, CMSException {
        CMSSignedData cmsSignedData = extractCMSData(signedMessage);
        CMSTypedData cmsTypedData = cmsSignedData.getSignedContent();
        String message  = new String((byte[]) cmsTypedData.getContent());

        return message;
    }

    private static X509Certificate extractCertificate(byte[] signedMessage) throws IOException, CMSException, CertificateException {
        // postavljanje BouncyCastle provajdera
        Security.addProvider(new BouncyCastleProvider());

        // dobavljanje CMS podataka iz enkriptovane poruke
        CMSSignedData cmsSignedData = extractCMSData(signedMessage);
        // dobavljanje podataka o potpisivacu iz CMS podataka
        SignerInformation signer = extractSignerInformation(cmsSignedData);

        // dobavljanje sertifikata cijim privatnim kljucem je potpisana poruka
        Store certStore = cmsSignedData.getCertificates();
        Collection<X509CertificateHolder> certCollection
                = certStore.getMatches(signer.getSID());
        X509CertificateHolder certHolder = certCollection.iterator().next();
        X509Certificate signerCertificate = new JcaX509CertificateConverter().setProvider( "BC" ).getCertificate(certHolder);
        return signerCertificate;
    }

    private static SignerInformation extractSignerInformation(CMSSignedData cmsSignedData) throws IOException, CMSException {
        // podaci o potpisivaocima/sertifikatima
        SignerInformationStore signers = cmsSignedData.getSignerInfos();
        // dobavljanje prvog potpisivaca (jedinog u nasem slucaju)
        SignerInformation signer = signers.getSigners().iterator().next();
        return signer;
    }

    private static CMSSignedData extractCMSData(byte[] signedMessage) throws IOException, CMSException {
        // inputStream od byte[]
        ByteArrayInputStream inputStream = new ByteArrayInputStream(signedMessage);
        // ASN.1 inputStream objekat
        ASN1InputStream asnInputStream = new ASN1InputStream(inputStream);
        // CMS potpisani podaci
        CMSSignedData cmsSignedData = new CMSSignedData(
                ContentInfo.getInstance(asnInputStream.readObject()));
        return cmsSignedData;
    }
}
