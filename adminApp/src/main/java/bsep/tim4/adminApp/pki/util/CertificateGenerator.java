package bsep.tim4.adminApp.pki.util;

import bsep.tim4.adminApp.pki.model.IssuerData;
import bsep.tim4.adminApp.pki.model.SubjectData;
import bsep.tim4.adminApp.pki.model.enums.CertificateTemplateEnum;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.math.BigInteger;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class CertificateGenerator {
    public CertificateGenerator() {
    }

    public static X509Certificate generateCertificate(
            SubjectData subjectData, IssuerData issuerData, CertificateTemplateEnum template) {
        Security.addProvider(new BouncyCastleProvider());
        try {
            // Posto klasa za generisanje sertifiakta ne moze da primi direktno privatni kljuc pravi se builder za objekat
            // Ovaj objekat sadrzi privatni kljuc izdavaoca sertifikata i koristiti se za potpisivanje sertifikata
            // Parametar koji se prosledjuje je algoritam koji se koristi za potpisivanje sertifiakta
            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");

            // Takodje se navodi koji provider se koristi, u ovom slucaju Bouncy Castle
            builder = builder.setProvider("BC");

            // Formira se objekat koji ce sadrzati privatni kljuc i koji ce se koristiti za potpisivanje sertifikata
            ContentSigner contentSigner = builder.build(issuerData.getPrivateKey());


            // Postavljaju se podaci za generisanje sertifiakta
            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(
                    issuerData.getX500name(),
                    new BigInteger(subjectData.getSerialNumber()),
                    subjectData.getStartDate(),
                    subjectData.getEndDate(),
                    subjectData.getX500name(),
                    subjectData.getPublicKey());

            switch (template) {
                // prvo true - isCritical -> ako se koristi za drugu nameru, narusava prava
                case CA_CERT:
                    // CA je
                    certGen.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));
                    // samo za CA
                    certGen.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.keyCertSign | KeyUsage.digitalSignature | KeyUsage.dataEncipherment));
                    break;
                case TLS_CLIENT:
                    // nije CA
                    certGen.addExtension(Extension.basicConstraints, true, new BasicConstraints(false));
                    certGen.addExtension(Extension.keyUsage, true,
                            new KeyUsage(KeyUsage.digitalSignature | KeyUsage.dataEncipherment | KeyUsage.keyAgreement));
                    break;
                case TLS_SERVER:
                    // nije CA
                    certGen.addExtension(Extension.basicConstraints, true, new BasicConstraints(false));
                    certGen.addExtension(Extension.keyUsage, true,
                            new KeyUsage(KeyUsage.digitalSignature | KeyUsage.dataEncipherment | KeyUsage.keyEncipherment | KeyUsage.keyAgreement));
                    break;
                case END_USER:
                    // nije CA
                    certGen.addExtension(Extension.basicConstraints, true, new BasicConstraints(false));
                    // https://help.hcltechsw.com/domino/10.0.1/admin/conf_keyusageextensionsandextendedkeyusage_r.html
                    // The digital signature and data encipherment key usage extensions are enabled by default for all Internet certificates.
                    certGen.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.digitalSignature | KeyUsage.dataEncipherment));
                    break;
            }

            // Generise se sertifikat - ovo jeste sertifikat ali mi bas zelimo da vratimo onaj objekat
            X509CertificateHolder certHolder = certGen.build(contentSigner);

            // Builder generise sertifikat kao objekat klase X509CertificateHolder
            // Nakon toga je potrebno certHolder konvertovati u sertifikat, za sta se koristi certConverter
            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");

            // Konvertuje objekat u sertifikat
            return certConverter.getCertificate(certHolder);
        } catch (IllegalArgumentException | IllegalStateException | OperatorCreationException | CertificateException | CertIOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
