package bsep.tim4.medicalDevice.service;

import bsep.tim4.medicalDevice.model.PatientStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

@Service
public class PatientStatusService {

    private List<String> patientIds = Arrays.asList("60ae697db04b8d0648494432");
    private final DecimalFormat df = new DecimalFormat("#.##");

    public PatientStatus generatePatientStatus() {
        final int ran = (int) Math.floor(Math.random()*100);
        if (ran < 60)  {
            System.out.println("Im normal");
            return generateNormalPatientStatus();
        } else if(60 < ran && ran <=80) {
            System.out.println("Something wrong");
            return generateCriticalPatientStatus();
        }
        else if(80 < ran && ran <=90) {
            System.out.println("Code blue");
            return generateCodeBlue();
        }
        else if(90 < ran && ran <=95) {
            System.out.println("Panic attack");
            return generatePanicAttack();
        }else {
            System.out.println("On the way to morgue");
            return generateCardiacArrest();
        }
    }

    public PatientStatus generateNormalPatientStatus() {

        int heartRate = (int)Math.floor(Math.random()*(80-60+1)+60);
        int lowerBloodPressure = (int)Math.floor(Math.random()*(80-60+1)+60);
        int upperBloodPressure = (int)Math.floor(Math.random()*(130-100+1)+100);
        double bodyTemperature =  Double.valueOf(df.format(Math.random()*(37.0-36.0+0.1)+36.0));
        int respiratoryRate = (int)Math.floor(Math.random()*(18-12+1)+12);
        int index = (int)Math.floor(Math.random()*(patientIds.size()));
        String patientId = patientIds.get(index);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        PatientStatus patientStatus = new PatientStatus(heartRate, lowerBloodPressure, upperBloodPressure, bodyTemperature,
                respiratoryRate, patientId, timestamp);

        return patientStatus;
    }

    public PatientStatus generateCriticalPatientStatus() {
        int heartRate = generateHeartRate();
        int lowerBloodPressure = generateLowerBloodPressure();
        int upperBloodPressure = generateUpperBloodPressure();
        double bodyTemperature =  generateTemperature();
        int respiratoryRate = generateRespiratoryRate();
        int index = (int)Math.floor(Math.random()*(patientIds.size()));
        String patientId = patientIds.get(index);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        PatientStatus patientStatus = new PatientStatus(heartRate, lowerBloodPressure, upperBloodPressure, bodyTemperature,
                respiratoryRate, patientId, timestamp);

        return patientStatus;
    }

    public int generateHeartRate() {
        final int ran = (int) Math.floor(Math.random()*100);
        //normal heart rate
        if (ran <= 60)  {
            return (int)Math.floor(Math.random()*(80-60+1)+60);
        }
        //low heart rate
        else if(60 < ran && ran <=80) {
            return (int)Math.floor(Math.random()*(60-0+1)+0);
        }
        //high heart rate
        else {
            return (int)Math.floor(Math.random()*(200-80+1)+80);
        }
    }

    public int generateLowerBloodPressure() {
        final int ran = (int) Math.floor(Math.random()*100);
        //normal lower blood pressure
        if (ran <= 60)  {
            return (int)Math.floor(Math.random()*(80-60+1)+60);
        }
        //low lower blood pressure
        else if(60 < ran && ran <=80) {
            return (int)Math.floor(Math.random()*(60-0+1)+0);
        }
        //high lower blood pressure
        else {
            return (int)Math.floor(Math.random()*(140-80+1)+80);
        }
    }

    public int generateUpperBloodPressure() {
        final int ran = (int) Math.floor(Math.random()*100);
        //normal upper blood pressure
        if (ran <= 60)  {
            return (int)Math.floor(Math.random()*(130-100+1)+100);
        }
        //low upper blood pressure
        else if(60 < ran && ran <=80) {
            return (int)Math.floor(Math.random()*(100-0+1)+0);
        }
        //high lower blood pressure
        else {
            return (int)Math.floor(Math.random()*(200-130+1)+130);
        }
    }

    public double generateTemperature() {
        final int ran = (int) Math.floor(Math.random()*100);
        Double.valueOf(df.format(Math.random()*(37.0-36.0+0.1)+36.0));
        //normal temperature
        if (ran <= 60)  {
            return Double.valueOf(df.format(Math.random()*(37.0-36.0+0.1)+36.0));
        }
        //low temperature
        else if(60 < ran && ran <=80) {
            return Double.valueOf(df.format(Math.random()*(36.0+0.1)+0.0));
        }
        //high temperature
        else {
            return Double.valueOf(df.format(Math.random()*(42.0-37.0+0.1)+37.0));
        }
    }

    public int generateRespiratoryRate() {
        final int ran = (int) Math.floor(Math.random()*100);
        //normal respiratory rate
        if (ran <= 60)  {
            return (int)Math.floor(Math.random()*(18-12+1)+12);
        }
        //low respiratory rate
        else if(60 < ran && ran <=80) {
            return (int)Math.floor(Math.random()*(12-0+1)+0);
        }
        //high respiratory rate
        else {
            return (int)Math.floor(Math.random()*(60-18+1)+18);
        }
    }

    public PatientStatus generateCardiacArrest() {

        int heartRate = (int)Math.floor(Math.random()*(200-140)+140);
        int lowerBloodPressure = (int)Math.floor(Math.random()*(130-100)+100);
        int upperBloodPressure = (int)Math.floor(Math.random()*(200-150)+150);
        double bodyTemperature = Double.valueOf(df.format( Math.random()*(40.0-38.0+0.1)+38.0));
        int respiratoryRate = (int)Math.floor(Math.random()*(5-3)+3);
        int index = (int)Math.floor(Math.random()*(patientIds.size()));
        String patientId = patientIds.get(index);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        PatientStatus patientStatus = new PatientStatus(heartRate, lowerBloodPressure, upperBloodPressure, bodyTemperature,
                respiratoryRate, patientId, timestamp);

        return patientStatus;
    }

    public PatientStatus generateCodeBlue() {

        int heartRate = (int)Math.floor(Math.random()*(10-0+1)+0);
        int lowerBloodPressure = (int)Math.floor(Math.random()*(80-60+1)+60);
        int upperBloodPressure = (int)Math.floor(Math.random()*(130-100+1)+100);
        double bodyTemperature =  Double.valueOf(df.format(Math.random()*(37.0-36.0+0.1)+36.0));
        int respiratoryRate = (int)Math.floor(Math.random()*(5-0+1)+0);
        int index = (int)Math.floor(Math.random()*(patientIds.size()));
        String patientId = patientIds.get(index);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        PatientStatus patientStatus = new PatientStatus(heartRate, lowerBloodPressure, upperBloodPressure, bodyTemperature,
                respiratoryRate, patientId, timestamp);

        return patientStatus;
    }

    public PatientStatus generatePanicAttack() {

        int heartRate = (int)Math.floor(Math.random()*(200-90+1)+90);
        int lowerBloodPressure = (int)Math.floor(Math.random()*(80-60+1)+60);
        int upperBloodPressure = (int)Math.floor(Math.random()*(130-100+1)+100);
        double bodyTemperature =  Double.valueOf(df.format(Math.random()*(37.0-36.0+0.1)+36.0));
        int respiratoryRate = (int)Math.floor(Math.random()*(60-20+1)+20);
        int index = (int)Math.floor(Math.random()*(patientIds.size()));
        String patientId = patientIds.get(index);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        PatientStatus patientStatus = new PatientStatus(heartRate, lowerBloodPressure, upperBloodPressure, bodyTemperature,
                respiratoryRate, patientId, timestamp);

        return patientStatus;
    }

}
