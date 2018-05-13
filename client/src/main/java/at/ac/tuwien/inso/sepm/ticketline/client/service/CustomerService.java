package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    /**
     * Find all customer entries.
     *
     * @param pageable data for pagination
     * @return list of customer entries
     * @throws DataAccessException in case something went wrong
     */
    Page<CustomerDTO> findAll(Pageable pageable) throws DataAccessException;
}
