package at.ac.tuwien.inso.sepm.ticketline.client.exception;

public class InvalidObjectDataException extends Exception {

    public InvalidObjectDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidObjectDataException(String message) {
        super(message);
    }
}
