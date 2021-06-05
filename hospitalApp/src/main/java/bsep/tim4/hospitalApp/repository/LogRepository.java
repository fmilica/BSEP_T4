package bsep.tim4.hospitalApp.repository;

import bsep.tim4.hospitalApp.model.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogRepository extends MongoRepository<Log, String> {

    Page<Log> findAllByOrderByTimestampDesc(Pageable pageable);
}
