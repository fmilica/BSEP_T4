package bsep.tim4.hospitalApp.repository;

import bsep.tim4.hospitalApp.model.PatientAlarm;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PatientAlarmRepository extends MongoRepository<PatientAlarm, String> {
}
