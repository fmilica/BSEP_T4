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

    String level;

    Date fromDate;

    Date toDate;

    String message;

    String source;

    String ipAddress;
}
