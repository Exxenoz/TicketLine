package at.ac.tuwien.inso.sepm.ticketline.client.exception;

public class UserValidationException extends Exception {

    public UserValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserValidationException(String message) {
        super(message);
    }

}
