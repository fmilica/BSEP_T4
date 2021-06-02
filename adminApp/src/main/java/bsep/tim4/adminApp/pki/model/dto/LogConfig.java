package bsep.tim4.adminApp.pki.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogConfig {

    @NotBlank(message = "Log folder path cannot be empty.")
    String path;

    long readInterval;
    String filter;
    Map<String, Long> lastRead;

}
