package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import org.springframework.data.domain.Page;

public interface CustomerRestClient {

    /**
     * Find all customer entries.
     *
     * @param pageRequestDTO data for pagination
     * @return page response, including list of customer entries
     * @throws DataAccessException in case something went wrong
     */
    PageResponseDTO<CustomerDTO> findAll(PageRequestDTO pageRequestDTO) throws DataAccessException;
}
