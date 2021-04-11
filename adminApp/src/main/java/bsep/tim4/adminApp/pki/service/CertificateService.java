package bsep.tim4.adminApp.pki.service;

import bsep.tim4.adminApp.mailSender.certificate.CertificateLink;
import bsep.tim4.adminApp.mailSender.certificate.CertificateLinkRepository;
import bsep.tim4.adminApp.mailSender.certificate.CertificateMailSenderService;
import bsep.tim4.adminApp.pki.exceptions.CertificateNotCAException;
import bsep.tim4.adminApp.pki.exceptions.InvalidCertificateException;
import bsep.tim4.adminApp.pki.exceptions.NonExistentIdException;
import bsep.tim4.adminApp.pki.model.CSR;
import bsep.tim4.adminApp.pki.model.CertificateData;
import bsep.tim4.adminApp.pki.model.IssuerData;
import bsep.tim4.adminApp.pki.model.SubjectData;
import bsep.tim4.adminApp.pki.model.dto.CertificateAdditionalInfo;
import bsep.tim4.adminApp.pki.model.dto.CertificateDetailedViewDTO;
import bsep.tim4.adminApp.pki.model.dto.CertificateViewDTO;
import bsep.tim4.adminApp.pki.model.dto.CreateCertificateDTO;
import bsep.tim4.adminApp.pki.model.enums.CertificateTemplateEnum;
import bsep.tim4.adminApp.pki.util.CertificateGenerator;
import bsep.tim4.adminApp.pki.util.KeyPairGenerator;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.security.auth.x500.X500Principal;
import java.io.IOException;
import java.io.StringWriter;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class CertificateService {

    @Autowired
    private CsrService csrService;
    @Autowired
    private CertificateDataService certificateDataService;
    @Autowired
    private CertificateMailSenderService certificateMailSenderService;
    @Autowired
    private CertificateLinkRepository certificateLinkRepository;

    @Autowired
    KeyStoreService keyStoreService;

    public IssuerData getRootCertificate() {
        IssuerData issuerData = keyStoreService.loadIssuerData("serbioneer@gmail.com", "RootPassword");

        return issuerData;
    }

    public IssuerData getByAlias(String alias) {
        IssuerData issuerData = keyStoreService.loadIssuerData(alias, "RootPassword");
        return issuerData;
    }

    public boolean validateCertificate(String alias) {
        // da li je on revoked
        CertificateData certData = certificateDataService.findByAlias(alias);
        if (certData.isRevoked()) {
            return false;
        }
        Certificate[] chain = keyStoreService.readCertificateChain(alias);
        Date now = new Date();
        // za svaki sertifikat u lancu provere
        for (int i = 0; i < chain.length; i++) {
            // kastovanje
            X509Certificate x509Certificate = (X509Certificate) chain[i];
            // validnost datuma
            if (now.before(x509Certificate.getNotBefore()) || now.after(x509Certificate.getNotAfter())) {
                return false;
            }
            // da li je revoked
            CertificateData cData = certificateDataService.findByAlias(alias);
            if (cData.isRevoked()) {
                return false;
            }
            // validnost potpisivanja
            // da li je prethodni u lancu potpisao trenutni
            // https://support.acquia.com/hc/en-us/articles/360004119234-Verifying-the-validity-of-an-SSL-certificate
            try {
                // root potpisuje samog sebe
                if (i == chain.length - 1) {
                    x509Certificate.verify(x509Certificate.getPublicKey());
                } else {
                    X509Certificate issuerCertificate = (X509Certificate) chain[i + 1];
                    x509Certificate.verify(issuerCertificate.getPublicKey());
                }
            } catch (InvalidKeyException | SignatureException e) {
                // greska u potpisu
                return false;
            }
            catch (CertificateException | NoSuchAlgorithmException | NoSuchProviderException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public CertificateDetailedViewDTO getDetails(String alias) {
        X509Certificate certificate = (X509Certificate) keyStoreService.loadCertificate(alias);
        CertificateDetailedViewDTO certDetails = new CertificateDetailedViewDTO();
        // certificate information
        certDetails.setCertAlias(alias);
        certDetails.setSerialNumb(certificate.getSerialNumber());
        certDetails.setVersion(certificate.getVersion());
        certDetails.setValidFrom(certificate.getNotBefore());
        certDetails.setValidUntil(certificate.getNotAfter());
        certDetails.setSignatureAlgorithm(certificate.getSigAlgName());
        certDetails.setPublicKey(certificate.getPublicKey().getEncoded());
        try {
            X500Name x500name = new JcaX509CertificateHolder(certificate).getSubject();
            RDN cn = x500name.getRDNs(BCStyle.CN)[0];
            certDetails.setCommonName(cn.getFirst().getValue().toString());
            X500Principal subject = certificate.getSubjectX500Principal();
            certDetails.setSubjectData(subject.toString());
            X500Principal issuer = certificate.getIssuerX500Principal();
            certDetails.setIssuerData(issuer.toString());
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        return certDetails;
    }


    public List<IssuerData> getAllCAIssuers() {
        return keyStoreService.loadAllCAIssuers();
    }

    /*public CertificateViewDTO getAllCertificates() {
        return keyStoreService.loadAllCertificates();
    }*/

    public String createCertificate(CreateCertificateDTO certDto) throws NonExistentIdException, MessagingException, InvalidCertificateException, CertificateNotCAException {
        // postavljanje statusa na accepted
        csrService.acceptCsr(certDto.getCsrId());
        // dobavljanje csr-a
        CSR csr = csrService.findById(certDto.getCsrId());
        // generisanje sertifikata i cuvanje u bazu i keystore
        CertificateData certData = generateCertificate(
                csr, certDto.getCaAlias(), certDto.getBeginDate(), certDto.getEndDate(), certDto.getAdditionalInfo());
        // slanje sertifikata u mejlu
        certificateMailSenderService.sendCertificateLink(certData);
        return "poslao";
    }

    public CertificateData generateCertificate(
            CSR csr, String caAlias, Date startDate, Date endDate, CertificateAdditionalInfo additionalInfo)
            throws InvalidCertificateException, CertificateNotCAException {
        try {
            // provera validnosti CA sertifikata
            if (!validateCertificate(caAlias)) {
                throw new InvalidCertificateException(caAlias);
            }
            // provera da li je sertifikat CA
            // https://stackoverflow.com/questions/12092457/how-to-check-if-x509certificate-is-ca-certificate
            Certificate[] issuerCertificateChain = keyStoreService.readCertificateChain(caAlias);
            X509Certificate issuer = (X509Certificate) issuerCertificateChain[0];
            try {
                if (issuer.getBasicConstraints() == -1 || !issuer.getKeyUsage()[5]) {
                    throw new CertificateNotCAException(caAlias);
                }
            } catch (NullPointerException e) {
                // u slucaju da nije postavljen basicConstraint ili keyUsage
            }

            // generisanje kljuceva
            KeyPair keyPair = KeyPairGenerator.generateKeyPair();

            SubjectData subjectData = generateSubjectData(csr, startDate, endDate, keyPair);
            IssuerData issuerData = generateIssuerData(caAlias);

            // email je alias za keystore
            RDN emailRDN = subjectData.getX500name().getRDNs(BCStyle.E)[0];
            String alias = emailRDN.getFirst().getValue().toString();

            // generisanje za bazu
            RDN commonNameRDN = subjectData.getX500name().getRDNs(BCStyle.CN)[0];
            String commonName = commonNameRDN.getFirst().getValue().toString();
            CertificateData certData = generateCertificateData(commonName, alias, caAlias, startDate, endDate);
            subjectData.setSerialNumber(certData.getId().toString());

            //generisanje sertifikata
            Certificate certificate = CertificateGenerator.generateCertificate(subjectData, issuerData, additionalInfo);

            // postavljanje lanca sertifikata
            keyStoreService.loadKeyStore();
            // dodaje novi sertifikat na pocetak lanca
            Certificate[] newCertificateChain = createCertificateChain(certificate, issuerCertificateChain);
            /*Certificate[] tempChain = new Certificate[issuerCertificateChain.length + 1];
            tempChain[0] = certificate;
            System.arraycopy(issuerCertificateChain, 0, tempChain, 1, issuerCertificateChain.length);
            */

            // cuvanje sertifikata (niza sertifikata)
            keyStoreService.savePrivateKey(alias, keyPair.getPrivate(), newCertificateChain);
            keyStoreService.saveKeyStore();

            /*//cuvanje sertifikata u keystore (ne koristimo savePrivateKey jer ne znamo privatan kjuc)
            //TODO potpisivanje privatnim kljucem???
            keyStoreService.saveCertificate(alias, certificate);
            keyStoreService.saveKeyStore();*/

            return certData;
            /*// konverzija sertifikata u String radi transporta
            StringWriter sw = new StringWriter();
            JcaPEMWriter writer = new JcaPEMWriter(sw);
            writer.writeObject(certificate);
            writer.close();
            return sw.toString();*/
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SubjectData generateSubjectData(CSR csr, Date startDate, Date endDate, KeyPair keyPair) throws NoSuchAlgorithmException, InvalidKeyException {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, csr.getCommonName());
        builder.addRDN(BCStyle.O, csr.getOrganizationName());
        builder.addRDN(BCStyle.OU, csr.getOrganizationUnit());
        builder.addRDN(BCStyle.GIVENNAME, csr.getName());
        builder.addRDN(BCStyle.SURNAME, csr.getSurname());
        builder.addRDN(BCStyle.C, csr.getCountry());
        builder.addRDN(BCStyle.E, csr.getEmail());
        X500Name x500name = builder.build();

        PublicKey publicKey = keyPair.getPublic();
        //PublicKey publicKey = csr.getPublicKey();

        //serial number je ID iz baze
        SubjectData subjectData = new SubjectData(publicKey, x500name, "-1", startDate, endDate);

        return subjectData;
    }

    public IssuerData generateIssuerData(String caAlias) {
        //citanje root sertifikata
        keyStoreService.loadKeyStore();
        //password za CA sertifikat je isti kao password za keyStore
        // TODO za sada koristimo uvek root (nemamo intermediate)
        IssuerData issuerData = keyStoreService.loadIssuerData(caAlias, "RootPassword");

        return issuerData;
    }

    private CertificateData generateCertificateData(String commonName, String alias, String issuerAlias, Date validFrom, Date validTo) {
        CertificateData certData = new CertificateData(commonName, alias, issuerAlias, validFrom, validTo);
        return certificateDataService.save(certData);
    }

    private Certificate[] createCertificateChain(Certificate newCertificate, Certificate[] issuerChain) {
        Certificate[] newChain = new Certificate[issuerChain.length + 1];
        newChain[0] = newCertificate;

        for(int i = 0; i < issuerChain.length; i++) {
            newChain[i + 1] = issuerChain[i];
        }

        return newChain;
    }

    public String findByToken(String token) {
        CertificateLink certificateLink = certificateLinkRepository.findOneByToken(token);
        //ako je verifikacioni link nevalidan, nista
        if (certificateLink == null) {
            return null;
        }

        CertificateData certData = certificateLink.getCertificateData();

        //TODO uvesti expiration date za verification links
        /*if (verificationToken.isExpired()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Verification token has expired! Please register again");
        }*/

        return certData.getAlias();
    }

    public String getPemCertificate(String alias) throws IOException {
        Certificate certificate = keyStoreService.loadCertificate(alias);
        StringBuilder certificateBuilder = new StringBuilder();
        String pemCertificate = writeCertificateToPEM((X509Certificate) certificate);
        return pemCertificate;
    }

    public String getRootChainPemCertificate(String alias) throws IOException {
        Certificate certificate = keyStoreService.loadCertificate(alias);
        Certificate root = keyStoreService.loadCertificate("serbioneer@gmail.com");
        StringBuilder chainBuilder = new StringBuilder();
        String pemCertificate = writeCertificateToPEM((X509Certificate) certificate);
        chainBuilder.append(pemCertificate);
        String rootCertificate = writeCertificateToPEM((X509Certificate) root);
        chainBuilder.append(rootCertificate);
        return chainBuilder.toString();
    }

    public String getPemCertificateChain(String alias) throws IOException {
        Certificate[] chain = keyStoreService.readCertificateChain(alias);

        StringBuilder chainBuilder = new StringBuilder();
        for (Certificate c : chain) {
            String pemCertificate = writeCertificateToPEM((X509Certificate) c);
            chainBuilder.append(pemCertificate);
        }
        return chainBuilder.toString();
    }

    private String writeCertificateToPEM(X509Certificate certificate) throws IOException {
        StringWriter writer = new StringWriter();
        JcaPEMWriter pemWriter = new JcaPEMWriter(writer);
        pemWriter.writeObject(certificate);
        pemWriter.flush();
        pemWriter.close();
        return writer.toString();
    }

}
