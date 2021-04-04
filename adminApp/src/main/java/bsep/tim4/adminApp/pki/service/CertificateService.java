package bsep.tim4.adminApp.pki.service;

import bsep.tim4.adminApp.pki.keystores.KeyStoreReader;
import bsep.tim4.adminApp.pki.model.IssuerData;
import bsep.tim4.adminApp.pki.model.SubjectData;
import bsep.tim4.adminApp.pki.model.dto.CertificateViewDTO;
import bsep.tim4.adminApp.pki.util.CertificateGenerator;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Calendar;
import java.util.Date;
import java.security.cert.Certificate;
import java.util.List;

@Service
public class CertificateService {

    @Autowired
    private CsrService csrService;

    @Autowired
    KeyStoreService keyStoreService;

    public IssuerData getRootCertificate() {
        IssuerData issuerData = keyStoreService.loadIssuerData("root", "RootPassword");

        return issuerData;
    }

    public List<IssuerData> getAllCAIssuers() {
        return keyStoreService.loadAllCAIssuers();
    }

    public CertificateViewDTO getAllCertificates() {
        return keyStoreService.loadAllCertificates();
    }

    public String generateCertificate(String csr) {
        JcaPKCS10CertificationRequest csrRequest = csrService.readCsr(csr);
        try {
            SubjectData subjectData = generateSubjectData(csrRequest);
            IssuerData issuerData = generateIssuerData();

            //generisanje sertifikata
            Certificate certificate = CertificateGenerator.generateCertificate(subjectData, issuerData);

            //cuvanje sertifikata u keystore (ne koristimo savePrivateKey jer ne znamo privatan kjuc)
            keyStoreService.loadKeyStore();
            RDN emailRDN = subjectData.getX500name().getRDNs(BCStyle.E)[0];
            String alias = emailRDN.getFirst().getValue().toString();
            keyStoreService.saveCertificate(alias, certificate);
            keyStoreService.saveKeyStore();

            // konverzija sertifikata u String radi transporta
            StringWriter sw = new StringWriter();
            JcaPEMWriter writer = new JcaPEMWriter(sw);
            writer.writeObject(certificate);
            writer.close();
            return sw.toString();
        } catch (NoSuchAlgorithmException | InvalidKeyException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public SubjectData generateSubjectData(JcaPKCS10CertificationRequest csrRequest) throws NoSuchAlgorithmException, InvalidKeyException {
        X500Name x500name = csrRequest.getSubject();
        PublicKey publicKey = csrRequest.getPublicKey();
        Date startDate = generateStartDate();
        Date endDate = generateEndDate(startDate);

        //TODO serial number je za sada 2 to cemo menjati
        SubjectData subjectData = new SubjectData(publicKey, x500name, "2", startDate, endDate);

        return subjectData;
    }

    public IssuerData generateIssuerData() {
        //citanje root sertifikata
        //TODO za sada je ovo root, a kada budu bili i intermediate sertifikati ce se menjati ovo
        keyStoreService.loadKeyStore();
        //TODO za sada ovde pise root password kasnije se moze mozda rucno unositi
        IssuerData issuerData = keyStoreService.loadIssuerData("root", "RootPassword");

        return issuerData;
    }

    private Date generateStartDate() {
        Date startDate = new Date();

        return startDate;
    }

    private Date generateEndDate(Date startDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.add(Calendar.YEAR, 1);
        Date endDate = c.getTime();

        return endDate;
    }
}
