package at.ac.tuwien.inso.sepm.ticketline.server.exception.service;

public class InternalForbiddenException extends Exception {
    public InternalForbiddenException() {
    }

    public InternalForbiddenException(String message) {
        super(message);
    }
}
