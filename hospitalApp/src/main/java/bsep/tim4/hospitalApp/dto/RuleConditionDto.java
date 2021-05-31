package bsep.tim4.hospitalApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RuleConditionDto {

    @NotBlank(message = "Rule condition type cannot be empty.")
    @Size(min=9, max=18, message = "Inadequate rule condition type length.")
    @Pattern(regexp = "heartRate|lowerBloodPressure|upperBloodPressure|bodyTemperature|respiratoryRate",
            message = "Rule condition type is not valid.")
    private String conditionType;

    @NotBlank(message = "Rule condition operator cannot be empty.")
    @Size(min=1, max=2, message = "Inadequate rule condition operator length.")
    @Pattern(regexp = "==|<=|>=|>|<",
            message = "Rule condition operator is not valid.")
    private String conditionOperator;

    @NotBlank(message = "Rule condition value cannot be empty.")
    private double value;

    @Override
    public String toString() {
        return conditionType + " " + conditionOperator + " " + value;
    }
}
