package at.ac.tuwien.inso.sepm.ticketline.rest.exception;

public class UserValidatorException extends Exception {
    public UserValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserValidatorException(String message) {
        super(message);
    }
}
