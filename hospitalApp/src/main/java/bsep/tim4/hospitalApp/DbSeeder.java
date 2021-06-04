package bsep.tim4.hospitalApp;

import bsep.tim4.hospitalApp.model.Patient;
import bsep.tim4.hospitalApp.repository.PatientRepository;
import bsep.tim4.hospitalApp.repository.PatientStatusRepository;
import bsep.tim4.hospitalApp.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DbSeeder implements CommandLineRunner {

    @Autowired
    private PatientStatusRepository patientStatusRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public void run(String... strings) throws Exception {

        //drop all patient statuses
        //this.patientStatusRepository.deleteAll();
        //this.patientRepository.deleteAll();

        /*SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        //Petar
        String petarString = "26-09-1989";
        Date petarDate = formatter.parse(petarString);

        Patient petar = new Patient("Petar Petrovic", petarDate, "Novi Sad", "Bulimican");

        patientService.save(petar);

        String markoString = "18-04-1998";
        Date markoDate = formatter.parse(markoString);

        Patient marko = new Patient("Marko Markovic", markoDate, "Novi Sad", "Dijabeticat");

        patientService.save(marko);

        String klaraString = "18-04-1998";
        Date klaraDate = formatter.parse(klaraString);

        Patient klara = new Patient("Klara Obradovic", klaraDate, "Novi Sad", "Mononukleoza");

        patientService.save(klara);*/

    }
}