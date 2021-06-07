package bsep.tim4.hospitalApp.exceptions;

public class ExistingConfigurationFolderException extends Exception {

    private static final long serialVersionUID = 1L;

    public ExistingConfigurationFolderException(String path) {
        super(String.format("Configuration folder with given path %s already in use.", path));
    }
}