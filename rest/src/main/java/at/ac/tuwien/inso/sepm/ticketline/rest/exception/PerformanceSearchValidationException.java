package at.ac.tuwien.inso.sepm.ticketline.rest.exception;

public class PerformanceSearchValidationException extends Exception {
    public PerformanceSearchValidationException(String message) {
        super(message);}

    public PerformanceSearchValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
