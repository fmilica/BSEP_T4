package bsep.tim4.hospitalApp.controller;

import bsep.tim4.hospitalApp.dto.PatientDTO;
import bsep.tim4.hospitalApp.exceptions.NonExistentIdException;
import bsep.tim4.hospitalApp.model.Patient;
import bsep.tim4.hospitalApp.model.PatientStatus;
import bsep.tim4.hospitalApp.service.PatientService;
import bsep.tim4.hospitalApp.service.PatientStatusService;
import bsep.tim4.hospitalApp.util.SignatureUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.operator.OperatorCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.Principal;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value="api/patients")
public class PatientController {

    @Autowired
    PatientStatusService patientStatusService;

    @Autowired
    PatientService patientService;

    @Autowired
    RestTemplate restTemplate;

    @Value("${trust.store}")
    private String trustStore;

    @Value("${trust.store.password}")
    private String trustStorePass;

    @Value("${super-admin.uri}")
    private String adminApplicationUri;

    private final String validateCertificateUrl = "/certificate/validate/";

    Logger logger = LoggerFactory.getLogger(PatientController.class);

    @PostMapping(value="/status")
    // UNAUTHORIZED
    // getting data from MedicalDevice application
    public ResponseEntity<Void> receivePatientStatus(@RequestBody byte[] signedMessage) {
        try {
            int serialNumber = SignatureUtil.checkTrustedCertificate(signedMessage, trustStore, trustStorePass);
            if (serialNumber != -1) {
                final String validateCertificateFullUrl = adminApplicationUri +  validateCertificateUrl + serialNumber;
                // provera da li je sertifikat revoked
                ResponseEntity<Boolean> responseEntityStr = restTemplate.
                        getForEntity(validateCertificateFullUrl, Boolean.class);
                boolean valid = responseEntityStr.getBody();
                if (valid) {
                    if (SignatureUtil.verifySignature(signedMessage)) {
                        String message = SignatureUtil.readSignedData(signedMessage);
                        ObjectMapper objectMapper = new ObjectMapper();
                        PatientStatus patientStatus = null;
                        try {
                            patientStatus = objectMapper.readValue(message, PatientStatus.class);
                        }catch (JsonMappingException | JsonParseException e) {
                            logger.error(String.format("%s called method %s with status code %s: %s",
                                    "Medical device", "receivePatientStatus", HttpStatus.BAD_REQUEST, "deserialization failure"));
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
                        }
                        try{
                            logger.info(String.format("%s called method %s with status code %s: %s",
                                    "Medical device", "receivePatientStatus", HttpStatus.OK, "patient status received"));
                            patientStatusService.save(patientStatus);
                            return new ResponseEntity<Void>(HttpStatus.OK);
                        } catch (NonExistentIdException e) {
                            logger.error(String.format("%s called method %s with status code %s: %s",
                                    "Medical device", "receivePatientStatus", HttpStatus.NOT_FOUND, "non existent patient id"));
                            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
                        }
                    }
                    logger.error(String.format("%s called method %s with status code %s: %s",
                            "Medical device", "receivePatientStatus", HttpStatus.BAD_REQUEST, "revoked certificate"));
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                }
                logger.error(String.format("%s called method %s with status code %s: %s",
                        "Medical device", "receivePatientStatus", HttpStatus.BAD_REQUEST, "invalid signature"));
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            logger.error(String.format("%s called method %s with status code %s: %s",
                    "Medical device", "receivePatientStatus", HttpStatus.BAD_REQUEST, "untrusted certificate"));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (CertificateException | CMSException | IOException | OperatorCreationException | NullPointerException e) {
            logger.error(String.format("%s called method %s with status code %s: %s",
                    "Medical device", "receivePatientStatus", HttpStatus.BAD_REQUEST, "untrusted certificate"));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{patientId}")
    public ResponseEntity<Patient> findById(Principal principal, @PathVariable("patientId") String patientId) {
        try {
            Patient patient = patientService.findById(patientId);
            logger.info(String.format("%s called method %s with status code %s: %s",
                    principal.getName(), "findById", HttpStatus.OK, "authorized"));
            return new ResponseEntity<>(patient, HttpStatus.OK);

        } catch (NonExistentIdException e) {
            logger.error(String.format("%s called method %s with status code %s: %s",
                    principal.getName(), "findById", HttpStatus.NOT_FOUND, "non existent patient id"));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<PatientDTO>> findAllPatients(Principal principal) {
        List<Patient> patients = patientService.findAll();

        List<PatientDTO> patientDTOS = new ArrayList<>();
        for(Patient patient : patients) {
            patientDTOS.add(new PatientDTO(patient.getId(), patient.getName()));
        }

        logger.info(String.format("%s called method %s with status code %s: %s",
                principal.getName(), "findAllPatients", HttpStatus.OK, "authorized"));
        return new ResponseEntity<>(patientDTOS, HttpStatus.OK);
    }

    @GetMapping(value = "/patientsStatuses")
    public ResponseEntity<List<PatientStatus>> findAllPatientStatuses(Principal principal){
        List<PatientStatus> patientStatuses = patientStatusService.findAll();

        logger.info(String.format("%s called method %s with status code %s: %s",
                principal.getName(), "findAllPatientStatuses", HttpStatus.OK, "authorized"));
        return new ResponseEntity<>(patientStatuses, HttpStatus.OK);
    }

    @GetMapping(value = "/patientsStatuses/{patientId}")
    public ResponseEntity<List<PatientStatus>> findAllPatientStatusesForPatient(Principal principal,
                                            @PathVariable("patientId") String patientId){
        try {
            List<PatientStatus> patientStatuses = patientStatusService.findAllByPatientId(patientId);
            logger.info(String.format("%s called method %s with status code %s: %s",
                    principal.getName(), "findAllPatientStatuses", HttpStatus.OK, "authorized"));
            return new ResponseEntity<>(patientStatuses, HttpStatus.OK);
        } catch (NonExistentIdException e) {
            logger.error(String.format("%s called method %s with status code %s: %s",
                    principal.getName(), "findAllPatientStatusesForPatient", HttpStatus.NOT_FOUND, "non existent patient id"));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
