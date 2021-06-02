package bsep.tim4.adminApp.pki.repository;

import bsep.tim4.adminApp.pki.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
}
