package at.ac.tuwien.inso.sepm.ticketline.rest.exception;

public class CustomerValidationException extends Exception{
    public CustomerValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomerValidationException(String message) {
        super(message);
    }
}
