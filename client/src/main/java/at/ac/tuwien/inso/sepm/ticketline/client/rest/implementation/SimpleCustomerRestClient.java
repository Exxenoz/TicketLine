package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.CustomerRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
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
    private final URI customerCreateUri;
    private final URI customerUpdateUri;

    public SimpleCustomerRestClient(RestClient restClient) {
        this.restClient = restClient;
        this.customerUri = restClient.getServiceURI("/customer");
        this.customerCreateUri = restClient.getServiceURI("/customer/create");
        this.customerUpdateUri = restClient.getServiceURI("/customer/update");
    }

    @Override
    public PageResponseDTO<CustomerDTO> findAll(PageRequestDTO pageRequestDTO) throws DataAccessException {
        try {
            LOGGER.info("Retrieving all Customers from {}", customerUri);
            final var customer =
                restClient.exchange(
                    new RequestEntity<>(pageRequestDTO, POST, customerUri),
                    new ParameterizedTypeReference<PageResponseDTO<CustomerDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", customer.getStatusCode(), customer.getBody());
            return customer.getBody();
        } catch (HttpStatusCodeException e) {
            LOGGER.error("A HTTP error occurred while getting Customers: {}", e.getStatusCode());
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()), e);
        } catch (RestClientException e) {
            LOGGER.error("An error occurred while getting Customers: {}", e.getMessage());
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public CustomerDTO create(CustomerDTO customerDTO) throws DataAccessException {
        try {
            LOGGER.info("Creating a customer with {}", customerCreateUri);
            final var customer =
                restClient.exchange(
                    new RequestEntity<>(customerDTO, POST, customerCreateUri),
                    new ParameterizedTypeReference<CustomerDTO>() {
                    });
            LOGGER.debug("Result status was {} with content {}", customer.getStatusCode(), customer.getBody());
            return customer.getBody();
        } catch (HttpStatusCodeException e) {
            LOGGER.error("A HTTP error occurred while creating a new Customer: {}", e.getStatusCode());
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()), e);
        } catch (RestClientException e) {
            LOGGER.error("An error occurred while creating a new Customer: {}", e.getMessage());
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public CustomerDTO update(CustomerDTO customerDTO) throws DataAccessException {
        try {
            LOGGER.info("Updating a Customer with {}", customerUpdateUri);
            final var customer =
                restClient.exchange(
                    new RequestEntity<>(customerDTO, POST, customerUpdateUri),
                    new ParameterizedTypeReference<CustomerDTO>() {
                    });
            LOGGER.debug("Result status was {} with content {}", customer.getStatusCode(), customer.getBody());
            return customer.getBody();
        } catch (HttpStatusCodeException e) {
            LOGGER.error("A HTTP error occurred while updating a Customer: {}", e.getStatusCode());
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()), e);
        } catch (RestClientException e) {
            LOGGER.error("An error occurred while updating a Customer: {}", e.getMessage());
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
