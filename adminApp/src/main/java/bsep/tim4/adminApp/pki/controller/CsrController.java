package bsep.tim4.adminApp.pki.controller;

import bsep.tim4.adminApp.pki.exceptions.NonExistentIdException;
import bsep.tim4.adminApp.pki.model.CSR;
import bsep.tim4.adminApp.pki.model.dto.CsrDTO;
import bsep.tim4.adminApp.pki.model.mapper.CsrMapper;
import bsep.tim4.adminApp.pki.service.CertificateService;
import bsep.tim4.adminApp.pki.service.CsrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.validation.constraints.*;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value="api/csr")
@Validated
public class CsrController {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private CsrService csrService;

    private final CsrMapper csrMapper = new CsrMapper();

    Logger logger = LoggerFactory.getLogger(CsrController.class);

    @PostMapping(value="/receive")
    // ADMIN
    public ResponseEntity<String> receiveCsr(Principal principal, @RequestBody @NotBlank(message = "Csr cannot be empty") String csr) {
        //Primljen csr se skladisti u bazu i na taj email se salje konfirmacioni link
        try {
            csrService.saveCsr(csr);
        } catch (NoSuchAlgorithmException | InvalidKeyException | MessagingException e) {
            e.printStackTrace();
            logger.warn(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "receiveCsr", HttpStatus.BAD_REQUEST, "invalid csr format"));
            return new ResponseEntity<>(csr, HttpStatus.BAD_REQUEST);
        }
        catch (IOException e) {
            e.printStackTrace();
            logger.error(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "receiveCsr", HttpStatus.INTERNAL_SERVER_ERROR, "io exception"));
            return new ResponseEntity<>(csr, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                principal.getName(), "receiveCsr", HttpStatus.OK, "csr received"));
        return new ResponseEntity<>(csr, HttpStatus.OK);
    }

    @GetMapping(value="/verification")
    // UNAUTHORIZED
    public void verifyCsr(@RequestParam("token") @Size(min = 36, max = 36, message = "Verification link is invalid") String token) {
        csrService.verifyCsr(token);
        logger.info(String.format("%s called method %s with status code %s: %s",
                "Email link", "verifyCsr", HttpStatus.OK, "csr verified"));
    }

    @GetMapping
    // SUPER ADMIN
    public ResponseEntity<List<CsrDTO>> findAllCsr(Principal principal) {
        List<CSR> csrList = csrService.findAllByVerified(true);
        List<CsrDTO> csrDTOList = csrMapper.toCsrDtoList(csrList);

        logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                principal.getName(), "findAllCsr", HttpStatus.OK, "authorized"));
        return new ResponseEntity<>(csrDTOList, HttpStatus.OK);
    }

    @PutMapping(value = "accept/{id}")
    // SUPER ADMIN
    public void acceptCsr(Principal principal, @PathVariable("id") @NotNull(message = "Id cannot be empty")
                              @Positive( message = "Id is invalid") Long id) {
        try {
            csrService.acceptCsr(id);
            logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "acceptCsr", HttpStatus.OK, "csr accepted"));
        } catch (NonExistentIdException e) {
            logger.warn(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "acceptCsr", HttpStatus.NOT_FOUND, "non existing csr id"));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping(value = "decline/{id}")
    // SUPER ADMIN
    public void declineCsr(Principal principal, @PathVariable("id") @NotNull(message = "Id cannot be empty")
                               @Positive( message = "Id is invalid") Long id) {
        try {
            csrService.declineCsr(id);
            logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "declineCsr", HttpStatus.OK, "csr declined"));
        } catch (NonExistentIdException | MessagingException e) {
            logger.warn(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "declineCsr", HttpStatus.NOT_FOUND, "non existing csr id"));
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
