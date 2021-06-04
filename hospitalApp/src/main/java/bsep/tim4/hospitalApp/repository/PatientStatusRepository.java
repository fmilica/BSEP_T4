package bsep.tim4.hospitalApp.repository;

import bsep.tim4.hospitalApp.model.PatientStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientStatusRepository extends MongoRepository<PatientStatus, String> {

    Page<PatientStatus> findAllByPatientIdOrderByTimestampDesc(String patientId, Pageable pageable);

    Page<PatientStatus> findAllByOrderByTimestampDesc(Pageable pageable);
}
