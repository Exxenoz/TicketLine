package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {

    /**
     * Create or update a customer
     * @param customer customer to add or update
     * @return customer with its id
     */
    Customer save(Customer customer);

    /**
     * Find all customer entries
     *
     * @param pageable page filter
     * @return page of customers
     */
    Page<Customer> findAll(Pageable pageable);
}
