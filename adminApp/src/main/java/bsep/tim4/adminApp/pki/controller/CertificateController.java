package bsep.tim4.adminApp.pki.controller;

import bsep.tim4.adminApp.pki.exceptions.CertificateNotCAException;
import bsep.tim4.adminApp.pki.exceptions.InvalidCertificateException;
import bsep.tim4.adminApp.pki.exceptions.NonExistentIdException;
import bsep.tim4.adminApp.pki.model.IssuerData;
import bsep.tim4.adminApp.pki.model.dto.*;
import bsep.tim4.adminApp.pki.model.mapper.CertificateSignerMapper;
import bsep.tim4.adminApp.pki.service.CertificateDataService;
import bsep.tim4.adminApp.pki.service.CertificateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "api/certificate")
@Validated
public class CertificateController {

    @Autowired
    private CertificateService certificateService;
    @Autowired
    private CertificateDataService certificateDataService;

    private final CertificateSignerMapper certificateSignerMapper = new CertificateSignerMapper();

    Logger logger = LoggerFactory.getLogger(CsrController.class);

    @GetMapping( value = "/validate/{serialNumb}" )
    // UNATHORIZED
    public ResponseEntity<Boolean> validateCertificate(@PathVariable @NotNull(message = "Serial number cannot be empty")
                                                          @Positive( message = "Serial number is invalid") Long serialNumb) {
        try {
            String alias = certificateDataService.getDateValidity(serialNumb);
            if (alias != null) {
                if (certificateService.validateCertificate(alias)) {
                    // validan sertifikat
                    logger.info(String.format("%s called method %s with status code %s: %s",
                            "Hospital", "validateCertificate", HttpStatus.OK, "valid certificate"));
                    return new ResponseEntity<>(true, HttpStatus.OK);
                } else {
                    // nije validan sertifikat
                    logger.info(String.format("%s called method %s with status code %s: %s",
                            "Hospital", "validateCertificate", HttpStatus.OK, "not valid certificate"));
                    return new ResponseEntity<>(false, HttpStatus.OK);
                }
            } else {
                // ne postoji sa tim serijskim brojem
                // NOT_EXISTANT_SERIAL_NUMBER
                logger.warn(String.format("%s called method %s with status code %s: %s",
                        "Hospital", "validateCertificate", HttpStatus.NOT_FOUND, "non existing serial number"));
                return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
            }
        } catch (NonExistentIdException e) {
            // ne postoji sa tim serijskim brojem
            // NOT_EXISTANT_SERIAL_NUMBER
            logger.warn(String.format("%s called method %s with status code %s: %s",
                    "Hospital", "validateCertificate", HttpStatus.NOT_FOUND, "non existing serial number"));
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        } catch (NoSuchProviderException | CertificateException | NoSuchAlgorithmException | InvalidKeyException | SignatureException e){
            logger.error(String.format("%s called method %s with status code %s: %s",
                    "Hospital", "validateCertificate", HttpStatus.INTERNAL_SERVER_ERROR, "certificate error"));
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping( value = "/detailed/{alias}" )
    // SUPER ADMIN
    public ResponseEntity<CertificateDetailedViewDTO> getDetailedCertificate(
                                Principal principal,
                                @PathVariable
                                @NotBlank(message = "Alias cannot be empty")
                                @Size( min = 1, max = 50, message = "Alias is too long")
                                @Pattern(regexp = "[a-zA-Z0-9-]+", message = "Alias is not valid") String alias) {
        CertificateDetailedViewDTO detailedView = null;
        try {
            detailedView = certificateService.getDetails(alias);
        } catch (CertificateEncodingException e) {
            logger.error(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "getDetailedCertificate", HttpStatus.INTERNAL_SERVER_ERROR, "certificate encoding error"));
            return new ResponseEntity<CertificateDetailedViewDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                principal.getName(), "getDetailedCertificate", HttpStatus.OK, "authorized"));
        return new ResponseEntity<CertificateDetailedViewDTO>(detailedView, HttpStatus.OK);
    }

    @PostMapping
    // SUPER ADMIN
    public ResponseEntity<String> createCertificate(Principal principal, @RequestBody @Valid CreateCertificateDTO certDto) {
        String certificate = "";
        try {
            certificate = certificateService.createCertificate(certDto);
        } catch (NonExistentIdException | MessagingException e) {
            logger.warn(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "createCertificate", HttpStatus.NOT_FOUND, "non existing csr id"));
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (CertificateNotCAException | InvalidCertificateException e) {
            logger.error(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "createCertificate", HttpStatus.BAD_REQUEST, "invalid certificate data"));
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (CertificateException | NoSuchAlgorithmException | InvalidKeyException | SignatureException | NoSuchProviderException e) {
            e.printStackTrace();
            logger.error(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "createCertificate", HttpStatus.BAD_REQUEST, "certificate creation"));
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                principal.getName(), "createCertificate", HttpStatus.OK, "authorized"));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping( value = "/download")
    // UNAUTHORIZED
    public ResponseEntity<Object> downloadCertificate(@RequestParam("token")
                                @Size(min = 36, max = 36, message = "Download certificate link is invalid") String token) {
        String alias = certificateService.findByToken(token);
        if (alias == null) {
            logger.warn(String.format("%s called method %s with status code %s: %s",
                    "Email link", "downloadCertificate", HttpStatus.NOT_FOUND, "non existing certificate alias"));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String certificateChain = null;
        try {
            certificateChain = certificateService.getPemCertificateChain(alias);
        } catch (IOException e) {
            logger.error(String.format("%s called method %s with status code %s: %s",
                    "Email link", "downloadCertificate", HttpStatus.INTERNAL_SERVER_ERROR, "file exception"));
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        byte[] contents = certificateChain.getBytes();
        HttpHeaders headers = createDownloadCertHeaders();
        logger.info(String.format("%s called method %s with status code %s: %s",
                "Email link", "downloadCertificate", HttpStatus.OK, "existing certificate alias"));
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }

    @GetMapping( value = "/download-any")
    // SUPER ADMIN
    public ResponseEntity<Object> adminDownloadCertificate(
                                       Principal principal,
                                       @RequestParam("alias")
                                       @NotBlank(message = "Alias cannot be empty")
                                       @Size( min = 1, max = 50, message = "Alias is too long")
                                       @Pattern(regexp = "[a-zA-Z0-9-]+", message = "Alias is not valid") String alias) {
        String certificateChain = null;
        try {
            certificateChain = certificateService.getPemCertificateChain(alias);
        } catch (IOException e) {
            logger.error(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "adminDownloadCertificate", HttpStatus.INTERNAL_SERVER_ERROR, "file exception"));
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        byte[] contents = certificateChain.getBytes();
        HttpHeaders headers = createDownloadCertHeaders();
        logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                principal.getName(), "adminDownloadCertificate", HttpStatus.OK, "existing certificate alias"));
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }

    @GetMapping( value = "/download-pkcs12")
    // SUPER ADMIN
    public ResponseEntity<Object> adminDownloadPkcs12(
                                      Principal principal,
                                      @RequestParam("alias")
                                      @NotBlank(message = "Alias cannot be empty")
                                      @Size( min = 1, max = 50, message = "Alias is too long")
                                      @Pattern(regexp = "[a-zA-Z0-9-]+", message = "Alias is not valid") String alias) {
        byte[] contents = new byte[0];
        try {
            contents = certificateService.getPkcs12Format(alias);
        } catch (NoSuchProviderException | KeyStoreException | CertificateException| NoSuchAlgorithmException e) {
            logger.error(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "adminDownloadPkcs12", HttpStatus.INTERNAL_SERVER_ERROR, "certificate exception"));
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            logger.error(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "adminDownloadPkcs12", HttpStatus.INTERNAL_SERVER_ERROR, "file exception"));
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        HttpHeaders headers = createDownloadCertHeaders();
        logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                principal.getName(), "adminDownloadPkcs12", HttpStatus.OK, "existing certificate alias"));
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }

    @GetMapping( value = "/root-certificate")
    // SUPER ADMIN
    public ResponseEntity<CertificateSignerDTO> getRootCertificate(Principal principal) {
        IssuerData issuerData = certificateService.getRootCertificate();

        CertificateSignerDTO certificateSignerDTO = certificateSignerMapper.toCertificateSignerDto("adminroot", issuerData);

        logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                principal.getName(), "getRootCertificate", HttpStatus.OK, "authorized"));
        return new ResponseEntity<>(certificateSignerDTO, HttpStatus.OK);
    }

    @GetMapping( value = "/ca-certificates")
    // SUPER ADMIN
    public ResponseEntity<List<CertificateSignerDTO>> getCACertificates(Principal principal) {
        Map<String, IssuerData> issuerDataList = certificateService.getAllCAIssuers();

        List<CertificateSignerDTO> certificateSignerDTOList = certificateSignerMapper.toCertificateSignerDtoList(issuerDataList);

        logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                principal.getName(), "getCACertificates", HttpStatus.OK, "authorized"));
        return new ResponseEntity<>(certificateSignerDTOList, HttpStatus.OK);
    }

    @GetMapping( value = "/{alias}")
    // SUPER ADMIN
    public ResponseEntity<CertificateSignerDTO> getByAlias(
                                       Principal principal,
                                       @PathVariable
                                       @NotBlank(message = "Alias cannot be empty")
                                       @Size( min = 1, max = 50, message = "Alias is too long")
                                       @Pattern(regexp = "[a-zA-Z0-9-]+", message = "Alias is not valid") String alias) {
        IssuerData issuerData = certificateService.getByAlias(alias);
        CertificateSignerDTO certificateSignerDTO = certificateSignerMapper.toCertificateSignerDto(alias, issuerData);

        logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                principal.getName(), "getByAlias", HttpStatus.OK, "authorized"));
        return new ResponseEntity<>(certificateSignerDTO, HttpStatus.OK);
    }

    @PostMapping( value = "revoke/{alias}" )
    // SUPER ADMIN
    public ResponseEntity<Void> revokeCertificate(
                                      Principal principal,
                                      @PathVariable
                                      @NotBlank(message = "Alias cannot be empty")
                                      @Size( min = 1, max = 50, message = "Alias is too long")
                                      @Pattern(regexp = "[a-zA-Z0-9-]+", message = "Alias is not valid") String alias,
                                      @RequestBody @Valid String revocationReason) {
        try {
            certificateDataService.revoke(alias, revocationReason);
        } catch (NonExistentIdException | MessagingException e) {
            logger.warn(String.format("User with userId=%s called method %s with status code %s: %s",
                    principal.getName(), "revokeCertificate", HttpStatus.NOT_FOUND, "non existing certificate alias"));
            e.printStackTrace();
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                principal.getName(), "revokeCertificate", HttpStatus.OK, "authorized"));
        return new ResponseEntity<Void>(HttpStatus.OK);
    }


    @GetMapping
    // SUPER ADMIN
    public ResponseEntity<List<CertificateViewDTO>> getAllCertificates(Principal principal) {
        List<CertificateViewDTO> certificateViewDTOS = certificateDataService.findCertificateView();

        logger.info(String.format("User with userId=%s called method %s with status code %s: %s",
                principal.getName(), "getAllCertificates", HttpStatus.OK, "authorized"));
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
