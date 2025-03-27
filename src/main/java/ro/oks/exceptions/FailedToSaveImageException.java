package ro.oks.exceptions;

public class FailedToSaveImageException extends RuntimeException {
    public FailedToSaveImageException(String message) {
        super(message);
    }
}
