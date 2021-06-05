package bsep.tim4.hospitalApp.model;

import com.querydsl.core.annotations.QueryEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Log")
@Role(Role.Type.EVENT)
@Timestamp("timestamp")
@QueryEntity
public class Log {

    @Id
    private String id;

    private Date timestamp;
    private LogLevel level;
    private String message;
    private String source;
    private String type;
    private String ipAddress;
    private String error;
    private String statusCode;
    private boolean processed;

    public Log(Date timestamp, LogLevel level, String message, String source, String type, String ipAddress,
               String error, String statusCode) {
        this.timestamp = timestamp;
        this.level = level;
        this.message = message;
        this.source = source;
        this.type = type;
        this.ipAddress = ipAddress;
        this.error = error;
        this.statusCode = statusCode;
        this.processed = false;
    }
}
