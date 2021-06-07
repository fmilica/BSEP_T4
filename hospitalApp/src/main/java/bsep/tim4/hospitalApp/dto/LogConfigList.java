package bsep.tim4.hospitalApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogConfigList {

    List<LogConfig> logConfigList;

    public boolean containsFolder(String folderPath) {
        for (LogConfig logConfig : logConfigList) {
            if (logConfig.getPath().equals(folderPath)) {
                return true;
            }
        }
        return false;
    }
}
