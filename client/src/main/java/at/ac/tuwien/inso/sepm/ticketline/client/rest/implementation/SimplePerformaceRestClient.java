package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.PerformanceRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;

public class SimplePerformaceRestClient implements PerformanceRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RestClient restClient;
    private final URI performanceUri;

    public SimplePerformaceRestClient(RestClient restClient) {
        this.restClient = restClient;
        this.performanceUri = restClient.getServiceURI("/news");
    }

    @Override
    public List<PerformanceDTO> findAllPerformances() throws DataAccessException {

        try {
            LOGGER.debug("Retrieving all performances from {}", performanceUri);
            final var performance =
                restClient.exchange(
                    new RequestEntity<>(GET, performanceUri),
                    new ParameterizedTypeReference<List<PerformanceDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", performance.getStatusCode(), performance.getBody());
            return performance.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve performance with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
