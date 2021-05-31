package bsep.tim4.hospitalApp.service;

import bsep.tim4.hospitalApp.dto.RuleDto;
import bsep.tim4.hospitalApp.exceptions.NonExistentIdException;
import bsep.tim4.hospitalApp.model.Patient;
import bsep.tim4.hospitalApp.model.PatientEncrypted;
import bsep.tim4.hospitalApp.repository.PatientRepository;
import bsep.tim4.hospitalApp.util.SignatureUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.drools.template.ObjectDataCompiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    @Value("${template.patient}")
    private String patientTemplate;

    @Value("${rules.path}")
    private String rulesPath;

    @Autowired
    private KeyStoreService keyStoreService;

    @Autowired
    private KieSessionService kieSessionService;

    @Autowired
    PatientRepository patientRepository;

    public Patient findById(String patientId) throws NonExistentIdException {
        PatientEncrypted patientEncrypted = patientRepository.findById(patientId).orElse(null);

        if (patientEncrypted == null) {
            throw new NonExistentIdException("Patient");
        }

        return decryptPatient(patientEncrypted);
    }

    public List<Patient> findAll() {
        List<Patient> patients = new ArrayList<>();
        List<PatientEncrypted> encryptedPatients = patientRepository.findAll();
        for (PatientEncrypted pe: encryptedPatients) {
            patients.add(decryptPatient(pe));
        }
        return patients;
    }

    public PatientEncrypted save(Patient patient) {
        try {
            ObjectMapper om = new ObjectMapper();
            String patientJson = om.writeValueAsString(patient);
            SecretKey symKey = (SecretKey) keyStoreService.getSymKey();
            byte[] encryptedPatient = SignatureUtil.encryptMessage(patientJson, symKey);
            PatientEncrypted patientEncrypted = new PatientEncrypted(encryptedPatient);

            return patientRepository.save(patientEncrypted);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createRule(RuleDto rule) throws NonExistentIdException, IOException, MavenInvocationException {
        PatientEncrypted patientEncrypted =
                patientRepository.findById(rule.getPatientId()).orElse(null);

        if (patientEncrypted == null) {
            throw new NonExistentIdException("Patient");
        }

        InputStream template = new FileInputStream(patientTemplate);
        // Compile template to generate new rules
        List<RuleDto> arguments = new ArrayList<>();
        arguments.add(rule);
        ObjectDataCompiler compiler = new ObjectDataCompiler();
        String drl = compiler.compile(arguments, template);
        // Save rule to drl file
        FileOutputStream drlFile = new FileOutputStream(
                rulesPath + UUID.randomUUID() + ".drl");
        drlFile.write(drl.getBytes());
        drlFile.close();

        kieSessionService.updateRulesJar();
    }

    private Patient decryptPatient(PatientEncrypted patientEncrypted) {
        SecretKey symKey = (SecretKey) keyStoreService.getSymKey();
        String patientJson = SignatureUtil.decryptMessage(patientEncrypted.getPersonalInfo(), symKey);

        try {
            ObjectMapper om = new ObjectMapper();
            Patient patient = om.readValue(patientJson, Patient.class);
            patient.setId(patientEncrypted.getId());
            return patient;
        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
