package bsep.tim4.adminApp.mailSender.csr;

import bsep.tim4.adminApp.mailSender.csr.VerificationLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationLinkRepository extends JpaRepository<VerificationLink, Long> {

    VerificationLink findOneByToken(String token);
}
