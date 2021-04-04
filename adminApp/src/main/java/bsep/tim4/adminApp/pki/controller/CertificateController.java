package bsep.tim4.adminApp.pki.controller;

import bsep.tim4.adminApp.pki.model.CertificateData;
import bsep.tim4.adminApp.pki.model.IssuerData;
import bsep.tim4.adminApp.pki.model.dto.CertificateDTO;
import bsep.tim4.adminApp.pki.model.dto.CertificateSignerDTO;
import bsep.tim4.adminApp.pki.model.dto.CertificateViewDTO;
import bsep.tim4.adminApp.pki.model.mapper.CertificateSignerMapper;
import bsep.tim4.adminApp.pki.service.CertificateDataService;
import bsep.tim4.adminApp.pki.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.cert.Certificate;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "api/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;
    @Autowired
    private CertificateDataService certificateDataService;

    private final CertificateSignerMapper certificateSignerMapper = new CertificateSignerMapper();

    @PostMapping
    public ResponseEntity<String> createCertificate(@RequestBody String csr) {
        //certificateDataService.save(certData);
        certificateService.generateCertificate(csr);
        return new ResponseEntity<>("kreiran", HttpStatus.OK);
    }

    @GetMapping( value = "/root-certificate")
    public ResponseEntity<CertificateSignerDTO> getRootCertificate() {
        IssuerData issuerData = certificateService.getRootCertificate();

        CertificateSignerDTO certificateSignerDTO = certificateSignerMapper.toCertificateSignerDto(issuerData);

        return new ResponseEntity<>(certificateSignerDTO, HttpStatus.OK);
    }

    @GetMapping( value = "/ca-certificates")
    public ResponseEntity<List<CertificateSignerDTO>> getCACertificates() {
        List<IssuerData> issuerDataList = certificateService.getAllCAIssuers();

        List<CertificateSignerDTO> certificateSignerDTOList = certificateSignerMapper.toCertificateSignerDtoList(issuerDataList);

        return new ResponseEntity<>(certificateSignerDTOList, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<CertificateViewDTO> getAllCertificates() {
        CertificateViewDTO root = certificateService.getAllCertificates();

        return new ResponseEntity<>(root, HttpStatus.OK);
    }


}
