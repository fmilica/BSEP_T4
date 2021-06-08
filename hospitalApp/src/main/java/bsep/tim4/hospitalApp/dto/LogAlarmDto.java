package bsep.tim4.hospitalApp.dto;

import java.util.Date;

public class LogAlarmDto {

    private String type;
    private String source;
    private String message;
    private Date timestamp;

    public LogAlarmDto() { }

    public LogAlarmDto(String type, String source, String message, Date timestamp) {
        this.type = type;
        this.source = source;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public Date getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
