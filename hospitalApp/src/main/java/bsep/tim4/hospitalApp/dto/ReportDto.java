package bsep.tim4.hospitalApp.dto;

import bsep.tim4.hospitalApp.model.LogAlarmType;
import bsep.tim4.hospitalApp.model.LogLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ReportDto {

    @NotNull(message = "Start date cannot be empty.")
    private Date startDate;

    @NotNull(message = "Start date cannot be empty.")
    private Date endDate;

    private Long totalLogs;
    private Long totalLogAlarms;
    private String mostFrequentSource;
    private Map<LogLevel, Long> logsByLevel;
    private Map<LogAlarmType, Long> logAlarmsByType;

    public ReportDto() {
        this.logsByLevel = new HashMap<>();
        this.logAlarmsByType = new HashMap<>();
    }

}
