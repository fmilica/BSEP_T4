package bsep.tim4.hospitalApp.dto;

import bsep.tim4.hospitalApp.model.LogLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogFilterDTO {

    //@Pattern(regexp = "[A-Z.]+", message = "Log level is not valid.")
    String level;

    //@Pattern(regexp = "/(\\d{4}-\\d{2}-\\d{2})[A-Z]+(\\d{2}:\\d{2}:\\d{2}).([0-9+-:]+)/gm", message = "Date not in valid format.")
    Date fromDate;

    //@Pattern(regexp = "/(\\d{4}-\\d{2}-\\d{2})[A-Z]+(\\d{2}:\\d{2}:\\d{2}).([0-9+-:]+)/gm", message = "Date not in valid format.")
    Date toDate;

    String message;

    String source;

    String ipAddress;
}
