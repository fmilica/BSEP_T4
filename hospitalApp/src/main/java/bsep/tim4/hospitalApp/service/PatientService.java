package bsep.tim4.hospitalApp.service;

import bsep.tim4.hospitalApp.exceptions.NonExistentIdException;
import bsep.tim4.hospitalApp.model.Patient;
import bsep.tim4.hospitalApp.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    PatientRepository patientRepository;

    public Patient findById(String patientId) throws NonExistentIdException {
        Patient patient= patientRepository.findById(patientId).orElse(null);

        if (patient == null) {
            throw new NonExistentIdException("Patient");
        }

        return patient;
    }

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }
}
