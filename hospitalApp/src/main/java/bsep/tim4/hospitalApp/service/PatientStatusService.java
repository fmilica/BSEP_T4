package bsep.tim4.hospitalApp.service;

import bsep.tim4.hospitalApp.exceptions.NonExistentIdException;
import bsep.tim4.hospitalApp.model.PatientEncrypted;
import bsep.tim4.hospitalApp.model.PatientStatus;
import bsep.tim4.hospitalApp.repository.PatientRepository;
import bsep.tim4.hospitalApp.repository.PatientStatusRepository;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientStatusService {

    @Autowired
    private PatientStatusRepository patientStatusRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private KieSessionService kieSessionService;

    public PatientStatus save(PatientStatus patientStatus) throws NonExistentIdException {

        PatientEncrypted patient = patientRepository.findById(patientStatus.getPatientId()).orElse(null);

        if(patient == null) {
            throw new NonExistentIdException("Patient");
        }

        patientStatus = patientStatusRepository.save(patientStatus);
        KieSession kieSession = kieSessionService.getRulesSession();
        kieSession.insert(patientStatus);
        kieSession.fireAllRules();
        return patientStatus;
    }

    public List<PatientStatus> findAll() {
        return patientStatusRepository.findAll();
    }

    public List<PatientStatus> findAllByPatientId(String patientId) throws NonExistentIdException {
        PatientEncrypted patient = patientRepository.findById(patientId).orElse(null);

        if(patient == null) {
            throw new NonExistentIdException("Patient");
        }
        return patientStatusRepository.findAllByPatientId(patientId);
    }
}
