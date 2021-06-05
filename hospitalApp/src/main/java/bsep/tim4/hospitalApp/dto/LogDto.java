package bsep.tim4.hospitalApp.dto;

import bsep.tim4.hospitalApp.model.LogLevel;

import java.util.Date;

public class LogDto {

    private LogLevel level;
    private String message;
    private Date timestamp;

    public LogDto() { }

    public LogDto(LogLevel level, String message, Date timestamp) {
        this.level = level;
        this.message = message;
        this.timestamp = timestamp;
    }

    public LogLevel getLevel() {
        return level;
    }
    public void setLevel(LogLevel level) {
        this.level = level;
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
