package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.CustomerRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.exception.CustomerValidationException;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.validator.CustomerValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class SimpleCustomerService implements CustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final CustomerRestClient customerRestClient;

    public SimpleCustomerService(CustomerRestClient customerRestClient) {
        this.customerRestClient = customerRestClient;
    }

    @Override
    public PageResponseDTO<CustomerDTO> findAll(PageRequestDTO pageRequestDTO) throws DataAccessException {
        return customerRestClient.findAll(pageRequestDTO);
    }

    @Override
    public CustomerDTO create(CustomerDTO customerDTO) throws DataAccessException {
        try {
            CustomerValidator.validateNewCustomer(customerDTO);
        } catch (CustomerValidationException e) {
            throw new DataAccessException(e.getMessage());
        }
        return customerRestClient.create(customerDTO);
    }

    @Override
    public CustomerDTO update(CustomerDTO customerDTO) throws DataAccessException {
        try {
            CustomerValidator.validateExistingCustomer(customerDTO);
        } catch (CustomerValidationException e) {
            throw new DataAccessException(e.getMessage());
        }
        return customerRestClient.update(customerDTO);
    }
}
