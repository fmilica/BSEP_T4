package bsep.tim4.hospitalApp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;

@Document(collection = "LogAlarm")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogAlarm {

    @Id
    private String id;

    //private List<Log> logs;
    private Date timestamp;
    private String type;
    private String source;
    private String message;

    public LogAlarm(Date timestamp, String source, String type, String message){
        this.timestamp = timestamp;
        this.source = source;
        this.type = type;
        this.message = message;
    }
}
