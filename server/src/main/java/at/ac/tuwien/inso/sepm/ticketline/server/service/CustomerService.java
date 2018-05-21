package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    /**
     * Find all customer entries
     *
     * @param pageable page filter
     * @return page of customers
     */
    Page<Customer> findAll(Pageable pageable);

    Customer findOneById(Long id);
}
