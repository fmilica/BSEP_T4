package bsep.tim4.hospitalApp.repository;

import bsep.tim4.hospitalApp.model.LogAlarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogAlarmRepository extends MongoRepository<LogAlarm, String> {

    LogAlarm findFirstByOrderByTimestampDesc();

    Page<LogAlarm> findAllByOrderByTimestampDesc(Pageable pageable);
}
