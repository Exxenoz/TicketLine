package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.UserRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserCreateRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserPasswordChangeRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserPasswordResetRequestDTO;
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
    private final URI resetUserPasswordUri;
    private final URI changeUserPasswordUri;

    public SimpleUserRestClient(RestClient restClient) {
        this.restClient = restClient;
        this.getAllUsersUri = restClient.getServiceURI("/users/all");
        this.getUsersUri = restClient.getServiceURI("/users");
        this.enableUserUri = restClient.getServiceURI("/users/enable");
        this.disableUserUri = restClient.getServiceURI("/users/disable");
        this.createUserUri = restClient.getServiceURI("/users/create");
        this.resetUserPasswordUri = restClient.getServiceURI("/users/password/reset");
        this.changeUserPasswordUri = restClient.getServiceURI("/users/password/change");
    }

    @Override
    public void enableUser(UserDTO user) throws DataAccessException {
        try {
            LOGGER.info("Enable User {} on {}", user.getUsername(), enableUserUri);
            final var response = restClient.exchange(new RequestEntity<>(user, POST, enableUserUri),
                new ParameterizedTypeReference<UserDTO>() {
                });
            LOGGER.debug("Result status was {} with content {}", response.getStatusCode(), response.getBody());
        } catch (HttpStatusCodeException e) {
            LOGGER.error("A HTTP error occurred while enabling the User: {}", e.getStatusCode());
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()), e);
        } catch (RestClientException e) {
            LOGGER.error("An error occurred while enabling the User: {}", e.getMessage());
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.internal"));
        }
    }

    @Override
    public void disableUser(UserDTO user) throws DataAccessException {
        try {
            LOGGER.info("Disable User {} on {}", user.getUsername(), disableUserUri);
            final var response = restClient.exchange(new RequestEntity<>(user, POST, disableUserUri),
                new ParameterizedTypeReference<UserDTO>() {
                });
            LOGGER.debug("Result status was {} with content {}", response.getStatusCode(), response.getBody());
        } catch (HttpStatusCodeException e) {
            LOGGER.error("A HTTP error occurred while disabling User: {}", e.getStatusCode());
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()), e);
        } catch (RestClientException e) {
            LOGGER.error("An error occurred while disabling User: {}", e.getMessage());
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.internal"));
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
            LOGGER.error("A HTTP error occurred while retrieving Users: {}", e.getStatusCode());
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()), e);
        } catch (RestClientException e) {
            LOGGER.error("An error occurred while retrieving Users: {}", e.getMessage());
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
            LOGGER.error("A HTTP error occurred while retrieving Users: {}", e.getStatusCode());
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()), e);
        } catch (RestClientException e) {
            LOGGER.error("An error occurred while retrieving Users: {}", e.getMessage());
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.internal"));
        }
    }

    @Override
    public UserDTO create(UserCreateRequestDTO userCreateRequestDTO) throws DataAccessException {
        try {
            LOGGER.info("Creating a user with {}", createUserUri);
            final var user =
                restClient.exchange(
                    new RequestEntity<>(userCreateRequestDTO, POST, createUserUri),
                    new ParameterizedTypeReference<UserDTO>() {
                    });
            LOGGER.debug("Result status was {} with content {}", user.getStatusCode(), user.getBody());
            return user.getBody();
        } catch (HttpStatusCodeException e) {
            LOGGER.error("A HTTP error occurred while creating Users: {}", e.getStatusCode());
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()), e);
        } catch (RestClientException e) {
            LOGGER.error("An error occurred while creating Users: {}", e.getMessage());
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.internal"));
        }
    }

    @Override
    public UserDTO resetPassword(UserPasswordResetRequestDTO userPasswordResetRequestDTO) throws DataAccessException {
        try {
            LOGGER.info("Resetting password of user with {}", resetUserPasswordUri);
            final var user =
                restClient.exchange(
                    new RequestEntity<>(userPasswordResetRequestDTO, POST, resetUserPasswordUri),
                    new ParameterizedTypeReference<UserDTO>() {
                    });
            LOGGER.debug("Result status was {} with content {}", user.getStatusCode(), user.getBody());
            return user.getBody();
        } catch (HttpStatusCodeException e) {
            LOGGER.error("A HTTP error occurred while resetting User: {}", e.getStatusCode());
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()), e);
        } catch (RestClientException e) {
            LOGGER.error("An error occurred while resetting Users: {}", e.getMessage());
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.internal"));
        }
    }

    @Override
    public void changePassword(UserPasswordChangeRequestDTO userPasswordChangeRequestDTO) throws DataAccessException {
        try {
            LOGGER.info("Changing password of user with {}", changeUserPasswordUri);
            final var user =
                restClient.exchange(
                    new RequestEntity<>(userPasswordChangeRequestDTO, POST, changeUserPasswordUri),
                    new ParameterizedTypeReference<UserPasswordChangeRequestDTO>() {
                    });
            LOGGER.debug("Result status was {} with content {}", user.getStatusCode(), user.getBody());
        } catch (HttpStatusCodeException e) {
            LOGGER.error("A HTTP error occurred while changing password: {}", e.getStatusCode());
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()), e);
        } catch (RestClientException e) {
            LOGGER.error("An error occurred while changing password: {}", e.getMessage());
            throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.internal"));
        }
    }
}
