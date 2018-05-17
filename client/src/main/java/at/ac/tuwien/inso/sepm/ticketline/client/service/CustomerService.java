package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;

public interface CustomerService {

    /**
     * Find all customer entries.
     *
     * @param pageRequestDTO data for pagination
     * @return page response, including list of customer entries
     * @throws DataAccessException in case something went wrong
     */
    PageResponseDTO<CustomerDTO> findAll(PageRequestDTO pageRequestDTO) throws DataAccessException;

    /**
     * Create a customer with the specified data transfer object.
     *
     * @param customerDTO the customer to create
     * @return the created customer
     * @throws DataAccessException in case something went wrong
     */
    CustomerDTO create(CustomerDTO customerDTO) throws DataAccessException;
}
