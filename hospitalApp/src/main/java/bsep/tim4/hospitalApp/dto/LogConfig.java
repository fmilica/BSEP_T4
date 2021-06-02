package bsep.tim4.hospitalApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogConfig {

    String path;
    Long readInterval;
    String filter;
    Map<String, Long> lastRead;

    public void setLogFilePointer(String path, Long linePointer) {
        lastRead.put(path, linePointer);
    }

    public Long getLogFilePointer(String path) {
        Long pointer = lastRead.putIfAbsent(path, 0L);
        if (pointer == null) {
            pointer = 0L;
        }
        return pointer;
    }
}
