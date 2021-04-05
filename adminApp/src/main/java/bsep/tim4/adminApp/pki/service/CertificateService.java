package bsep.tim4.adminApp.pki.service;

import bsep.tim4.adminApp.mailSender.certificate.CertificateLink;
import bsep.tim4.adminApp.mailSender.certificate.CertificateLinkRepository;
import bsep.tim4.adminApp.mailSender.certificate.CertificateMailSenderService;
import bsep.tim4.adminApp.pki.exceptions.NonExistentIdException;
import bsep.tim4.adminApp.pki.model.CSR;
import bsep.tim4.adminApp.pki.model.CertificateData;
import bsep.tim4.adminApp.pki.model.IssuerData;
import bsep.tim4.adminApp.pki.model.SubjectData;
import bsep.tim4.adminApp.pki.model.dto.CertificateViewDTO;
import bsep.tim4.adminApp.pki.model.dto.CreateCertificateDTO;
import bsep.tim4.adminApp.pki.model.enums.CertificateTemplateEnum;
import bsep.tim4.adminApp.pki.util.CertificateGenerator;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.StringWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
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

    public List<IssuerData> getAllCAIssuers() {
        return keyStoreService.loadAllCAIssuers();
    }

    public CertificateViewDTO getAllCertificates() {
        return keyStoreService.loadAllCertificates();
    }

    public String createCertificate(CreateCertificateDTO certDto) throws NonExistentIdException, MessagingException {
        // postavljanje statusa na accepted
        csrService.acceptCsr(certDto.getCsrId());
        // dobavljanje csr-a
        CSR csr = csrService.findById(certDto.getCsrId());
        // generisanje sertifikata i cuvanje u bazu i keystore
        CertificateData certData = generateCertificate(csr, certDto.getCaAlias(), certDto.getBeginDate(), certDto.getEndDate());
        // slanje sertifikata u mejlu
        certificateMailSenderService.sendCertificateLink(certData);
        return "poslao";
    }

    public CertificateData generateCertificate(CSR csr, String caAlias, Date startDate, Date endDate) {
        try {
            SubjectData subjectData = generateSubjectData(csr, startDate, endDate);
            IssuerData issuerData = generateIssuerData(caAlias);

            // email je alias za bazu
            RDN emailRDN = subjectData.getX500name().getRDNs(BCStyle.E)[0];
            String alias = emailRDN.getFirst().getValue().toString();

            // generisanje za bazu
            RDN commonNameRDN = subjectData.getX500name().getRDNs(BCStyle.CN)[0];
            String commonName = commonNameRDN.getFirst().getValue().toString();
            CertificateData certData = generateCertificateData(commonName, alias, caAlias, startDate, endDate);
            subjectData.setSerialNumber(certData.getId().toString());

            //generisanje sertifikata
            //TODO PROSLEDJIVACEMO TEMPLEJT POSLE - svi su endUser
            Certificate certificate = CertificateGenerator.generateCertificate(subjectData, issuerData, CertificateTemplateEnum.END_USER);

            //cuvanje sertifikata u keystore (ne koristimo savePrivateKey jer ne znamo privatan kjuc)
            keyStoreService.loadKeyStore();
            keyStoreService.saveCertificate(alias, certificate);
            keyStoreService.saveKeyStore();

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

    public SubjectData generateSubjectData(CSR csr, Date startDate, Date endDate) throws NoSuchAlgorithmException, InvalidKeyException {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, csr.getCommonName());
        builder.addRDN(BCStyle.O, csr.getOrganizationName());
        builder.addRDN(BCStyle.OU, csr.getOrganizationUnit());
        builder.addRDN(BCStyle.GIVENNAME, csr.getName());
        builder.addRDN(BCStyle.SURNAME, csr.getSurname());
        builder.addRDN(BCStyle.C, csr.getCountry());
        builder.addRDN(BCStyle.E, csr.getEmail());
        X500Name x500name = builder.build();

        PublicKey publicKey = csr.getPublicKey();

        //serial number je ID iz baze
        SubjectData subjectData = new SubjectData(publicKey, x500name, "-1", startDate, endDate);

        return subjectData;
    }

    public IssuerData generateIssuerData(String caAlias) {
        //citanje root sertifikata
        keyStoreService.loadKeyStore();
        //password za CA sertifikat je isti kao password za keyStore
        IssuerData issuerData = keyStoreService.loadIssuerData(caAlias, "RootPassword");

        return issuerData;
    }

    private CertificateData generateCertificateData(String commonName, String alias, String issuerAlias, Date validFrom, Date validTo) {
        CertificateData certData = new CertificateData(commonName, alias, issuerAlias, validFrom, validTo);
        return certificateDataService.save(certData);
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
