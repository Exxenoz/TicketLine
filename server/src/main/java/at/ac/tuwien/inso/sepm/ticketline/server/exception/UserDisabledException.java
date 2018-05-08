package at.ac.tuwien.inso.sepm.ticketline.server.exception;

import org.springframework.security.core.AuthenticationException;

public class UserDisabledException extends AuthenticationException {

    public UserDisabledException(String msg) {
        super(msg);
    }

    public UserDisabledException(String msg, Throwable t) {
        super(msg, t);
    }

}
