package bsep.tim4.hospitalApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RuleConditionListDto {

    @Valid
    @Size(min=1, max=5, message = "Inadequate number of rule conditions.")
    private List<RuleConditionDto> ruleConditions;

    @Override
    public String toString() {
        StringBuilder statementBuilder = new StringBuilder();
        for (RuleConditionDto condition : ruleConditions) {
            statementBuilder.append(condition);
            statementBuilder.append(", ");
        }
        String statement = statementBuilder.toString();
        // remove trailing ', '
        return statement.substring(0, statement.length() - 2);
    }
}
