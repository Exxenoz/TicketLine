package at.ac.tuwien.inso.sepm.ticketline.server.security;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    /**
     * Returns the reference of an authentication object
     *
     * @return authentication reference
     */
    Authentication getAuthentication();
}
