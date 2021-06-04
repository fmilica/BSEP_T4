package bsep.tim4.hospitalApp.startup;

import bsep.tim4.hospitalApp.service.KeyStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class HospitalAppInit implements ApplicationRunner {

    @Autowired
    private KeyStoreService keyStoreService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // ako keystore ne postoji, kreiraj ga
        keyStoreService.loadSymKeyStore();
        // ako simetricni kljuc ne postoji, kreiraj ga
        Key symKey = keyStoreService.getSymKey();
        if (symKey == null) {
            keyStoreService.createSymetricKey();
        }
    }
}
