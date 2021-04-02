package bsep.tim4.hospitalApp.service;

import bsep.tim4.hospitalApp.dto.CSRDto;
import bsep.tim4.hospitalApp.util.CSRGenerator;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyPair;

@Service
public class CSRService {

    @Autowired
    private KeyStoreService keyStoreService;

    private CSRGenerator csrGenerator;

    public CSRService() {
        this.csrGenerator = new CSRGenerator();
    }

    public String createCSR(CSRDto csrDto) {
        KeyPair keyPair = this.csrGenerator.generateKeyPair();
        // cuvanje privatnog kljuca
        keyStoreService.loadKeyStore();
        //keyStoreService.savePrivateKey(csrDto.getEmail(), keyPair.getPrivate(), csrDto.getKeyPassword(), null);
        keyStoreService.saveKeyStore();
        // generisanje CertificateSigningRequest-a
        PKCS10CertificationRequest csr = this.csrGenerator.generateCSR(csrDto);
            // vraca byte[]
            //return csr.getEncoded();
        return this.csrGenerator.convertCsr(csr);
    }
}
