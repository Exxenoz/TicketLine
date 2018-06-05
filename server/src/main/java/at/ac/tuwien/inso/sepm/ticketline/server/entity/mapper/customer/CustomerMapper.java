package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.customer;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.reservation.SeatIdsToSeatsMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDTO customerToCustomerDTO(Customer customer);

    Customer customerDTOToCustomer(CustomerDTO customer);

    List<CustomerDTO> customerToCustomerDTO(List<Customer> all);

    List<Customer> customerDTOToCustomer(List<CustomerDTO> all);
}
