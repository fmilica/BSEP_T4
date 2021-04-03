package bsep.tim4.adminApp.pki.controller;

import bsep.tim4.adminApp.pki.model.CSR;
import bsep.tim4.adminApp.pki.model.dto.CsrDTO;
import bsep.tim4.adminApp.pki.model.mapper.CsrMapper;
import bsep.tim4.adminApp.pki.service.CertificateService;
import bsep.tim4.adminApp.pki.service.CsrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="api/csr")
public class CsrController {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private CsrService csrService;

    private final CsrMapper csrMapper = new CsrMapper();

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

    @GetMapping
    public ResponseEntity<List<CsrDTO>> findAll() {
        List<CSR> csrList = csrService.findAllByVerified(true);
        List<CsrDTO> csrDTOList = csrMapper.toCsrDtoList(csrList);

        return new ResponseEntity<>(csrDTOList, HttpStatus.OK);
    }
}
