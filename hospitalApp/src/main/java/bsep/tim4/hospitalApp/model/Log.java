package bsep.tim4.hospitalApp.model;

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
    private int status_code;
    private boolean processed;
}
