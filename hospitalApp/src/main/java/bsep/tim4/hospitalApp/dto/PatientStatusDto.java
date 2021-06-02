package bsep.tim4.hospitalApp.dto;

import java.util.Date;

public class PatientStatusDto {

    private String name;

    private int heartRate;

    private String bloodPressure;

    private double bodyTemperature;

    private int respiratoryRate;

    private Date timestamp;

    public PatientStatusDto(String name) {}

    public PatientStatusDto(String name, int heartRate, String bloodPressure, double bodyTemperature, int respiratoryRate, Date timestamp) {
        this.name = name;
        this.heartRate = heartRate;
        this.bloodPressure = bloodPressure;
        this.bodyTemperature = bodyTemperature;
        this.respiratoryRate = respiratoryRate;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
