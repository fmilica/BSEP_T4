package bsep.tim4.hospitalApp.controller;

import bsep.tim4.hospitalApp.dto.PatientNameDto;
import bsep.tim4.hospitalApp.dto.RuleDto;
import bsep.tim4.hospitalApp.exceptions.NonExistentIdException;
import bsep.tim4.hospitalApp.model.Patient;
import bsep.tim4.hospitalApp.model.PatientStatus;
import bsep.tim4.hospitalApp.service.PatientService;
import bsep.tim4.hospitalApp.service.PatientStatusService;
import bsep.tim4.hospitalApp.util.SignatureUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.operator.OperatorCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.Valid;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.cert.CertificateException;
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
                            logger.warn(String.format("%s called method %s with status code %s: %s",
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
            logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "findById", HttpStatus.OK, "authorized"));
            return new ResponseEntity<>(patient, HttpStatus.OK);

        } catch (NonExistentIdException e) {
            logger.warn(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "findById", HttpStatus.NOT_FOUND, "non existent patient id"));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (JsonProcessingException e) {
            logger.error(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "findById", HttpStatus.BAD_REQUEST, "json parse exception"));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException |  InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            logger.error(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "findById", HttpStatus.INTERNAL_SERVER_ERROR, "decryption exception"));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping()
    public ResponseEntity<List<PatientNameDto>> findAllPatients(Principal principal) {
        List<PatientNameDto> patientNameDtos = null;
        try {
            patientNameDtos = patientService.findAll();
        } catch (JsonProcessingException e) {
            logger.error(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "findAllPatients", HttpStatus.BAD_REQUEST, "json parse exception"));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException | InvalidKeyException e) {
            logger.error(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "findAllPatients", HttpStatus.INTERNAL_SERVER_ERROR, "decryption exception"));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                principal.getName(), "findAllPatients", HttpStatus.OK, "authorized"));
        return new ResponseEntity<>(patientNameDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/by-page")
    public ResponseEntity<Page<Patient>> findAllPatientsByPage(Principal principal, Pageable pageable) {
        Page<Patient> patients = null;
        try {
            patients = patientService.findAll(pageable);
        } catch (JsonProcessingException e) {
            logger.error(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "findAllPatientsByPage", HttpStatus.BAD_REQUEST, "json parse exception"));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException |  InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            logger.error(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "findAllPatientsByPage", HttpStatus.INTERNAL_SERVER_ERROR, "decryption exception"));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                principal.getName(), "findAllPatientsByPage", HttpStatus.OK, "authorized"));
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @PostMapping(value = "/create-rule")
    public ResponseEntity<Void> createRule(Principal principal, @Valid @RequestBody RuleDto ruleDto) {
        try {
            patientService.createRule(ruleDto);
            logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "createRule", HttpStatus.OK, "authorized"));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NonExistentIdException e) {
            e.printStackTrace();
            logger.warn(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "createRule", HttpStatus.BAD_REQUEST, "non existent patient id"));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (IOException | MavenInvocationException e) {
            e.printStackTrace();
            logger.error(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "createRule", HttpStatus.INTERNAL_SERVER_ERROR, "new drl file error"));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
