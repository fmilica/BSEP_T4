package bsep.tim4.hospitalApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
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
