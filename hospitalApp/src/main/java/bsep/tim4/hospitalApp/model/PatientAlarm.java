package bsep.tim4.hospitalApp.model;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Date;

@Document(collection = "PatientAlarms")
public class PatientAlarm {

    @Id
    private String id;
    private String patientId;
    private String patientStatusId;
    private Date timestamp;
    private String message;

    public PatientAlarm(String patientId, String patientStatusId, Date timestamp, String message) {
        this.patientId = patientId;
        this.patientStatusId = patientStatusId;
        this.timestamp = timestamp;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientStatusId() {
        return patientStatusId;
    }

    public void setPatientStatusId(String patientStatusId) {
        this.patientStatusId = patientStatusId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "PatientAlarm{" +
                "id='" + id + '\'' +
                ", patientId='" + patientId + '\'' +
                ", patientStatusId='" + patientStatusId + '\'' +
                ", timestamp=" + timestamp +
                ", message='" + message + '\'' +
                '}';
    }
}
