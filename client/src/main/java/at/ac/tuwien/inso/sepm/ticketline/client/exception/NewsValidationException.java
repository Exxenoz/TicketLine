package at.ac.tuwien.inso.sepm.ticketline.client.exception;

public class NewsValidationException extends Exception {

    public NewsValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public NewsValidationException(String message) {
        super(message);
    }

}
