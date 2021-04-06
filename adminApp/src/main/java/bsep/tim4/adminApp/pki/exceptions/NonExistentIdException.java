package bsep.tim4.adminApp.pki.exceptions;

public class NonExistentIdException extends Exception {

    private static final long serialVersionUID = 1L;

    public NonExistentIdException(String type) {
        super(String.format("%s with given id doesn't exist.", type));
    }
}
