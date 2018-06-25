package at.ac.tuwien.inso.sepm.ticketline.client.exception;

public class AddressValidationException extends Exception {

    private String exceptionBundleKey;

    public AddressValidationException() {
    }

    public AddressValidationException(String message, String exceptionBundleKey) {
        super(message);

        this.exceptionBundleKey = exceptionBundleKey;
    }

    public String getExceptionBundleKey() {
        return exceptionBundleKey;
    }
}
