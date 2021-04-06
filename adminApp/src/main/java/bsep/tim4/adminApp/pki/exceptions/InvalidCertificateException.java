package bsep.tim4.adminApp.pki.exceptions;

public class InvalidCertificateException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidCertificateException(String alias) {
        super(String.format("Certificate with alias: '%s' doesn't exist.", alias));
    }
}
