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
        } else if(60 < ran && ran <=90) {
            System.out.println("Something wrong");
            return generateCriticalPatientStatus();
        } else {
            System.out.println("On the way to morgue");
            return generateCardiacArrest();
        }
    }

    public PatientStatus generateNormalPatientStatus() {

        int heartRate = (int)Math.floor(Math.random()*(80-60+1)+60);
        int lowerBloodPressure = (int)Math.floor(Math.random()*(80-60+1)+60);
        int upperBloodPressure = (int)Math.floor(Math.random()*(120-90+1)+90);
        double bodyTemperature =  Double.valueOf(df.format(Math.random()*(37.2-35.7+0.1)+35.7));
        int respiratoryRate = (int)Math.floor(Math.random()*(16-12+1)+12);
        int index = (int)Math.floor(Math.random()*(patientIds.size()));
        String patientId = patientIds.get(index);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        PatientStatus patientStatus = new PatientStatus(heartRate, lowerBloodPressure, upperBloodPressure, bodyTemperature,
                respiratoryRate, patientId, timestamp);

        return patientStatus;
    }

    public PatientStatus generateCriticalPatientStatus() {

        int heartRate = (int)Math.floor(Math.random()*(100-60+1)+60);
        int lowerBloodPressure = (int)Math.floor(Math.random()*(80-60+1)+60);
        int upperBloodPressure = (int)Math.floor(Math.random()*(120-90+1)+90);
        double bodyTemperature =  Double.valueOf(df.format(Math.random()*(37.2-35.7+0.1)+35.7));
        int respiratoryRate = (int)Math.floor(Math.random()*(16-12+1)+12);
        int index = (int)Math.floor(Math.random()*(patientIds.size()));
        String patientId = patientIds.get(index);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        final int ran = (int) Math.floor(Math.random()*100);
        if (ran <= 20)  {
            heartRate = (int)Math.floor(Math.random()*(300-120+1)+120);
        } else if(20 < ran && ran <=40) {
            heartRate = (int)Math.floor(Math.random()*(40+1));
        } else if(40 < ran && ran <=60) {
            lowerBloodPressure = (int)Math.floor(Math.random()*(120-90+1)+90);
            upperBloodPressure = (int)Math.floor(Math.random()*(200-130+1)+130);
        } else if(60 < ran && ran <=80) {
            bodyTemperature =  Math.random()*(42-37.2+0.1)+37.2;
        } else if(80 < ran && ran <=100) {
            respiratoryRate = (int)Math.floor(Math.random()*(40-20+1)+20);
        }

        PatientStatus patientStatus = new PatientStatus(heartRate, lowerBloodPressure, upperBloodPressure, bodyTemperature,
                respiratoryRate, patientId, timestamp);

        return patientStatus;
    }

   /* public PatientStatus generateCriticalPatientStatus() {

    }

    public int generateHeartRate() {
        final int ran = (int) Math.floor(Math.random()*100);
        //high heart rate
        if (ran <= 20)  {
            return (int)Math.floor(Math.random()*(-120+1)+120);
        }
        //low heart rate
        else if(20 < ran && ran <=40) {
            return (int)Math.floor(Math.random()*(60-0+1)+0);
        }
        //normal heart rate
        else if(40 < ran && ran <=60) {
            return (int)Math.floor(Math.random()*(120-90+1)+90);
        }
    }*/

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

}
