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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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

    public Patient findById(String patientId) throws NonExistentIdException, JsonProcessingException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException {
        PatientEncrypted patientEncrypted = patientRepository.findById(patientId).orElse(null);

        if (patientEncrypted == null) {
            throw new NonExistentIdException("Patient");
        }

        return decryptPatient(patientEncrypted);
    }

    public Page<Patient> findAll(Pageable pageable) throws JsonProcessingException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException {
        List<Patient> patients = new ArrayList<>();
        Page<PatientEncrypted> page = patientRepository.findAll(pageable);
        for (PatientEncrypted pe: page.toList()) {
            patients.add(decryptPatient(pe));
        }
        return new PageImpl<>(patients, page.getPageable(), page.getTotalElements());
    }

    public PatientEncrypted save(Patient patient) throws JsonProcessingException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {

        ObjectMapper om = new ObjectMapper();
        String patientJson = om.writeValueAsString(patient);
        SecretKey symKey = (SecretKey) keyStoreService.getSymKey();
        byte[] encryptedPatient = SignatureUtil.encryptMessage(patientJson, symKey);
        PatientEncrypted patientEncrypted = new PatientEncrypted(encryptedPatient);

        return patientRepository.save(patientEncrypted);
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

     Patient decryptPatient(PatientEncrypted patientEncrypted) throws JsonProcessingException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        SecretKey symKey = (SecretKey) keyStoreService.getSymKey();
        String patientJson = SignatureUtil.decryptMessage(patientEncrypted.getPersonalInfo(), symKey);

        ObjectMapper om = new ObjectMapper();
        Patient patient = om.readValue(patientJson, Patient.class);
        patient.setId(patientEncrypted.getId());
        return patient;
    }

}
