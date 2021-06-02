package bsep.tim4.hospitalApp.repository;

import bsep.tim4.hospitalApp.model.Log;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogRepository extends MongoRepository<Log, String> {
}
