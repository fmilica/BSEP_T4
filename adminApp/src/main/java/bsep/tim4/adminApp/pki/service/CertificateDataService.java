package bsep.tim4.adminApp.pki.service;

import bsep.tim4.adminApp.mailSender.certificate.CertificateMailSenderService;
import bsep.tim4.adminApp.pki.exceptions.NonExistentIdException;
import bsep.tim4.adminApp.pki.model.CertificateData;
import bsep.tim4.adminApp.pki.model.dto.CertificateViewDTO;
import bsep.tim4.adminApp.pki.repository.CertificateDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.math.BigInteger;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CertificateDataService {

    @Autowired
    private CertificateDataRepository certificateDataRepository;

    @Autowired
    private CertificateMailSenderService certificateMailSenderService;

    public CertificateData findById(Long id) {
        return certificateDataRepository.findById(id).orElse(null);
    }

    public CertificateData findByAlias(String alias) {
        return certificateDataRepository.findByAlias(alias);
    }

    public CertificateData save(CertificateData certData){
        return certificateDataRepository.save(certData);
    }

    public String getDateValidity(Long serialNumb) throws NonExistentIdException {
        CertificateData certData = certificateDataRepository.findOneById(serialNumb);
        if (certData == null) {
            throw new NonExistentIdException("certificate");
        }
        Date now = new Date();
        if (now.after(certData.getValidFrom()) || now.before(certData.getValidTo())) {
            return certData.getAlias();
        }
        return null;
    }

    public CertificateData revoke(String alias, String revocationReason) throws NonExistentIdException, MessagingException {
        CertificateData certData = findByAlias(alias);
        if (certData == null) {
            throw new NonExistentIdException("certificate");
        }
        certData.setRevoked(true);
        certData.setRevocationDate(new Date());
        certData.setRevocationReason(revocationReason);

        certificateMailSenderService.sendRevocationReason(certData);

        return certificateDataRepository.save(certData);
    }

    public List<CertificateViewDTO> findCertificateView() {
        CertificateData rootCertificate = certificateDataRepository.findByAlias("adminroot");
        List<CertificateData> rootChildren = certificateDataRepository.findAllByAliasNotAndParentAlias("adminroot", "adminroot");
        // kreiranje view-a
        CertificateViewDTO root = new CertificateViewDTO(rootCertificate.getAlias(), rootCertificate.getCommonName(), rootCertificate.isRevoked());
        List<CertificateViewDTO> childrenView = new ArrayList<>();
        for (CertificateData c : rootChildren) {
            // dobavljanje dece intermediate CA ako postoje
            List<CertificateData> intermediateChildren = certificateDataRepository.findAllByParentAlias(c.getAlias());
            CertificateViewDTO intermediate = new CertificateViewDTO(c.getAlias(), c.getCommonName(), c.isRevoked());
            if (!intermediateChildren.isEmpty()) {
                List<CertificateViewDTO> intermediateChildrenView = new ArrayList<>();
                for (CertificateData interChild : intermediateChildren) {
                    intermediateChildrenView.add(new CertificateViewDTO(interChild.getAlias(), interChild.getCommonName(), interChild.isRevoked()));
                }
                intermediate.setChildren(intermediateChildrenView);
            }
            childrenView.add(intermediate);
        }

        root.setChildren(childrenView);
        List<CertificateViewDTO> certificateViewDTOS = new ArrayList<>();
        certificateViewDTOS.add(root);
        return certificateViewDTOS;
    }
}
