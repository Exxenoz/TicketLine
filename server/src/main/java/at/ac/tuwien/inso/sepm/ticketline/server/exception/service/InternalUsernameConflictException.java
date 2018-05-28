package at.ac.tuwien.inso.sepm.ticketline.server.exception.service;

public class InternalUsernameConflictException extends Exception {
    public InternalUsernameConflictException() {
    }

    public InternalUsernameConflictException(String message) {
        super(message);
    }
}
