package at.ac.tuwien.inso.sepm.ticketline.client.exception;

public class CustomerValidationException extends Exception {
    public CustomerValidationException() {
    }

    public CustomerValidationException(String message) {
        super(message);
    }
}
