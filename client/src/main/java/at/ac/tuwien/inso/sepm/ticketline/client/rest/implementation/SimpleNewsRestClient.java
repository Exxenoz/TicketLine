package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.NewsRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
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
public class SimpleNewsRestClient implements NewsRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RestClient restClient;
    private final URI newsUri;

    public SimpleNewsRestClient(RestClient restClient) {
        this.restClient = restClient;
        this.newsUri = restClient.getServiceURI("/news");
    }

    @Override
    public List<SimpleNewsDTO> findAll() throws DataAccessException {
        try {
            LOGGER.debug("Retrieving all news from {}", newsUri);
            final var news =
                restClient.exchange(
                    new RequestEntity<>(GET, newsUri),
                    new ParameterizedTypeReference<List<SimpleNewsDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", news.getStatusCode(), news.getBody());
            return news.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve news with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public void publish(DetailedNewsDTO detailedNewsDTO) throws DataAccessException {
        try {
            LOGGER.debug("Publish news with {}", newsUri);
            final var news =
                restClient.exchange(
                    new RequestEntity<>(detailedNewsDTO, POST, newsUri),
                    new ParameterizedTypeReference<SimpleNewsDTO>() {
                    });
            LOGGER.debug("Result status was {} with content {}", news.getStatusCode(), news.getBody());
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed to publish news with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
