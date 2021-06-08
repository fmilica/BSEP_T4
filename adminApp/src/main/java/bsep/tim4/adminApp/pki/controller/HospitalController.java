package bsep.tim4.adminApp.pki.controller;

import bsep.tim4.adminApp.pki.exceptions.NonExistentIdException;
import bsep.tim4.adminApp.pki.model.Hospital;
import bsep.tim4.adminApp.pki.model.Simulator;
import bsep.tim4.adminApp.pki.model.dto.HospitalDTO;
import bsep.tim4.adminApp.pki.model.dto.LogConfig;
import bsep.tim4.adminApp.pki.model.mapper.CsrMapper;
import bsep.tim4.adminApp.pki.model.mapper.HospitalMapper;
import bsep.tim4.adminApp.pki.service.HospitalService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.net.ConnectException;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value="api/hospital")
@Validated
public class HospitalController {

    @Value("${hospital.uri}")
    private String hospitalUri;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HospitalService hospitalService;

    private final HospitalMapper hospitalMapper = new HospitalMapper();

    private final String configureLogDirsUri = "/log/add-log-folder";

    Logger logger = LoggerFactory.getLogger(HospitalController.class);

    @GetMapping
    // SUPER ADMIN
    public ResponseEntity<List<HospitalDTO>> findAll(Principal principal) {
        List<Hospital> hospitals = hospitalService.findAll();
        logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                principal.getName(), "findAll", HttpStatus.OK, "authorized"));
        return new ResponseEntity<>(hospitalMapper.toHospitalDtoList(hospitals), HttpStatus.OK);
    }

    @GetMapping(value="/{hospitalId}")
    // SUPER ADMIN
    public ResponseEntity<Set<Simulator>> findAllForHospitals(Principal principal,
            @PathVariable @NotNull(message = "Hospital id cannot be empty")
            @Positive( message = "Hospital id is invalid") Long hospitalId) {
        try {
            Set<Simulator> simulators = hospitalService.findAllForHospital(hospitalId);
            logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "findAllForHospitals", HttpStatus.OK, "authorized"));
            return new ResponseEntity<>(simulators, HttpStatus.OK);
        } catch (NonExistentIdException e) {
            logger.warn(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "findAllForHospitals", HttpStatus.BAD_REQUEST, "nonexistent id"));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping(value="/not-in/{hospitalId}")
    // SUPER ADMIN
    public ResponseEntity<List<Simulator>> findAllNotInHospital(Principal principal,
            @PathVariable @NotNull(message = "Hospital id cannot be empty")
            @Positive( message = "Hospital id is invalid") Long hospitalId) {
        try {
            List<Simulator> simulators = hospitalService.findAllNotInHospital(hospitalId);
            logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "findAllNotInHospital", HttpStatus.OK, "authorized"));
            return new ResponseEntity<>(simulators, HttpStatus.OK);
        } catch (NonExistentIdException e) {
            logger.warn(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "findAllNotInHospital", HttpStatus.BAD_REQUEST, "nonexistent id"));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping(value="/add-simulator/{hospitalId}")
    // SUPER ADMIN
    public ResponseEntity<String> addSimulator(
            Principal principal,
            @RequestHeader("Authorization") String token,
            @PathVariable @NotNull(message = "Hospital id cannot be empty")
            @Positive( message = "Hospital id is invalid") Long hospitalId,
            @RequestBody @Valid List<LogConfig> logConfigList) {

        try {
            logConfigList = hospitalService.addSimulator(hospitalId, logConfigList);

            configureHospitalRequest(token, logConfigList);

            logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "addSimulator", HttpStatus.OK, "authorized"));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NonExistentIdException | JsonProcessingException e) {
            logger.warn(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "addSimulator", HttpStatus.BAD_REQUEST, "nonexistent id"));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping(value="/remove-simulator/{hospitalId}")
    // SUPER ADMIN
    public ResponseEntity<String> removeSimulator(
            Principal principal,
            @RequestHeader("Authorization") String token,
            @PathVariable @NotNull(message = "Hospital id cannot be empty")
            @Positive( message = "Hospital id is invalid") Long hospitalId,
            @RequestBody @Valid List<LogConfig> logConfigList) {

        try {
            logConfigList = hospitalService.removeSimulator(hospitalId, logConfigList);

            configureHospitalRequest(token, logConfigList);

            logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "addSimulator", HttpStatus.OK, "authorized"));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NonExistentIdException | JsonProcessingException e) {
            logger.warn(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "addSimulator", HttpStatus.BAD_REQUEST, "nonexistent id"));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    private void configureHospitalRequest(String token, List<LogConfig> logConfigList) throws JsonProcessingException {
        final String fullUri = hospitalUri +  configureLogDirsUri;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        ObjectMapper om = new ObjectMapper();
        String contentJson = om.writeValueAsString(logConfigList);

        HttpEntity<String> request =
                new HttpEntity<>(contentJson, headers);

        ResponseEntity<Void> responseEntity = restTemplate.
                postForEntity(fullUri, request, void.class);
    }
}
