package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.PerformanceRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.SearchDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;

@Component
public class SimplePerformanceRestClient implements PerformanceRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RestClient restClient;
    private final URI performanceUri;

    public SimplePerformanceRestClient(RestClient restClient) {
        this.restClient = restClient;
        this.performanceUri = restClient.getServiceURI("/performance");
    }

    @Override
    public List<PerformanceDTO> findAllPerformances() throws DataAccessException {

        try {
            LOGGER.debug("Retrieving all performances from {}", performanceUri + "/findAll");

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

    @Override
    public List<PerformanceDTO> findByEvent(EventDTO event) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving all performances of a specific event from {}", performanceUri + "/findByEvent");

            UriComponentsBuilder builder = UriComponentsBuilder.fromUri(restClient.getServiceURI("/performance/findByEvent"))
                .queryParam("name", event.getName())
                .queryParam("artists", event.getArtists())
                .queryParam("eventType", event.getEventType())
                .queryParam("description", event.getDescription());

            final var performance =
                restClient.exchange(
                    new RequestEntity<>(GET, builder.build().toUri()),
                    new ParameterizedTypeReference<List<PerformanceDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", performance.getStatusCode(), performance.getBody());
            return performance.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve performances with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<PerformanceDTO> search(SearchDTO search) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving all performances of a specific query from {}", performanceUri + "/performance/search");
            UriComponentsBuilder builder = UriComponentsBuilder.fromUri(restClient.getServiceURI("/performance/search"))
                .queryParam("performanceName", search.getPerformanceName())
                .queryParam("eventName", search.getEventName())
                .queryParam("artistFirstName", search.getArtistFirstName())
                .queryParam("artistLastname", search.getArtistLastName())
                .queryParam("eventType", search.getEventType())
                .queryParam("performanceStart", search.getPerformanceStart())
                .queryParam("price", search.getPrice())
                .queryParam("locationName", search.getLocationName())
                .queryParam("street", search.getStreet())
                .queryParam("city", search.getCity())
                .queryParam("country", search.getCountry())
                .queryParam("postalCode", search.getPostalCode())
                .queryParam("duration", search.getDuration());

            final var performance =
                restClient.exchange(
                    new RequestEntity<>(GET, builder.build().toUri()),
                    new ParameterizedTypeReference<List<PerformanceDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", performance.getStatusCode(), performance.getBody());
            return performance.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve performances with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
