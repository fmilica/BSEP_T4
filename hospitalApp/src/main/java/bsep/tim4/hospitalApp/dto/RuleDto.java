package bsep.tim4.hospitalApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RuleDto {

    @NotBlank(message = "Rule condition type cannot be empty.")
    @Size(min=24, max=24, message = "Inadequate patient id length.")
    @Pattern(regexp = "^[a-f0-9]{24}$", message = "Patient id is not valid.")
    private String patientId;

    @Valid
    @NotNull(message = "Rule conditions cannot be empty.")
    private RuleConditionListDto ruleConditions;

    private UUID id;
}