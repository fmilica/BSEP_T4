package bsep.tim4.adminApp.pki.controller;

import bsep.tim4.adminApp.pki.exceptions.NonExistentIdException;
import bsep.tim4.adminApp.pki.model.Hospital;
import bsep.tim4.adminApp.pki.model.dto.LogConfig;
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
import java.security.Principal;
import java.util.List;

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

    private final String configureLogDirsUri = "/log/add-log-folder";

    Logger logger = LoggerFactory.getLogger(HospitalController.class);

    @GetMapping
    // SUPER ADMIN
    public ResponseEntity<List<Hospital>> findAll() {
        List<Hospital> hospitals = hospitalService.getAll();
        return new ResponseEntity<>(hospitals, HttpStatus.OK);
    }

    @PostMapping(value="/add-simulator/{hospitalId}")
    // SUPER ADMIN
    public ResponseEntity<String> addSimulator(
            //Principal principal,
            //@RequestHeader("Authorization") String token,
            @PathVariable @NotNull(message = "Hospital id cannot be empty")
            @Positive( message = "Hospital id is invalid") Long hospitalId,
            @RequestBody @Valid List<LogConfig> logConfigList) {

        try {
            logConfigList = hospitalService.addSimulator(hospitalId, logConfigList);

            final String fullUri = hospitalUri +  configureLogDirsUri;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            //headers.set("Authorization", token);

            ObjectMapper om = new ObjectMapper();
            String contentJson = om.writeValueAsString(logConfigList);

            HttpEntity<String> request =
                    new HttpEntity<>(contentJson, headers);

            ResponseEntity<Void> responseEntity = restTemplate.
                    postForEntity(fullUri, request, void.class);

            //logger.info(String.format("%s called method %s with status code %s: %s",
            //        principal.getName(), "createCsr", HttpStatus.OK, "CSR created and sent"));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NonExistentIdException | JsonProcessingException e) {
            //logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
            //        principal.getName(), "receiveCsr", HttpStatus.BAD_REQUEST, "io exception"));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
