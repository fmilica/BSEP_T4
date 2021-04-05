package bsep.tim4.adminApp.pki.startup;

import bsep.tim4.adminApp.pki.model.CertificateData;
import bsep.tim4.adminApp.pki.model.IssuerData;
import bsep.tim4.adminApp.pki.model.SubjectData;
import bsep.tim4.adminApp.pki.service.CertificateDataService;
import bsep.tim4.adminApp.pki.service.KeyStoreService;
import bsep.tim4.adminApp.pki.util.CertificateGenerator;
import bsep.tim4.adminApp.pki.util.KeyPairGenerator;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.X509CRLHolder;
import org.bouncycastle.cert.X509v2CRLBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CRLConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CRLException;
import java.security.cert.Certificate;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Component
public class AdminPkiInit implements ApplicationRunner {

    @Autowired
    private KeyStoreService keyStoreService;

    @Autowired
    private CertificateDataService certificateDataService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // ako keystore ne postoji, kreiraj ga
        keyStoreService.loadKeyStore();
        // ako root sertifikat ne postoji, kreiraj ga
        Certificate root = keyStoreService.loadCertificate("root");
        if (root == null) {
            createRootCertificate();
            keyStoreService.saveKeyStore();
        }

    }

    private void createRootCertificate() {
        X500Name rootInfo = generateCertIssAndSubjData();
        Date startDate = generateStartDate();
        Date endDate = generateEndDate(startDate);

        // generisanje entiteta
        CertificateData certData = createRootInfoEntity(startDate, endDate);

        KeyPair keyPair = KeyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        IssuerData issuerData = new IssuerData(privateKey, rootInfo);
        // serial number je ID
        SubjectData subjectData = new SubjectData(publicKey, rootInfo, certData.getId().toString(), startDate, endDate);

        X509Certificate certificate = CertificateGenerator.generateCertificate(subjectData, issuerData, true);
        //poziva se savePrivateKey jer za ovaj sertifikat ima i privatni kljuc, root sertifikat
        //za ostale sertifikate se poziva saveCertificate jer ima samo sertifikat i njegov javni kljuc, a privatni kljuc mu je nedostupan
        keyStoreService.savePrivateKey("serbioneer@gmail.com", privateKey, certificate );
        /*
        try {
            createCRL(privateKey, rootInfo);
        } catch (CRLException e) {
            e.printStackTrace();
        } catch (OperatorCreationException | IOException e) {
            e.printStackTrace();
        }*/
    }

    private X500Name generateCertIssAndSubjData() {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, "adminRoot");
        builder.addRDN(BCStyle.O, "BSEP");
        builder.addRDN(BCStyle.OU, "Tim4");
        builder.addRDN(BCStyle.GIVENNAME, "Admin");
        builder.addRDN(BCStyle.SURNAME, "Root");
        builder.addRDN(BCStyle.C, "RS");
        builder.addRDN(BCStyle.E, "serbioneer@gmail.com");

        return builder.build();
    }

    private Date generateStartDate() {
        Date startDate = new Date();

        return startDate;
    }

    private Date generateEndDate(Date startDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.add(Calendar.YEAR, 10);
        Date endDate = c.getTime();

        return endDate;
    }

    private void createCRL(PrivateKey pk, X500Name issuerName) throws CRLException, OperatorCreationException, IOException {
        X509v2CRLBuilder crlBuilder = new X509v2CRLBuilder(issuerName, new Date());
        crlBuilder.setNextUpdate(new Date(System.currentTimeMillis() + 86400 * 1000));

        JcaContentSignerBuilder contentSignerBuilder = new JcaContentSignerBuilder("SHA256WithRSA");
        contentSignerBuilder.setProvider("BC");

        //issuer pk
        X509CRLHolder crlHolder = crlBuilder.build(contentSignerBuilder.build(pk));
        JcaX509CRLConverter converter = new JcaX509CRLConverter();
        converter.setProvider("BC");

        X509CRL crl = converter.getCRL(crlHolder);

        byte[] bytes = crl.getEncoded();


        OutputStream os = new FileOutputStream("src/main/resources/CRLs.crl");
        os.write(bytes);
        os.close();
    }
    
    private CertificateData createRootInfoEntity(Date startDate, Date endDate) {
        CertificateData certData = new CertificateData("root", "serbioneer@gmail.com", "serbioneer@gmail.com", startDate, endDate);
        certData = certificateDataService.save(certData);
        return certData;
    }
}
