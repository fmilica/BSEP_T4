package bsep.tim4.hospitalApp.repository;

import bsep.tim4.hospitalApp.model.PatientStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientStatusRepository extends MongoRepository<PatientStatus, String> {
}
