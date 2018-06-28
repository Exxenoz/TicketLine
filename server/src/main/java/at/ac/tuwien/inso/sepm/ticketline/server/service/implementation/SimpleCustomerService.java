package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.exception.CustomerValidationException;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.validator.CustomerValidator;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.customer.CustomerMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class SimpleCustomerService implements CustomerService {


    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public SimpleCustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerDTO save(CustomerDTO customerDTO) throws CustomerValidationException {
        LOGGER.info("Saving Customer {}", customerDTO);
        CustomerValidator.validateNewCustomer(customerDTO);
        var customer = customerMapper.customerDTOToCustomer(customerDTO);
        customer = customerRepository.save(customer);
        LOGGER.debug("Saved Customer {} successfully", customer);
        return customerMapper.customerToCustomerDTO(customer);
    }

    @Override
    public CustomerDTO update(CustomerDTO customerDTO) throws CustomerValidationException {
        LOGGER.info("Update Customer {}", customerDTO);
        CustomerValidator.validateExistingCustomer(customerDTO);
        var customer = customerMapper.customerDTOToCustomer(customerDTO);
        customer = customerRepository.save(customer);
        LOGGER.debug("Updated Customer {} successfully", customer);
        return customerMapper.customerToCustomerDTO(customer);
    }

    @Override
    public PageResponseDTO<CustomerDTO> findAll(Pageable pageable) {
        LOGGER.info("Get Page {} of Customers", pageable.getPageNumber());
        Page<Customer> customerPage = customerRepository.findAll(pageable);
        List<CustomerDTO> customerDTOList = customerMapper.customerToCustomerDTO(customerPage.getContent());
        return new PageResponseDTO<>(customerDTOList, customerPage.getTotalPages());
    }

    @Override
    public Customer findOneById(Long id) {
        LOGGER.info("Get Customer with id={}", id);
        return customerRepository.getOne(id);
    }
}
