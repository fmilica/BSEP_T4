package bsep.tim4.hospitalApp.repository;

import bsep.tim4.hospitalApp.dto.FrequentSources;
import bsep.tim4.hospitalApp.model.LogAlarm;
import bsep.tim4.hospitalApp.model.LogAlarmType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Repository
public interface LogAlarmRepository extends MongoRepository<LogAlarm, String> {

    LogAlarm findFirstByOrderByTimestampDesc();

    Page<LogAlarm> findAllByOrderByTimestampDesc(Pageable pageable);

    long countByTimestampBetween(Date startDate, Date endDate);
    long countByTypeAndTimestampBetween(LogAlarmType type, Date startDate, Date endDate);

    @Aggregation(pipeline = {
            "{$match: {" +
                    "type: {$nin: [\"BRUTE_FORCE\", \"DOS\"]}, \n" +
                    "timestamp: { " +
                        "$gte: ?0,\n" +
                        "$lte: ?1" +
                        "} " +
                    "}" +
            "}",
            "{$group: { '_id': { $ifNull: [ $source, \"Unknown\" ] }, total: { $sum: 1 } } }",
            "{$sort: {total: -1} }",
            "{$limit: ?2}",
            "{$project: {\n" +
            "        id: 1,\n" +
            "        total: 1\n" +
            "}}"
    })
    List<FrequentSources> findMostFrequentSources(Instant from, Instant to, int sourcesNumber);

    @Aggregation(pipeline = {
            "{$group: { '_id': $type, total: { $sum: 1 } } }",
            "{$project: {\n" +
                    "        id: 1\n" +
                    "}}"
    })
    List<String> findLogAlarmTypes();
}
