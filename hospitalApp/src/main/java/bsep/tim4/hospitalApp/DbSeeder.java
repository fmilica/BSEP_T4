package bsep.tim4.hospitalApp;

import bsep.tim4.hospitalApp.model.MaliciousIp;
import bsep.tim4.hospitalApp.model.Patient;
import bsep.tim4.hospitalApp.repository.MaliciousIpRepository;
import bsep.tim4.hospitalApp.repository.PatientRepository;
import bsep.tim4.hospitalApp.repository.PatientStatusRepository;
import bsep.tim4.hospitalApp.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class DbSeeder implements CommandLineRunner {

    @Autowired
    private PatientStatusRepository patientStatusRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MaliciousIpRepository maliciousIpRepository;

    @Override
    public void run(String... strings) throws Exception {

        //drop all patient statuses
        //this.patientStatusRepository.deleteAll();
        //this.patientRepository.deleteAll();

        //Date dateOfBirth = new Date();

        //Patient patient = new Patient("Petar Petrovic", dateOfBirth, "Novi Sad", "Bulimican");

        //patientService.save(patient);

        List<MaliciousIp> maliciousIps = Arrays.asList(new MaliciousIp("151.106.3.179"),
        new MaliciousIp("134.119.192.123"), new MaliciousIp("111.223.219.131"),
        new MaliciousIp("160.20.45.194"), new MaliciousIp("111.223.219.71"),
                new MaliciousIp("160.20.45.162"), new MaliciousIp("103.48.37.62"),
                new MaliciousIp("113.212.142.141"), new MaliciousIp("103.50.86.228"),
                new MaliciousIp("212.129.25.195"), new MaliciousIp("186.22.100.227"),
                new MaliciousIp("203.194.117.10"), new MaliciousIp("160.20.45.237"),
                new MaliciousIp("103.227.11.247"), new MaliciousIp("103.99.0.210"),
                new MaliciousIp("160.20.45.227"), new MaliciousIp("160.20.45.48"),
                new MaliciousIp("203.138.203.198"), new MaliciousIp("160.20.45.144"),
                new MaliciousIp("43.249.241.105"), new MaliciousIp("1.53.4.247"),
                new MaliciousIp("160.20.45.7"), new MaliciousIp("160.20.45.174"),
                new MaliciousIp("103.20.9.55"), new MaliciousIp("122.14.137.73"));

        maliciousIpRepository.saveAll(maliciousIps);

    }
}