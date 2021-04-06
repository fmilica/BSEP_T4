package bsep.tim4.adminApp.pki.service;

import bsep.tim4.adminApp.mailSender.csr.CsrSenderService;
import bsep.tim4.adminApp.mailSender.csr.VerificationLink;
import bsep.tim4.adminApp.mailSender.csr.VerificationLinkRepository;
import bsep.tim4.adminApp.pki.exceptions.NonExistentIdException;
import bsep.tim4.adminApp.pki.model.CSR;
import bsep.tim4.adminApp.pki.model.enums.CsrStatus;
import bsep.tim4.adminApp.pki.repository.CsrRepository;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.bouncycastle.asn1.x500.X500Name;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.StringReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

@Service
public class CsrService {

    @Autowired
    private CsrRepository csrRepository;

    @Autowired
    private VerificationLinkRepository verificationLinkRepository;

    @Autowired
    private CsrSenderService csrSenderService;

    public List<CSR> findAllByVerified(boolean verified) {
        return csrRepository.findAllByVerifiedOrderByIdAsc(verified);
    }

    public CSR findById(Long id) throws NonExistentIdException {
        CSR csr =  csrRepository.findById(id).orElse(null);
        if(csr == null) {
            throw new NonExistentIdException("CSR");
        }
        return csr;
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
            csrSenderService.sendVerificationLink(certificateRequest);

        } catch (NoSuchAlgorithmException | InvalidKeyException | MessagingException e) {
            e.printStackTrace();
        }
    }

    public void acceptCsr(Long id) throws NonExistentIdException {
        CSR csr = csrRepository.findById(id).orElse(null);

        if(csr == null) {
            throw new NonExistentIdException("CSR");
        }
        csr.setStatus(CsrStatus.ACCEPTED);

        csrRepository.save(csr);
    }

    public void declineCsr(Long id) throws NonExistentIdException, MessagingException {
        CSR csr = csrRepository.findById(id).orElse(null);

        if(csr == null) {
            throw new NonExistentIdException("CSR");
        }
        csr.setStatus(CsrStatus.DECLINED);

        csrRepository.save(csr);
        //slanje maila za obavestenje o odbijanju csr-a
        csrSenderService.sendCsrDeclined(csr);
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
        //ako je verifikacioni link nevalidan, nista
        if (verificationLink == null) {
            return;
        }

        CSR certificateRequest = verificationLink.getCertificateRequest();

        //TODO uvesti expiration date za verification links
        /*if (verificationToken.isExpired()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Verification token has expired! Please register again");
        }*/

        certificateRequest.setVerified(true);
        certificateRequest.setStatus(CsrStatus.PENDING);
        csrRepository.save(certificateRequest);
    }
}
