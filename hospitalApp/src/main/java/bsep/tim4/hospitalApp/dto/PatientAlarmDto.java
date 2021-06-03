package bsep.tim4.hospitalApp.dto;

import java.util.Date;

public class PatientAlarmDto {

    private String name;
    private String message;
    private Date timestamp;

    public PatientAlarmDto() {}

    public PatientAlarmDto(String name, String message, Date timestamp) {
        this.name = name;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
