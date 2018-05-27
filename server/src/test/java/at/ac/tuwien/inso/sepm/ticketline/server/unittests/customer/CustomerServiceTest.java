package at.ac.tuwien.inso.sepm.ticketline.server.unittests.customer;

import at.ac.tuwien.inso.sepm.ticketline.rest.address.BaseAddressDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.exception.CustomerValidationException;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.BaseAddress;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.CustomerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("unit-test")
public class CustomerServiceTest {

    /**
     * @BeforeClass does not work with @Autowired and static members
     * so we have to use some dirty tricks
     */
    private boolean dataLoaded = false;

    @Autowired
    CustomerService service;

    @Autowired
    public CustomerRepository repository;

    @Before
    public void setup() {
        if(!dataLoaded) {
            dataLoaded = true;

            BaseAddress address = new BaseAddress(
                "",
                "",
                "",
                "");

            Customer customer = new Customer(
                "Flo",
                "Florian",
                "+43 699 1111 1111",
                "",
                address
            );

            repository.save(customer);
        }
    }

    @Test
    public void saveWithCorrectParametersShouldPersist() throws CustomerValidationException {
        BaseAddressDTO address = new BaseAddressDTO(
            "Ring",
            "Vienna",
            "Austria",
            "1010");

        CustomerDTO newCustomer = new CustomerDTO(
            "Flooo",
            "Floriannn",
            "0699/1111 2222",
            "",
            address);

        newCustomer = service.save(newCustomer);

        Assert.assertNotNull(newCustomer.getId());
        // get all customer entries
        PageRequestDTO pageRequestDTO = new PageRequestDTO(0, Integer.MAX_VALUE,  Sort.Direction.ASC, null);
        PageResponseDTO<CustomerDTO> pageResponse = service.findAll(pageRequestDTO.getPageable());
        List<CustomerDTO> customers = pageResponse.getContent();

        CustomerDTO dbCustomer = null;
        for(CustomerDTO customer : customers) {
            if(customer.getId() == newCustomer.getId()) {
                dbCustomer = customer;
            }
        }

        Assert.assertNotNull(dbCustomer);
        Assert.assertEquals(newCustomer.getFirstName(), dbCustomer.getFirstName());
        Assert.assertEquals(newCustomer.getLastName(), dbCustomer.getLastName());
        Assert.assertEquals(newCustomer.getTelephoneNumber(), dbCustomer.getTelephoneNumber());
        Assert.assertEquals(newCustomer.getEmail(), dbCustomer.getEmail());
        Assert.assertEquals(newCustomer.getBaseAddress(), dbCustomer.getBaseAddress());
    }

    @Test
    public void updateWithCorrectParametersShouldPersist() throws CustomerValidationException {
        // fetch an existing customer to update
        PageRequestDTO pageRequestDTO = new PageRequestDTO(0, Integer.MAX_VALUE,  Sort.Direction.ASC, null);
        PageResponseDTO<CustomerDTO> pageResponse = service.findAll(pageRequestDTO.getPageable());
        List<CustomerDTO> customers = pageResponse.getContent();

        CustomerDTO dbCustomer = null;
        for(CustomerDTO customer : customers) {
            if(customer.getFirstName().equals("Flo")) {
                dbCustomer = customer;
            }
        }

        dbCustomer.setFirstName("UpdatedFlo");

        service.update(dbCustomer);

        // look for updated customer
        pageResponse = service.findAll(pageRequestDTO.getPageable());
        List<CustomerDTO> updatedCustomers = pageResponse.getContent();

        CustomerDTO updatedCustomer = null;
        for(CustomerDTO customer : customers) {
            if(customer.getId() == dbCustomer.getId()) {
                updatedCustomer = customer;
            }
        }

        Assert.assertNotNull(updatedCustomer);
        Assert.assertEquals(dbCustomer.getFirstName(), updatedCustomer.getFirstName());
    }
}
