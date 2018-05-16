package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.EventRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventFilterTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
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
import static org.springframework.http.HttpMethod.POST;

@Component
public class SimpleEventRestClient implements EventRestClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RestClient restClient;
    private final URI eventUri;
    private final URI topTenUri;

    public SimpleEventRestClient(RestClient restClient) {
        this.restClient = restClient;
        this.eventUri = restClient.getServiceURI("/event");
        this.topTenUri = restClient.getServiceURI("/event/top_ten");
    }

    @Override
    public List<EventDTO> findAll() throws DataAccessException {
        try {
            LOGGER.debug("Retrieving all events from {}", eventUri);
            final var event =
                restClient.exchange(
                    new RequestEntity<>(GET, eventUri),
                    new ParameterizedTypeReference<List<EventDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", event.getStatusCode(), event.getBody());
            return event.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve event with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public EventDTO findByPerformanceID(Long performanceID) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving event of a specific performance from {}", eventUri);

            URI uri = restClient.getServiceURI("/event/findByPerformanceID" + performanceID);

            final var event =
                restClient.exchange(
                    new RequestEntity<>(GET, uri),
                    new ParameterizedTypeReference<EventDTO>() {
                    });
            LOGGER.debug("Result status was {} with content {}", event.getStatusCode(), event.getBody());
            return event.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve events with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<EventDTO> findTop10ByPaidReservationCountByFilter(EventFilterTopTenDTO eventFilterTopTen) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving top 10 events by sales from month: {}", topTenUri);
            final var event =
                restClient.exchange(
                    new RequestEntity<>(eventFilterTopTen, POST, topTenUri),
                    new ParameterizedTypeReference<List<EventDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", event.getStatusCode(), event.getBody());
            return event.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve events with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
