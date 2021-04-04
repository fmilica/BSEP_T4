package bsep.tim4.adminApp.mailSender.certificate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateLinkRepository extends JpaRepository<CertificateLink, Long> {

    CertificateLink findOneByToken(String token);
}
