package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationRequest;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationTokenInfo;

public interface AuthenticationService {

    /**
     * Authenticate with username and password.
     *
     * @param authenticationRequest containing the username and password to authenticate
     * @return A valid authentication token
     * @throws DataAccessException in case something went wrong
     */
    AuthenticationTokenInfo authenticate(AuthenticationRequest authenticationRequest) throws DataAccessException;

    /**
     * Deauthenticate current user
     *
     */
    void deAuthenticate();

    /**
     * A simple helper method provided to setup up everything needed for authentication
     */
    void prepareAuthenticationContext();
}
