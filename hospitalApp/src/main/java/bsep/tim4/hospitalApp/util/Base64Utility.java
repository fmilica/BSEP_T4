package bsep.tim4.hospitalApp.util;

import java.io.IOException;
import java.util.Base64;


public class Base64Utility {
    // Pomocna funkcija za enkodovanje bajtova u string
    public static String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    // Pomocna funkcija za dekodovanje stringa u bajt niz
    public static byte[] decode(String base64Data) throws IOException {
        return Base64.getDecoder().decode(base64Data);
    }
}
