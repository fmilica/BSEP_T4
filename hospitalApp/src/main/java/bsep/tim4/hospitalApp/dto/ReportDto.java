package bsep.tim4.hospitalApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ReportDto {

    @NotNull(message = "Start date cannot be empty.")
    private Date startDate;

    @NotNull(message = "Start date cannot be empty.")
    private Date endDate;

    @NotNull(message = "Number of most frequent sources cannot be empty.")
    @Positive(message = "Number of most frequent sources must be positive.")
    private int sourcesNumber;

    private Long totalLogs;
    private Long totalLogAlarms;
    private List<FrequentSources> frequentSource;
    private List<Long> logsByLevel;
    private List<Long> logAlarmsByType;

    public ReportDto() {
        this.logsByLevel = new ArrayList<>();
        this.logAlarmsByType = new ArrayList<>();
    }

}
