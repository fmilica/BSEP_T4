package bsep.tim4.adminApp.pki.controller;

import bsep.tim4.adminApp.pki.model.IssuerData;
import bsep.tim4.adminApp.pki.model.dto.CertificateSignerDTO;
import bsep.tim4.adminApp.pki.model.mapper.CertificateSignerMapper;
import bsep.tim4.adminApp.pki.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.cert.Certificate;
import java.util.List;

@RestController
@RequestMapping(value = "api/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    private final CertificateSignerMapper certificateSignerMapper = new CertificateSignerMapper();

    @GetMapping( value = "/rootCertificate")
    public ResponseEntity<CertificateSignerDTO> getRootCertificate() {
        IssuerData issuerData = certificateService.getRootCertificate();

        CertificateSignerDTO certificateSignerDTO = certificateSignerMapper.toCertificateSignerDto(issuerData);

        return new ResponseEntity<>(certificateSignerDTO, HttpStatus.OK);
    }

    @GetMapping( value = "/CACertificates")
    public ResponseEntity<List<CertificateSignerDTO>> getCACertificates() {
        List<IssuerData> issuerDataList = certificateService.getAllCAIssuers();

        List<CertificateSignerDTO> certificateSignerDTOList = certificateSignerMapper.toCertificateSignerDtoList(issuerDataList);

        return new ResponseEntity<>(certificateSignerDTOList, HttpStatus.OK);
    }


}
