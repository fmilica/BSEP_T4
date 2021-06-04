package bsep.tim4.adminApp.pki.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class LogConfig {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Simulator id cannot be empty.")
    Long simulatorId;

    String type;

    long readInterval;
    String filter;

    String path;

    public LogConfig() {
        this.type = "SIMULATED";
    }
}
