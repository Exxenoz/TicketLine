package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.AuthenticationRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationRequest;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationToken;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationTokenInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

@Component
public class SimpleAuthenticationRestClient implements AuthenticationRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleAuthenticationRestClient.class);
    private static final String AUTHENTICATION_URL = "/authentication";
    private static final String AUTHENTICATION_INFO_URL = AUTHENTICATION_URL + "/info";

    private final RestClient restClient;

    public SimpleAuthenticationRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public AuthenticationToken authenticate(final AuthenticationRequest authenticationRequest) throws DataAccessException {
        try {
            LOGGER.info("Authenticate {} at {}", authenticationRequest.getUsername(), restClient.getServiceURI(AUTHENTICATION_URL));
            ResponseEntity<AuthenticationToken> response =
                restClient.exchange(
                    restClient.getServiceURI(AUTHENTICATION_URL),
                    HttpMethod.POST,
                    new HttpEntity<Object>(authenticationRequest),
                    new ParameterizedTypeReference<AuthenticationToken>() {
                    });
            LOGGER.debug("Authenticate {} status {}", authenticationRequest.getUsername(), response.getStatusCode());
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed to login with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public AuthenticationToken authenticate() throws DataAccessException {
        try {
            LOGGER.info("Get AuthenticationToken at {}", restClient.getServiceURI(AUTHENTICATION_URL));
            ResponseEntity<AuthenticationToken> response =
                restClient.exchange(
                    restClient.getServiceURI(AUTHENTICATION_URL),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<AuthenticationToken>() {
                    });
            LOGGER.debug("Get AuthenticationToken status {}", response.getStatusCode());
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed to obtain authentication token with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public AuthenticationTokenInfo tokenInfoCurrent() throws DataAccessException {
        try {
            LOGGER.info("Get AuthenticationTokenInfo at {}", restClient.getServiceURI(AUTHENTICATION_INFO_URL));
            ResponseEntity<AuthenticationTokenInfo> response =
                restClient.exchange(
                    restClient.getServiceURI(AUTHENTICATION_INFO_URL),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<AuthenticationTokenInfo>() {
                    });
            LOGGER.debug("Get AuthenticationTokenInfo status {}", response.getStatusCode());
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed to authentication token info with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
