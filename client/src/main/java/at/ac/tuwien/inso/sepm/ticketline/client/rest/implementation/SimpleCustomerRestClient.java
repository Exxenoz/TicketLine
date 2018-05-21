package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.CustomerRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.lang.invoke.MethodHandles;
import java.net.URI;

import static org.springframework.http.HttpMethod.POST;

@Component
public class SimpleCustomerRestClient implements CustomerRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RestClient restClient;
    private final URI customerUri;

    public SimpleCustomerRestClient(RestClient restClient) {
        this.restClient = restClient;
        this.customerUri = restClient.getServiceURI("/customer");
    }

    @Override
    public PageResponseDTO<CustomerDTO> findAll(PageRequestDTO pageRequestDTO) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving all customers from {}", customerUri);
            final var customer =
                restClient.exchange(
                    new RequestEntity<>(pageRequestDTO, POST, customerUri),
                    new ParameterizedTypeReference<PageResponseDTO<CustomerDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", customer.getStatusCode(), customer.getBody());
            return customer.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve customers with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
