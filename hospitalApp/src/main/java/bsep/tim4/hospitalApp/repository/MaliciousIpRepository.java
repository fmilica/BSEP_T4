package bsep.tim4.hospitalApp.repository;

import bsep.tim4.hospitalApp.model.MaliciousIp;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MaliciousIpRepository extends MongoRepository<MaliciousIp, String> {

}
