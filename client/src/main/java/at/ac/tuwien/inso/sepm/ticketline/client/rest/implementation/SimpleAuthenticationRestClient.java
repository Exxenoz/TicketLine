package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.AuthenticationRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationRequest;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationToken;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationTokenInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.lang.invoke.MethodHandles;
import java.net.URI;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Component
public class SimpleAuthenticationRestClient implements AuthenticationRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RestClient restClient;
    private final URI authenticationUri;
    private final URI authenticationInfoUri;

    public SimpleAuthenticationRestClient(RestClient restClient) {
        this.restClient = restClient;
        this.authenticationUri = restClient.getServiceURI("/authentication");
        this.authenticationInfoUri = restClient.getServiceURI("/authentication/info");
    }

    @Override
    public AuthenticationToken authenticate(final AuthenticationRequest authenticationRequest) throws DataAccessException {
        try {
            LOGGER.info("Authenticate {} at {}", authenticationRequest.getUsername(), authenticationUri);
            final var response =
                restClient.exchange(
                    new RequestEntity<>(authenticationRequest, POST, authenticationUri),
                    new ParameterizedTypeReference<AuthenticationToken>() {
                    });
            LOGGER.debug("Authenticate {} status {}", authenticationRequest.getUsername(), response.getStatusCode());
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()));
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.internal"));
        }
    }

    @Override
    public AuthenticationToken authenticate() throws DataAccessException {
        try {
            LOGGER.info("Get AuthenticationToken at {}", authenticationUri);
            final var response =
                restClient.exchange(
                    new RequestEntity<>(GET, authenticationUri),
                    new ParameterizedTypeReference<AuthenticationToken>() {
                    });
            LOGGER.debug("Get AuthenticationToken status {}", response.getStatusCode());
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()));
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.internal"));
        }
    }

    @Override
    public AuthenticationTokenInfo tokenInfoCurrent() throws DataAccessException {
        try {
            LOGGER.info("Get AuthenticationTokenInfo at {}", authenticationInfoUri);
            final var response =
                restClient.exchange(
                    new RequestEntity<>(GET, authenticationInfoUri),
                    new ParameterizedTypeReference<AuthenticationTokenInfo>() {
                    });
            LOGGER.debug("Get AuthenticationTokenInfo status {}", response.getStatusCode());
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()));
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.internal"));
        }
    }
}
