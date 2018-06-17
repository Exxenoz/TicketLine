package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.PerformanceRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
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
import java.time.format.DateTimeFormatter;

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
    public PageResponseDTO<PerformanceDTO> findByEventID(Long eventID, PageRequestDTO pageRequestDTO) throws DataAccessException {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUri(restClient.getServiceURI("/performance/findByEventID/" + eventID))
                .queryParam("page", pageRequestDTO.getPage())
                .queryParam("size", pageRequestDTO.getSize());

            URI uri = builder.build().toUri();

            final var performance =
                restClient.exchange(
                    new RequestEntity<>(GET, uri),
                    new ParameterizedTypeReference<PageResponseDTO<PerformanceDTO>>() {
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
    public PageResponseDTO<PerformanceDTO> findAllPerformances(PageRequestDTO pageRequestDTO) throws DataAccessException {
        try {
            URI uri = null;
            if (pageRequestDTO.getSortColumnName() != null && pageRequestDTO.getSortDirection() != null) {
                UriComponentsBuilder builder = UriComponentsBuilder.fromUri(restClient.getServiceURI("/performance/"))
                    .queryParam("page", pageRequestDTO.getPage())
                    .queryParam("size", pageRequestDTO.getSize())
                    .queryParam("sort", pageRequestDTO.getSortColumnName()
                        + "," + pageRequestDTO.getSortDirection().toString().toLowerCase());
                uri = builder.build().toUri();
            } else {
                UriComponentsBuilder builder = UriComponentsBuilder.fromUri(restClient.getServiceURI("/performance/"))
                    .queryParam("page", pageRequestDTO.getPage())
                    .queryParam("size", pageRequestDTO.getSize());

                uri = builder.build().toUri();
            }

            LOGGER.debug("Entering findAllPerformances method with URI {}", uri);
            final var performance =
                restClient.exchange(
                    new RequestEntity<>(GET, uri),
                    new ParameterizedTypeReference<PageResponseDTO<PerformanceDTO>>() {
                    }
                );

            LOGGER.debug("Result status was {} with content {}", performance.getStatusCode(), performance.getBody());
            return performance.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()));
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public PageResponseDTO<PerformanceDTO> findAll(SearchDTO search, PageRequestDTO pageRequestDTO) throws DataAccessException {
        try {
            UriComponentsBuilder builder;
            if(search.getPerformanceStart() != null) {
                builder = UriComponentsBuilder.fromUri(restClient.getServiceURI("/performance/search/"))
                    .queryParam("performanceName", search.getPerformanceName())
                    .queryParam("eventName", search.getEventName())
                    .queryParam("firstName", search.getFirstName())
                    .queryParam("lastName", search.getLastName())
                    .queryParam("eventType", search.getEventType())
                    .queryParam("performanceStart", search.getPerformanceStart().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm")))
                    .queryParam("price", search.getPrice())
                    .queryParam("locationName", search.getLocationName())
                    .queryParam("street", search.getStreet())
                    .queryParam("city", search.getCity())
                    .queryParam("country", search.getCountry())
                    .queryParam("postalCode", search.getPostalCode())
                    .queryParam("duration", search.getDuration())
                    .queryParam("page", pageRequestDTO.getPage())
                    .queryParam("size", pageRequestDTO.getSize());
            } else
            {
                builder = UriComponentsBuilder.fromUri(restClient.getServiceURI("/performance/search/"))
                    .queryParam("performanceName", search.getPerformanceName())
                    .queryParam("eventName", search.getEventName())
                    .queryParam("firstName", search.getFirstName())
                    .queryParam("lastName", search.getLastName())
                    .queryParam("eventType", search.getEventType())
                    .queryParam("performanceStart", search.getPerformanceStart())
                    .queryParam("price", search.getPrice())
                    .queryParam("locationName", search.getLocationName())
                    .queryParam("street", search.getStreet())
                    .queryParam("city", search.getCity())
                    .queryParam("country", search.getCountry())
                    .queryParam("postalCode", search.getPostalCode())
                    .queryParam("duration", search.getDuration())
                    .queryParam("page", pageRequestDTO.getPage())
                    .queryParam("size", pageRequestDTO.getSize());
            }

            URI uri = builder.build().toUri();

            final var performance =
                restClient.exchange(
                    new RequestEntity<>(GET, uri),
                    new ParameterizedTypeReference<PageResponseDTO<PerformanceDTO>>() {
                    }
                );

            LOGGER.debug("Result status was {} with content {}", performance.getStatusCode(), performance.getBody());
            return performance.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve performances with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
