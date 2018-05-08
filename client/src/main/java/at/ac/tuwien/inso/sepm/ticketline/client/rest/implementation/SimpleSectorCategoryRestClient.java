package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.SectorCategoryRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.sector.SectorCategoryDTO;
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

@Component
public class SimpleSectorCategoryRestClient implements SectorCategoryRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RestClient restClient;
    private final URI sectorCategoryUri;

    public SimpleSectorCategoryRestClient(RestClient restClient) {
        this.restClient = restClient;
        this.sectorCategoryUri = restClient.getServiceURI("/sector_category");
    }

    @Override
    public List<SectorCategoryDTO> findAllOrderByBasePriceModAsc() throws DataAccessException {
        try {
            LOGGER.debug("Retrieving all sector categories from {}", sectorCategoryUri);
            final var sectorCategories =
                restClient.exchange(
                    new RequestEntity<>(GET, sectorCategoryUri),
                    new ParameterizedTypeReference<List<SectorCategoryDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", sectorCategories.getStatusCode(), sectorCategories.getBody());
            return sectorCategories.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve sector categories with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
