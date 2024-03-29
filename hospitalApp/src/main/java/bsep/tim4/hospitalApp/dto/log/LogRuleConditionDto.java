package bsep.tim4.hospitalApp.dto.log;

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
public class LogRuleConditionDto {

    @NotBlank(message = "Rule condition type cannot be empty.")
    @Size(min=4, max=18, message = "Inadequate rule condition type length.")
    @Pattern(regexp = "(level|message|source|ipAddress)",
            message = "Rule condition type is not valid.")
    private String conditionType;

    @NotBlank(message = "Rule condition operator cannot be empty.")
    @Size(min=1, max=2, message = "Inadequate rule condition operator length.")
    @Pattern(regexp = "(==|!=)",
            message = "Rule condition operator is not valid.")
    private String conditionOperator;

    private String value;

    @Override
    public String toString() {
        String enumValue = value;
        if (conditionType.equals("level")) {
            enumValue = "LogLevel." + value;
        } else {
            if (conditionType.equals("message")) {
                if (conditionOperator.equals("==")) {
                    return conditionType + ".contains(\"" + value + "\")";
                } else {
                    return "!" + conditionType + ".contains(\"" + value + "\")";
                }
            }
            enumValue = '"' + value + '"';
        }
        return conditionType + " " + conditionOperator + " " + enumValue;
    }
}
