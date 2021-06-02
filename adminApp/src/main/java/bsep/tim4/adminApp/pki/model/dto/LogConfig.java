package bsep.tim4.adminApp.pki.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogConfig {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Simulator id cannot be empty.")
    Long simulatorId;

    long readInterval;
    String filter;

    String path;
}
