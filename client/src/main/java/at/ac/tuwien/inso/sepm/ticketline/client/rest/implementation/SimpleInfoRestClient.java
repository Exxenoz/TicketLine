package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.InfoRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.info.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.lang.invoke.MethodHandles;
import java.net.URI;

import static org.springframework.http.HttpMethod.GET;

@Component
public class SimpleInfoRestClient implements InfoRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RestClient restClient;
    private final URI infoUri;

    public SimpleInfoRestClient(RestClient restClient) {
        this.restClient = restClient;
        this.infoUri = restClient.getServiceURI("/info");
    }

    @Override
    public Info find() throws DataAccessException {
        try {
            LOGGER.info("Retrieving server info from {}", infoUri);
            final var info =
                restClient.exchange(
                    new RequestEntity<>(GET, infoUri),
                    new ParameterizedTypeReference<Info>() {
                    });
            LOGGER.debug("Result status was {} with content {}", info.getStatusCode(), info.getBody());
            return info.getBody();
        } catch (HttpStatusCodeException e) {
            LOGGER.error("A HTTP error occurred while trying to get the ServerInfo: {}", e.getStatusCode());
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()), e);
        } catch (RestClientException e) {
            LOGGER.error("An error occurred while trying to get the ServerInfo: {}", e.getMessage());
            throw new DataAccessException(e.getMessage(), e);
        }
    }

}
