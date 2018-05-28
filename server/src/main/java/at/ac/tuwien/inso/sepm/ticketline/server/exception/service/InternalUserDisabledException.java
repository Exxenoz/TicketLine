package at.ac.tuwien.inso.sepm.ticketline.server.exception.service;

import org.springframework.security.core.AuthenticationException;

public class InternalUserDisabledException extends AuthenticationException {
    public InternalUserDisabledException(String msg, Throwable t) {
        super(msg, t);
    }

    public InternalUserDisabledException(String msg) {
        super(msg);
    }
}
