package bsep.tim4.adminApp.pki.repository;

import bsep.tim4.adminApp.pki.model.CertificateData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface CertificateDataRepository extends JpaRepository<CertificateData, Long> {

    CertificateData findByAlias(String alias);

    List<CertificateData> findAllByAliasNot(String alias);

    CertificateData findOneById(Long serialNumb);
}
