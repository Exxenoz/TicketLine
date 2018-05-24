package at.ac.tuwien.inso.sepm.ticketline.rest.exception;

public class AddressValidationException extends Exception {
    public AddressValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AddressValidationException(String message) {
        super(message);
    }
}
