package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.customer;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    List<CustomerDTO> customerToCustomerDTO(List<Customer> all);

    List<Customer> customerDTOToCustomer(List<CustomerDTO> all);
}
