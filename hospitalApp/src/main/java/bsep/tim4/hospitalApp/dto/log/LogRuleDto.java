package bsep.tim4.hospitalApp.dto.log;

import bsep.tim4.hospitalApp.model.LogAlarmType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogRuleDto {

    @Valid
    @NotNull(message = "Rule conditions cannot be empty.")
    private LogRuleConditionListDto ruleConditions;

    @NotNull(message = "Rule alarm type cannot be empty.")
    private LogAlarmType logAlarmType;

    @NotNull(message = "Rule alarm message cannot be empty.")
    private String logAlarmMessage;

    private UUID id;
}