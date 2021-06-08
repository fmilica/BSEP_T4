package bsep.tim4.hospitalApp.service;

import bsep.tim4.hospitalApp.dto.FrequentSources;
import bsep.tim4.hospitalApp.dto.ReportDto;
import bsep.tim4.hospitalApp.model.LogAlarmType;
import bsep.tim4.hospitalApp.model.LogLevel;
import bsep.tim4.hospitalApp.repository.LogAlarmRepository;
import bsep.tim4.hospitalApp.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private LogAlarmRepository logAlarmRepository;

    public ReportDto generateReport(ReportDto reportDto) {
        Date startDate = reportDto.getStartDate();
        Date endDate = reportDto.getEndDate();
        int sourcesNumber = reportDto.getSourcesNumber();

        long totalLogs = logRepository.countByTimestampBetween(startDate, endDate);
        reportDto.setTotalLogs(totalLogs);
        for (LogLevel level : LogLevel.values()) {
            long totalLevelLogs = logRepository.countByLevelAndTimestampBetween(level, startDate, endDate);
            reportDto.getLogsByLevel().add(totalLevelLogs);
        }
        long totalAlarms = logAlarmRepository.countByTimestampBetween(startDate, endDate);
        reportDto.setTotalLogAlarms(totalAlarms);
        for (LogAlarmType type : LogAlarmType.values()) {
            long totalTypeAlarms = logAlarmRepository.countByTypeAndTimestampBetween(type, startDate, endDate);
            reportDto.getLogAlarmsByType().add(totalTypeAlarms);
        }

        List<FrequentSources> sources =
                logAlarmRepository.findMostFrequentSources(startDate.toInstant(), endDate.toInstant(), sourcesNumber);
        reportDto.setFrequentSource(sources);

        return reportDto;
    }
}
