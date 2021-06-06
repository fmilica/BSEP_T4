package bsep.tim4.hospitalApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogConfig {

    @NotBlank(message = "Log folder path cannot be empty.")
    String path;

    @NotBlank(message = "Log folder type cannot be empty.")
    @Size(min=7, max=9, message = "Log folder type length is inadequate.")
    @Pattern(regexp = "DEFAULT|SIMULATED", message = "Log folder type is not valid.")
    String type;

    @PositiveOrZero(message = "Read interval must be positive or zero.")
    long readInterval;

    String filter;
    Map<String, Long> lastRead;

    public void setPath(String path) {
        this.path = path.replaceAll("\\\\", "/");
        this.path = this.path.replaceAll("//", "/");
    }

    public void setLogFilePointer(String path, Long linePointer) {
        if (lastRead == null) {
            lastRead = new HashMap<>();
        }
        lastRead.put(path, linePointer);
    }

    public Long getLogFilePointer(String path) {
        if (lastRead == null) {
            lastRead = new HashMap<>();
        }
        Long pointer = lastRead.putIfAbsent(path, 0L);
        if (pointer == null) {
            pointer = 0L;
        }
        return pointer;
    }

}
