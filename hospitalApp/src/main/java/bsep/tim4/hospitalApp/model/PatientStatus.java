package bsep.tim4.hospitalApp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;

@Document(collection = "PatientStatuses")
public class PatientStatus {

    @Id
    private String id;

    private int heartRate;

    private int lowerBloodPressure;

    private int upperBloodPressure;

    private double bodyTemperature;

    private int respiratoryRate;

    private String patientId;

    private Timestamp timestamp;

    public PatientStatus() {}

    public PatientStatus(int heartRate, int lowerBloodPressure, int upperBloodPressure, double bodyTemperature,
                         int respiratoryRate, String patientId, Timestamp timestamp) {
        this.heartRate = heartRate;
        this.lowerBloodPressure = lowerBloodPressure;
        this.upperBloodPressure = upperBloodPressure;
        this.bodyTemperature = bodyTemperature;
        this.respiratoryRate = respiratoryRate;
        this.patientId = patientId;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public int getLowerBloodPressure() {
        return lowerBloodPressure;
    }

    public void setLowerBloodPressure(int lowerBloodPressure) {
        this.lowerBloodPressure = lowerBloodPressure;
    }

    public int getUpperBloodPressure() {
        return upperBloodPressure;
    }

    public void setUpperBloodPressure(int upperBloodPressure) {
        this.upperBloodPressure = upperBloodPressure;
    }

    public double getBodyTemperature() {
        return bodyTemperature;
    }

    public void setBodyTemperature(double bodyTemperature) {
        this.bodyTemperature = bodyTemperature;
    }

    public int getRespiratoryRate() {
        return respiratoryRate;
    }

    public void setRespiratoryRate(int respiratoryRate) {
        this.respiratoryRate = respiratoryRate;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "PatientStatus{" +
                "heartRate=" + heartRate +
                ", lowerBloodPressure=" + lowerBloodPressure +
                ", upperBloodPressure=" + upperBloodPressure +
                ", bodyTemperature=" + bodyTemperature +
                ", respiratoryRate=" + respiratoryRate +
                ", patientId='" + patientId + '\'' +
                '}';
    }
}
