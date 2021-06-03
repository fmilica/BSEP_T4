package bsep.tim4.hospitalApp.controller;

import bsep.tim4.hospitalApp.dto.PatientAlarmDto;
import bsep.tim4.hospitalApp.exceptions.NonExistentIdException;
import bsep.tim4.hospitalApp.model.Patient;
import bsep.tim4.hospitalApp.service.PatientAlarmService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;

@RestController
@RequestMapping(value="api/patient-alarms")
public class PatientAlarmController {

    @Autowired
    private PatientAlarmService patientAlarmService;

    Logger logger = LoggerFactory.getLogger(PatientAlarmController.class);

    @GetMapping(value = "/by-page")
    public ResponseEntity<Page<PatientAlarmDto>> findAllPatientAlarms(Principal principal, Pageable pageable) {
        Page<PatientAlarmDto> patientAlarms = null;
        try {
            patientAlarms = patientAlarmService.findAll(pageable);
        } catch (JsonProcessingException e) {
            logger.error(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "findAllPatientAlarms", HttpStatus.BAD_REQUEST, "json parse exception"));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            logger.error(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "findAllPatientAlarms", HttpStatus.INTERNAL_SERVER_ERROR, "decryption exception"));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NonExistentIdException e) {
            logger.warn(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "findAllPatientAlarms", HttpStatus.BAD_REQUEST, "decryption exception"));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                principal.getName(), "findAllPatientAlarms", HttpStatus.OK, "authorized"));
        return new ResponseEntity<>(patientAlarms, HttpStatus.OK);
    }
}
