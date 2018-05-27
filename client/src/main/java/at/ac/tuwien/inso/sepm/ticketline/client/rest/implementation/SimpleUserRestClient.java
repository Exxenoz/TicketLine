package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.UserRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.exception.UserValidatorException;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
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
    private final URI getAllUsersUri;
    private final URI enableUserUri;
    private final URI disableUserUri;
    private final URI createUserUri;

    public SimpleUserRestClient(RestClient restClient) {
        this.restClient = restClient;
        this.getAllUsersUri = restClient.getServiceURI("/users/all");
        this.getUsersUri = restClient.getServiceURI("/users");
        this.enableUserUri = restClient.getServiceURI("/users/enable");
        this.disableUserUri = restClient.getServiceURI("/users/disable");
        this.createUserUri = restClient.getServiceURI("/users/create");
    }

    @Override
    public void enableUser(UserDTO user) throws DataAccessException {
        try {
            UserValidator.validateExistingUser(user);
            LOGGER.info("Enable User {} on {}", user.getUsername(), enableUserUri);
            final var response = restClient.exchange(new RequestEntity<>(user, POST, enableUserUri),
                new ParameterizedTypeReference<UserDTO>() {
                });
            LOGGER.debug("Result status was {} with content {}", response.getStatusCode(), response.getBody());
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.internal"));
        } catch (UserValidatorException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void disableUser(UserDTO user) throws DataAccessException {
        try {
            UserValidator.validateExistingUser(user);
            LOGGER.info("Disable User {} on {}", user.getUsername(), disableUserUri);
            final var response = restClient.exchange(new RequestEntity<>(user, POST, disableUserUri),
                new ParameterizedTypeReference<UserDTO>() {
                });
            LOGGER.debug("Result status was {} with content {}", response.getStatusCode(), response.getBody());
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.internal"));
        } catch (UserValidatorException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public List<UserDTO> findAll() throws DataAccessException {
        try {
            LOGGER.info("Retrieving all Users from {}", getAllUsersUri);
            final var response = restClient.exchange(new RequestEntity<>(GET, getAllUsersUri),
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

    @Override
    public PageResponseDTO<UserDTO> findAll(PageRequestDTO request) throws DataAccessException {
        try {
            LOGGER.info("Retrieving all Users from {}", getUsersUri);
            final var response = restClient.exchange(new RequestEntity<>(request, POST, getUsersUri),
                new ParameterizedTypeReference<PageResponseDTO<UserDTO>>() {
                });
            LOGGER.debug("Result status was {} with content {}", response.getStatusCode(), response.getBody());
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()), e);
        } catch (RestClientException e) {
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.internal"));
        }
    }

    @Override
    public UserDTO create(UserDTO userDTO) throws DataAccessException {
        try {
            LOGGER.debug("Creating a user with {}", createUserUri);
            final var user =
                restClient.exchange(
                    new RequestEntity<>(userDTO, POST, createUserUri),
                    new ParameterizedTypeReference<UserDTO>() {
                    });
            LOGGER.debug("Result status was {} with content {}", user.getStatusCode(), user.getBody());
            return user.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()), e);
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
