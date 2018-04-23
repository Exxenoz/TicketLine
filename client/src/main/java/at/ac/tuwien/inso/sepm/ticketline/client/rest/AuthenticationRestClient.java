package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationRequest;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationToken;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationTokenInfo;

public interface AuthenticationRestClient {

    /**
     * Authenticate with username and password.
     *
     * @param authenticationRequest containing the username and password to authenticate
     * @return A valid authentication token
     * @throws DataAccessException in case something went wrong
     */
    AuthenticationToken authenticate(AuthenticationRequest authenticationRequest) throws DataAccessException;

    /**
     * Renew authentication with the current authentication token.
     *
     * @return A valid authentication token
     * @throws DataAccessException in case something went wrong
     */
    AuthenticationToken authenticate() throws DataAccessException;

    /**
     * @return Informations about the current authentication
     * @throws DataAccessException in case something went wrong
     */
    AuthenticationTokenInfo tokenInfoCurrent() throws DataAccessException;

}
