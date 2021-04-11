package bsep.tim4.adminApp.pki.controller;

import bsep.tim4.adminApp.pki.exceptions.CertificateNotCAException;
import bsep.tim4.adminApp.pki.exceptions.InvalidCertificateException;
import bsep.tim4.adminApp.pki.exceptions.NonExistentIdException;
import bsep.tim4.adminApp.pki.model.IssuerData;
import bsep.tim4.adminApp.pki.model.dto.*;
import bsep.tim4.adminApp.pki.model.mapper.CertificateSignerMapper;
import bsep.tim4.adminApp.pki.service.CertificateDataService;
import bsep.tim4.adminApp.pki.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
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

    @GetMapping( value = "/validate/{serialNumb}" )
    public ResponseEntity<String> validateCertificate(@PathVariable Long serialNumb) {
        try {
            String alias = certificateDataService.getDateValidity(serialNumb);
            if (alias != null) {
                if (certificateService.validateCertificate(alias)) {
                    return new ResponseEntity<>("VALID", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("INVALID", HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>("INVALID", HttpStatus.OK);
            }
        } catch (NonExistentIdException e) {
            return new ResponseEntity<>("NOT_EXISTANT_SERIAL_NUMBER", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping( value = "/detailed/{alias}" )
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<CertificateDetailedViewDTO> getDetailedCertificate(@PathVariable String alias) {
        CertificateDetailedViewDTO detailedView = certificateService.getDetails(alias);
        return new ResponseEntity<CertificateDetailedViewDTO>(detailedView, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<String> createCertificate(@RequestBody CreateCertificateDTO certDto) {
        String certificate = "";
        try {
            certificate = certificateService.createCertificate(certDto);
        } catch (NonExistentIdException | MessagingException e) {
            e.printStackTrace();
        } catch (CertificateNotCAException | InvalidCertificateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
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
            certificateChain = certificateService.getPemCertificateChain(alias);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] contents = certificateChain.getBytes();
        HttpHeaders headers = createDownloadCertHeaders();
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }

    @GetMapping( value = "/download-any")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<Object> adminDownloadCertificate(@RequestParam("alias") String alias) {
        String certificateChain = null;
        try {
            certificateChain = certificateService.getPemCertificateChain(alias);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] contents = certificateChain.getBytes();
        HttpHeaders headers = createDownloadCertHeaders();
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }

    @GetMapping( value = "/download-pkcs12")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<Object> adminDownloadPkcs12(@RequestParam("alias") String alias) {
        byte[] contents = certificateService.getPkcs12Format(alias);
        HttpHeaders headers = createDownloadCertHeaders();
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }

    @GetMapping( value = "/root-certificate")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<CertificateSignerDTO> getRootCertificate() {
        IssuerData issuerData = certificateService.getRootCertificate();

        CertificateSignerDTO certificateSignerDTO = certificateSignerMapper.toCertificateSignerDto(issuerData);

        return new ResponseEntity<>(certificateSignerDTO, HttpStatus.OK);
    }

    @GetMapping( value = "/ca-certificates")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<CertificateSignerDTO>> getCACertificates() {
        List<IssuerData> issuerDataList = certificateService.getAllCAIssuers();

        List<CertificateSignerDTO> certificateSignerDTOList = certificateSignerMapper.toCertificateSignerDtoList(issuerDataList);

        return new ResponseEntity<>(certificateSignerDTOList, HttpStatus.OK);
    }

    @GetMapping( value = "/{alias}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<CertificateSignerDTO> getByAlias(@PathVariable String alias) {
        IssuerData issuerData = certificateService.getByAlias(alias);
        CertificateSignerDTO certificateSignerDTO = certificateSignerMapper.toCertificateSignerDto(issuerData);

        return new ResponseEntity<>(certificateSignerDTO, HttpStatus.OK);
    }

    @PostMapping( value = "revoke/{alias}" )
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<Void> revokeCertificate(@PathVariable String alias, @RequestBody String revocationReason) {
        try {
            certificateDataService.revoke(alias, revocationReason);
        } catch (NonExistentIdException | MessagingException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Void>(HttpStatus.OK);
    }


    @GetMapping
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<CertificateViewDTO>> getAllCertificates() {
        List<CertificateViewDTO> certificateViewDTOS = certificateDataService.findCertificateView();
        /*CertificateViewDTO root = certificateService.getAllCertificates();*/

        return new ResponseEntity<>(certificateViewDTOS, HttpStatus.OK);
    }

    private HttpHeaders createDownloadCertHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        String filename = "certificate.crt";
        ContentDisposition contentDisposition = ContentDisposition
                .builder("inline")
                .filename(filename)
                .build();
        headers.setContentDisposition(contentDisposition);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return headers;
    }
}
