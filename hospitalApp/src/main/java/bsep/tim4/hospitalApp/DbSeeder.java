package bsep.tim4.hospitalApp;

import bsep.tim4.hospitalApp.model.Patient;
import bsep.tim4.hospitalApp.repository.PatientRepository;
import bsep.tim4.hospitalApp.repository.PatientStatusRepository;
import bsep.tim4.hospitalApp.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

@Component
public class DbSeeder implements CommandLineRunner {

    @Autowired
    private PatientStatusRepository patientStatusRepository;

    @Autowired
    private PatientService patientService;

    @Override
    public void run(String... strings) throws Exception {

        //drop all patient statuses
        /*this.patientStatusRepository.deleteAll();

        LocalDate dateOfBirth = LocalDate.parse("1996-05-16");

        Patient patient = new Patient("Petar Petrovic", dateOfBirth, "Novi Sad", "Bulimican");

        patientService.save(patient);*/

    }
}