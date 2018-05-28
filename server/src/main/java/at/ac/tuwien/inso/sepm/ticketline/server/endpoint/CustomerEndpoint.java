package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.exception.CustomerValidationException;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.endpoint.HttpBadRequestException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalBadRequestException;
import at.ac.tuwien.inso.sepm.ticketline.server.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(value = "/customer")
@Api(value = "customer")
public class CustomerEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final CustomerService customerService;

    public CustomerEndpoint(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/create")
    @ApiOperation("Add new customer")
    public CustomerDTO save(@RequestBody final CustomerDTO customerDTO) {
        try {
            return customerService.save(customerDTO);
        } catch (CustomerValidationException e) {
            throw new HttpBadRequestException();
        }
    }

    @PostMapping("/update")
    @ApiOperation("Add new customer")
    public CustomerDTO update(@RequestBody final CustomerDTO customerDTO) {
        try {
            return customerService.update(customerDTO);
        } catch (CustomerValidationException e) {
            throw new HttpBadRequestException();
        }
    }

    @PostMapping()
    @ApiOperation("Get page of customer entries")
    public PageResponseDTO<CustomerDTO> findAll(@RequestBody final PageRequestDTO pageRequestDTO) {
        return customerService.findAll(pageRequestDTO.getPageable());
    }
}
