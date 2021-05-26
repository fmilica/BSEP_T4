package bsep.tim4.hospitalApp.service;

import bsep.tim4.hospitalApp.exceptions.NonExistentIdException;
import bsep.tim4.hospitalApp.model.Patient;
import bsep.tim4.hospitalApp.model.PatientStatus;
import bsep.tim4.hospitalApp.repository.PatientRepository;
import bsep.tim4.hospitalApp.repository.PatientStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientStatusService {

    @Autowired
    private PatientStatusRepository patientStatusRepository;

    @Autowired
    private PatientRepository patientRepository;

    public PatientStatus save(PatientStatus patientStatus) throws NonExistentIdException {

        Patient patient = patientRepository.findById(patientStatus.getPatientId()).orElse(null);

        if(patient == null) {
            throw new NonExistentIdException("Patient");
        }

        return patientStatusRepository.save(patientStatus);
    }
}
