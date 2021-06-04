package bsep.tim4.hospitalApp.repository;

import bsep.tim4.hospitalApp.model.PatientEncrypted;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends MongoRepository<PatientEncrypted, String> {

}
