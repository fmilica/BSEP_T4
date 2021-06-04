package bsep.tim4.hospitalApp.repository;

import bsep.tim4.hospitalApp.model.PatientAlarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PatientAlarmRepository extends MongoRepository<PatientAlarm, String> {

    PatientAlarm findFirstByOrderByTimestampDesc();

    Page<PatientAlarm> findAllByOrderByTimestampDesc(Pageable pageable);

    Page<PatientAlarm> findAllByPatientIdOrderByTimestampDesc(String patientId, Pageable pageable);

}
