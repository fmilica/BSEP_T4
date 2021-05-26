package bsep.tim4.hospitalApp;

import bsep.tim4.hospitalApp.model.Patient;
import bsep.tim4.hospitalApp.repository.PatientRepository;
import bsep.tim4.hospitalApp.repository.PatientStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DbSeeder implements CommandLineRunner {

    @Autowired
    private PatientStatusRepository patientStatusRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public void run(String... strings) throws Exception {

        //drop all patient statuses
        this.patientStatusRepository.deleteAll();

        //LocalDate dateOfBirth = LocalDate.parse("1998-04-15");

        //Patient patient = new Patient("Marko Markovic", dateOfBirth, "Novi Sad", "Dijabeticar");

        //patientRepository.save(patient);

    }
}
