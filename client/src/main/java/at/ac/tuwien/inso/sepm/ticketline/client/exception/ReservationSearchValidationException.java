package at.ac.tuwien.inso.sepm.ticketline.client.exception;

public class ReservationSearchValidationException extends Exception {

    private String exceptionBundleKey;

    public ReservationSearchValidationException() {
    }

    public ReservationSearchValidationException(String message, String exceptionBundleKey) {
        super(message);

        this.exceptionBundleKey = exceptionBundleKey;
    }

    public String getExceptionBundleKey() {
        return exceptionBundleKey;
    }
}
