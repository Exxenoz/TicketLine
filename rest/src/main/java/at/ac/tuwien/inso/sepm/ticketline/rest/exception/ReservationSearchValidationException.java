package at.ac.tuwien.inso.sepm.ticketline.rest.exception;

public class ReservationSearchValidationException extends Exception {
    public ReservationSearchValidationException(String message) {
        super(message);
    }

    public ReservationSearchValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
