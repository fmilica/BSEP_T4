package bsep.tim4.adminApp.pki.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class LogConfig {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Simulator id cannot be empty.")
    Long simulatorId;

    @PositiveOrZero(message = "Read interval must be positive or zero.")
    long readInterval;

    String filter;

    // sta god da su ove dve vrednosti, ne posmatramo ih, niti ih citamo
    String path;
    String type;

    public LogConfig() {
        this.type = "SIMULATED";
    }
}
