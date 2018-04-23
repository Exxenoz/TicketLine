package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.InfoRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.info.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

@Component
public class SimpleInfoRestClient implements InfoRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleInfoRestClient.class);
    private static final String INFO_URL = "/info";

    private final RestClient restClient;

    public SimpleInfoRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public Info find() throws DataAccessException {
        try {
            LOGGER.debug("Retrieving server info from {}", restClient.getServiceURI(INFO_URL));
            ResponseEntity<Info> info =
                restClient.exchange(
                    restClient.getServiceURI(INFO_URL),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Info>() {
                    });
            LOGGER.debug("Result status was {} with content {}", info.getStatusCode(), info.getBody());
            return info.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve news with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

}
