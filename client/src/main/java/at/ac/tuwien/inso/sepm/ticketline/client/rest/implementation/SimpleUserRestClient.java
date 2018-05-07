package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.UserRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
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
    private final URI userUri;

    public SimpleUserRestClient(RestClient restClient) {
        this.restClient = restClient;
        this.userUri = restClient.getServiceURI("/users");
    }

    @Override
    public void enableUser(UserDTO user) throws DataAccessException {
        try {
            LOGGER.info("Enable User {} on {}", user.getUsername(), userUri);
            final var response = restClient.exchange(new RequestEntity<>(user, POST, userUri),
                new ParameterizedTypeReference<UserDTO>() {
                });
            LOGGER.debug("Result status was {} with content {}", response.getStatusCode(), response.getBody());
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve news with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<UserDTO> findAll() throws DataAccessException {
        try {
            LOGGER.info("Retrieving all Users from {}", userUri);
            final var response = restClient.exchange(new RequestEntity<>(GET, userUri),
                new ParameterizedTypeReference<List<UserDTO>>() {
                });
            LOGGER.debug("Result status was {} with content {}", response.getStatusCode(), response.getBody());
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve news with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
