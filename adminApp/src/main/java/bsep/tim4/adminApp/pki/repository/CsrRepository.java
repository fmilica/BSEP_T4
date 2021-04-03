package bsep.tim4.adminApp.pki.repository;

import bsep.tim4.adminApp.pki.model.CSR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CsrRepository extends JpaRepository<CSR, Long> {

    List<CSR> findAllByVerifiedOrderByIdAsc(boolean verified);


}
