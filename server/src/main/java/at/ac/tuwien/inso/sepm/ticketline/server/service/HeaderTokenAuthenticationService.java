package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationToken;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationTokenInfo;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;

public interface HeaderTokenAuthenticationService {

    /**
     * Authenticate users with username and password.
     *
     * @param username of the users
     * @param password of the users
     * @return an authentication token
     */
    AuthenticationToken authenticate(String username, CharSequence password);

    /**
     * Get informations about a header token.
     *
     * @param headerToken the authentication token for which the informations should be obtained
     * @return informations about the given token
     */
    AuthenticationTokenInfo authenticationTokenInfo(String headerToken);

    /**
     * Renew authentication based on a header token.
     *
     * @param headerToken current authentication token.
     * @return an authentication token
     */
    AuthenticationToken renewAuthentication(String headerToken);

    /**
     * Authenticate users with a header token.
     *
     * @param headerToken which should be users to authenticate users
     * @return Authenticated users
     * @throws AuthenticationException when the authentication of the provided headerToken fails
     */
    User authenticate(String headerToken) throws AuthenticationException;

}
