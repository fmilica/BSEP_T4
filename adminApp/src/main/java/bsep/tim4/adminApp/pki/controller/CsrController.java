package bsep.tim4.adminApp.pki.controller;

import bsep.tim4.adminApp.pki.exceptions.NonExistentIdException;
import bsep.tim4.adminApp.pki.model.CSR;
import bsep.tim4.adminApp.pki.model.dto.CsrDTO;
import bsep.tim4.adminApp.pki.model.mapper.CsrMapper;
import bsep.tim4.adminApp.pki.service.CertificateService;
import bsep.tim4.adminApp.pki.service.CsrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.validation.constraints.*;
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

    @PostMapping(value="/receive")
    // ADMIN
    public ResponseEntity<String> receiveCsr(@RequestBody @NotBlank(message = "Csr cannot be empty") String csr) {
        //Primljen csr se skladisti u bazu i na taj email se salje konfirmacioni link
        csrService.saveCsr(csr);
        return new ResponseEntity<>(csr, HttpStatus.OK);
    }

    @GetMapping(value="/verification")
    // UNAUTHORIZED
    public void verifyCsr(@RequestParam("token") @Size(min = 86, max = 86, message = "Verification link is invalid") String token) {
        csrService.verifyCsr(token);
    }

    @GetMapping
    // SUPER ADMIN
    public ResponseEntity<List<CsrDTO>> findAll() {
        List<CSR> csrList = csrService.findAllByVerified(true);
        List<CsrDTO> csrDTOList = csrMapper.toCsrDtoList(csrList);

        return new ResponseEntity<>(csrDTOList, HttpStatus.OK);
    }

    @PutMapping(value = "accept/{id}")
    // SUPER ADMIN
    public void acceptCsr(@PathVariable("id") @NotNull(message = "Id cannot be empty")
                              @Positive( message = "Id is invalid") Long id) {
        try {
            csrService.acceptCsr(id);
        } catch (NonExistentIdException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping(value = "decline/{id}")
    // SUPER ADMIN
    public void declineCsr(@PathVariable("id") @NotBlank(message = "Id cannot be empty")
                               @Positive( message = "Id is invalid") Long id) {
        try {
            csrService.declineCsr(id);
        } catch (NonExistentIdException | MessagingException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
