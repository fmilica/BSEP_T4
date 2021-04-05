package bsep.tim4.adminApp.pki.controller;

import bsep.tim4.adminApp.pki.exceptions.NonExistentIdException;
import bsep.tim4.adminApp.pki.model.CertificateData;
import bsep.tim4.adminApp.pki.model.IssuerData;
import bsep.tim4.adminApp.pki.model.dto.CertificateDTO;
import bsep.tim4.adminApp.pki.model.dto.CertificateSignerDTO;
import bsep.tim4.adminApp.pki.model.dto.CertificateViewDTO;
import bsep.tim4.adminApp.pki.model.dto.CreateCertificateDTO;
import bsep.tim4.adminApp.pki.model.mapper.CertificateSignerMapper;
import bsep.tim4.adminApp.pki.service.CertificateDataService;
import bsep.tim4.adminApp.pki.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
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
    public ResponseEntity<String> createCertificate(@RequestBody CreateCertificateDTO certDto) {
        String certificate = "nesto ne valja";
        try {
            certificate = certificateService.createCertificate(certDto);
        } catch (NonExistentIdException | MessagingException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(certificate, HttpStatus.OK);
    }

    @GetMapping( value = "/download")
    public ResponseEntity<Object> downloadCertificate(@RequestParam("token") String token) {
        String alias = certificateService.findByToken(token);
        if (alias == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String certificateChain = null;
        try {
            certificateChain = certificateService.getPemCertificate(alias);
            //certificateChain = certificateService.getPemCertificateChain(alias);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] contents = certificateChain.getBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        String filename = "certificate.crt";
        ContentDisposition contentDisposition = ContentDisposition
                .builder("inline")
                .filename(filename)
                .build();
        headers.setContentDisposition(contentDisposition);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
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

    @GetMapping( value = "/{alias}")
    public ResponseEntity<CertificateSignerDTO> getByAlias(@PathVariable String alias) {
        IssuerData issuerData = certificateService.getByAlias(alias);
        CertificateSignerDTO certificateSignerDTO = certificateSignerMapper.toCertificateSignerDto(issuerData);

        return new ResponseEntity<>(certificateSignerDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CertificateViewDTO>> getAllCertificates() {
        CertificateViewDTO root = certificateService.getAllCertificates();

        List<CertificateViewDTO> certificateViewDTOS = new ArrayList<>();
        certificateViewDTOS.add(root);

        return new ResponseEntity<>(certificateViewDTOS, HttpStatus.OK);
    }


}
