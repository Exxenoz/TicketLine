package at.ac.tuwien.inso.sepm.ticketline.client.exception;

public class NewsValidationException extends Exception {

    private String exceptionBundleKey;

    public NewsValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public NewsValidationException(String message, String exceptionBundleKey) {
        super(message);

        this.exceptionBundleKey = exceptionBundleKey;
    }

    public String getExceptionBundleKey() {
        return exceptionBundleKey;
    }
}
