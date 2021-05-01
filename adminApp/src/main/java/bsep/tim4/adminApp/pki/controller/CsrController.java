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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import java.util.List;

//@CrossOrigin(origins = {"https://localhost:4200", "https://localhost:8081"} )
//@CrossOrigin()
@RestController
@RequestMapping(value="api/csr")
public class CsrController {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private CsrService csrService;

    private final CsrMapper csrMapper = new CsrMapper();

    @PostMapping(value="/receive")
    public ResponseEntity<String> receiveCsr(@RequestBody String csr) {
        //Primljen csr se skladisti u bazu i na taj email se salje konfirmacioni link
        csrService.saveCsr(csr);
        return new ResponseEntity<>(csr, HttpStatus.OK);
    }

    @GetMapping(value="/verification")
    public void verifyCsr(@RequestParam("token") String token) {
        csrService.verifyCsr(token);
    }

    @GetMapping
//    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<CsrDTO>> findAll() {
        List<CSR> csrList = csrService.findAllByVerified(true);
        List<CsrDTO> csrDTOList = csrMapper.toCsrDtoList(csrList);

        return new ResponseEntity<>(csrDTOList, HttpStatus.OK);
    }

    @PutMapping(value = "accept/{id}")
//    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public void acceptCsr(@PathVariable("id") Long id) {
        try {
            csrService.acceptCsr(id);
        } catch (NonExistentIdException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping(value = "decline/{id}")
//    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public void declineCsr(@PathVariable("id") Long id) {
        try {
            csrService.declineCsr(id);
        } catch (NonExistentIdException | MessagingException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
