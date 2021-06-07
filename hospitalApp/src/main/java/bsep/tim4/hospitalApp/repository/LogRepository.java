package bsep.tim4.hospitalApp.repository;

import bsep.tim4.hospitalApp.model.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import bsep.tim4.hospitalApp.model.LogLevel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Date;
import java.util.List;

public interface LogRepository extends MongoRepository<Log, String>, QuerydslPredicateExecutor<Log> {

    Page<Log> findAllByOrderByTimestampDesc(Pageable pageable);

    long countByTimestampBetween(Date startDate, Date endDate);
    long countByLevelAndTimestampBetween(LogLevel level, Date startDate, Date endDate);
    List<Log> findByTimestampBetween(Date startDate, Date endDate);
}
