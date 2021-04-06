package bsep.tim4.adminApp.pki.exceptions;

public class CertificateNotCAException extends Exception {
    private static final long serialVersionUID = 1L;

    public CertificateNotCAException(String alias) {
        super(String.format("Certificate with alias: '%s' is not CA.", alias));
    }
}
