package bsep.tim4.adminApp.pki.service;

import bsep.tim4.adminApp.pki.exceptions.NonExistentIdException;
import bsep.tim4.adminApp.pki.model.CertificateData;
import bsep.tim4.adminApp.pki.model.dto.CertificateViewDTO;
import bsep.tim4.adminApp.pki.repository.CertificateDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CertificateDataService {

    @Autowired
    private CertificateDataRepository certificateDataRepository;

    public CertificateData findById(Long id) {
        return certificateDataRepository.findById(id).orElse(null);
    }

    public CertificateData findByAlias(String alias) {
        return certificateDataRepository.findByAlias(alias);
    }

    public CertificateData save(CertificateData certData){
        return certificateDataRepository.save(certData);
    }

    public CertificateData revoke(String alias, String revocationReason) throws NonExistentIdException {
        CertificateData certData = findByAlias(alias);
        if (certData == null) {
            throw new NonExistentIdException("certificate");
        }
        certData.setRevoked(true);
        certData.setRevocationDate(new Date());
        certData.setRevocationReason(revocationReason);

        return certificateDataRepository.save(certData);
    }

    public List<CertificateViewDTO> findCertificateView() {
        CertificateData rootCertificate = certificateDataRepository.findByAlias("serbioneer@gmail.com");
        List<CertificateData> children = certificateDataRepository.findAllByAliasNot("serbioneer@gmail.com");
        // kreiranje view-a
        CertificateViewDTO root = new CertificateViewDTO(rootCertificate.getAlias(), rootCertificate.isRevoked());
        List<CertificateViewDTO> childrenView = new ArrayList<>();
        for (CertificateData c : children) {
            childrenView.add(new CertificateViewDTO(c.getAlias(), c.isRevoked()));
        }

        root.setChildren(childrenView);
        List<CertificateViewDTO> certificateViewDTOS = new ArrayList<>();
        certificateViewDTOS.add(root);
        return certificateViewDTOS;
    }
}
