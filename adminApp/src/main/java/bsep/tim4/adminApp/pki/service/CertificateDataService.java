package bsep.tim4.adminApp.pki.service;

import bsep.tim4.adminApp.pki.model.CertificateData;
import bsep.tim4.adminApp.pki.repository.CertificateDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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

    public CertificateData revoke(Long id, String revocationReason) {
        CertificateData certData = findById(id);
        if (certData == null) {
            //throw new CertificateNotFoundException(id);
            return null;
        }
        certData.setRevoked(true);
        certData.setRevocationDate(new Date());
        certData.setRevocationReason(revocationReason);

        return certificateDataRepository.save(certData);
    }
}
