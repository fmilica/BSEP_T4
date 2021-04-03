package bsep.tim4.adminApp.pki.controller;

import bsep.tim4.adminApp.pki.service.CertificateService;
import bsep.tim4.adminApp.pki.service.CsrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping(value="api/csr")
public class CsrController {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private CsrService csrService;

    @PostMapping(value="/receive")
    public void receiveCsr(@RequestBody String csr) {
        //Primljen csr se skladisti u bazu i na taj email se salje konfirmacioni link
        csrService.saveCsr(csr);

        //String certificate = certificateService.generateCertificate(csr);
        //System.out.println(certificate);
        //return new ResponseEntity<>(certificate, HttpStatus.OK);
    }

    @GetMapping(value="/verification")
    public void verifyCsr(@RequestParam("token") String token) {
        csrService.verifyCsr(token);
    }
}
