package bsep.tim4.adminApp.pki.service;

import bsep.tim4.adminApp.mailSender.MailSenderService;
import bsep.tim4.adminApp.mailSender.VerificationLink;
import bsep.tim4.adminApp.mailSender.VerificationLinkRepository;
import bsep.tim4.adminApp.pki.model.CSR;
import bsep.tim4.adminApp.pki.repository.CsrRepository;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.bouncycastle.asn1.x500.X500Name;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.StringReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.List;

@Service
public class CsrService {

    @Autowired
    private CsrRepository csrRepository;

    @Autowired
    private VerificationLinkRepository verificationLinkRepository;

    @Autowired
    private MailSenderService mailSenderService;

    public List<CSR> findAllByVerified(boolean verified) {
        return csrRepository.findAllByVerified(verified);
    }

    public void saveCsr(String csr) {
        JcaPKCS10CertificationRequest certificationRequest = readCsr(csr);

        try {
            PublicKey publicKey = certificationRequest.getPublicKey();
            X500Name x500Name = certificationRequest.getSubject();

            CSR csrEntity = new CSR(publicKey, x500Name);

            //sacuva csr u bazu
            CSR certificateRequest = csrRepository.save(csrEntity);

            //slanje konfirmacionog linka na tu email adresu
            mailSenderService.sendVerificationLink(certificateRequest);

        } catch (NoSuchAlgorithmException | InvalidKeyException | MessagingException e) {
            e.printStackTrace();
        }
    }


    public JcaPKCS10CertificationRequest readCsr(String csrString) {

        PEMParser pemParser = null;
        try {
            pemParser = new PEMParser(new StringReader(csrString));
            Object parsedObj = pemParser.readObject();
            JcaPKCS10CertificationRequest jcaPKCS10CertificationRequest = new JcaPKCS10CertificationRequest((PKCS10CertificationRequest)parsedObj);
            return jcaPKCS10CertificationRequest;
        } catch (IOException | ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void verifyCsr(String token) {
        VerificationLink verificationLink = verificationLinkRepository.findOneByToken(token);
        /*if (verificationLink == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Verification token does not exist!");
        }*/

        CSR certificateRequest = verificationLink.getCertificateRequest();

        /*if (verificationToken.isExpired()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Verification token has expired! Please register again");
        }*/

        certificateRequest.setVerified(true);
        csrRepository.save(certificateRequest);
    }
}
