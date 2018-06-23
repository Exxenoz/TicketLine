package at.ac.tuwien.inso.sepm.ticketline.client.exception;

public class CustomerValidationException extends Exception {

    private String exceptionBundleKey;

    public CustomerValidationException() {
    }

    public CustomerValidationException(String message, String exceptionBundleKey) {
        super(message);

        this.exceptionBundleKey = exceptionBundleKey;
    }

    public String getExceptionBundleKey() {
        return exceptionBundleKey;
    }
}
