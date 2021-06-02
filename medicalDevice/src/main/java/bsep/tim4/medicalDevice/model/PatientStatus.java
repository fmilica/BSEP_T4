package bsep.tim4.medicalDevice.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

public class PatientStatus {

    private int heartRate;

    private int lowerBloodPressure;

    private int upperBloodPressure;

    private double bodyTemperature;

    private int respiratoryRate;

    private String patientId;

    private Date timestamp;

    public PatientStatus(int heartRate, int lowerBloodPressure, int upperBloodPressure, double bodyTemperature,
                         int respiratoryRate, String patientId, Date timestamp) {
        this.heartRate = heartRate;
        this.lowerBloodPressure = lowerBloodPressure;
        this.upperBloodPressure = upperBloodPressure;
        this.bodyTemperature = bodyTemperature;
        this.respiratoryRate = respiratoryRate;
        this.patientId = patientId;
        this.timestamp = timestamp;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
