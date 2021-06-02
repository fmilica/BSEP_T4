package bsep.tim4.hospitalApp.controller;

import bsep.tim4.hospitalApp.dto.PatientStatusDto;
import bsep.tim4.hospitalApp.exceptions.NonExistentIdException;
import bsep.tim4.hospitalApp.model.PatientStatus;
import bsep.tim4.hospitalApp.service.PatientService;
import bsep.tim4.hospitalApp.service.PatientStatusService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value="api/patientStatuses")
public class PatientStatusController {

    @Autowired
    PatientStatusService patientStatusService;

    Logger logger = LoggerFactory.getLogger(PatientController.class);

    @GetMapping(value = "/by-page")
    public ResponseEntity<Page<PatientStatusDto>> findAllPatientStatuses(Principal principal, Pageable pageable){
        Page<PatientStatusDto> patientStatuses = null;
        try {
            patientStatuses = patientStatusService.findAll(pageable);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "findAllPatientStatuses", HttpStatus.BAD_REQUEST, "json parse exception"));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        catch (NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
            logger.error(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "findAllPatientStatuses", HttpStatus.INTERNAL_SERVER_ERROR, "decryption exception"));
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NonExistentIdException e) {
            e.printStackTrace();
            logger.warn(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "findAllPatientStatuses", HttpStatus.NOT_FOUND, "non existent patient id"));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                principal.getName(), "findAllPatientStatuses", HttpStatus.OK, "authorized"));
        return new ResponseEntity<>(patientStatuses, HttpStatus.OK);
    }

    @GetMapping(value = "/{patientId}")
    public ResponseEntity<List<PatientStatus>> findAllPatientStatusesForPatient(Principal principal,
                                                                                @PathVariable("patientId") String patientId){
        try {
            List<PatientStatus> patientStatuses = patientStatusService.findAllByPatientId(patientId);
            logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "findAllPatientStatuses", HttpStatus.OK, "authorized"));
            return new ResponseEntity<>(patientStatuses, HttpStatus.OK);
        } catch (NonExistentIdException e) {
            logger.warn(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "findAllPatientStatusesForPatient", HttpStatus.NOT_FOUND, "non existent patient id"));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
