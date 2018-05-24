package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.exception.CustomerValidationException;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import org.springframework.data.domain.Pageable;


public interface CustomerService {

    /**
     * Create a new customer
     * @param customerDTO customer to create
     * @return customer with its id
     * @throws CustomerValidationException in case customer was invalid
     */
    CustomerDTO save(CustomerDTO customerDTO) throws CustomerValidationException;

    /**
     * Update a customer with the specified data transfer object.
     *
     * @param customerDTO the customer to update
     * @return the updated customer
     * @throws CustomerValidationException in case customer was invalid
     */
    CustomerDTO update(CustomerDTO customerDTO) throws CustomerValidationException;;

    /**
     * Find all customer entries
     * @param pageable page filter
     * @return page response of customers
     */
    PageResponseDTO<CustomerDTO> findAll(Pageable pageable);

    Customer findOneById(Long id);
}
