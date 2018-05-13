package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.UserRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.exception.UserValidatorException;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Component
public class SimpleUserRestClient implements UserRestClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RestClient restClient;
    private final URI getUsersUri;
    private final URI enableUserUri;

    public SimpleUserRestClient(RestClient restClient) {
        this.restClient = restClient;
        this.getUsersUri = restClient.getServiceURI("/users");
        this.enableUserUri = restClient.getServiceURI("/users/enable");
    }

    @Override
    public void enableUser(UserDTO user) throws DataAccessException {
        try {
            UserValidator.validateUser(user);
            LOGGER.info("Enable User {} on {}", user.getUsername(), enableUserUri);
            final var response = restClient.exchange(new RequestEntity<>(user, POST, enableUserUri),
                new ParameterizedTypeReference<UserDTO>() {
                });
            LOGGER.debug("Result status was {} with content {}", response.getStatusCode(), response.getBody());
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()));
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.internal"));
        } catch (UserValidatorException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public List<UserDTO> findAll() throws DataAccessException {
        try {
            LOGGER.info("Retrieving all Users from {}", getUsersUri);
            final var response = restClient.exchange(new RequestEntity<>(GET, getUsersUri),
                new ParameterizedTypeReference<List<UserDTO>>() {
                });
            LOGGER.debug("Result status was {} with content {}", response.getStatusCode(), response.getBody());
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.internal"));
        }
    }
}
