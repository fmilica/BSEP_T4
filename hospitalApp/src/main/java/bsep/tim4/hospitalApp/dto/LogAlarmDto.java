package bsep.tim4.hospitalApp.dto;

import bsep.tim4.hospitalApp.model.LogAlarmType;

import java.util.Date;

public class LogAlarmDto {

    private LogAlarmType type;
    private String message;
    private Date timestamp;

    public LogAlarmDto() { }

    public LogAlarmDto(LogAlarmType type, String message, Date timestamp) {
        this.type = type;
        this.message = message;
        this.timestamp = timestamp;
    }

    public LogAlarmType getType() {
        return type;
    }
    public void setType(LogAlarmType type) {
        this.type = type;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Date getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
