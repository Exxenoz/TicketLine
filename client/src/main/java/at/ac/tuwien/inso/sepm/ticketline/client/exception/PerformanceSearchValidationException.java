package at.ac.tuwien.inso.sepm.ticketline.client.exception;

public class PerformanceSearchValidationException extends Exception {

    private String exceptionBundleKey;

    public PerformanceSearchValidationException(){}

    public PerformanceSearchValidationException(String message, String exceptionBundleKey) {
        super(message);

        this.exceptionBundleKey = exceptionBundleKey;
    }

    public String getExceptionBundleKey() {
        return exceptionBundleKey;
    }
}
