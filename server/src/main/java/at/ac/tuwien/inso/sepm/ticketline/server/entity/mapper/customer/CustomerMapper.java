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

    /**
     * Converts a customer entity object to a customer DTO
     *
     * @param customer the object to be converted
     * @return the converted DTO
     */
    CustomerDTO customerToCustomerDTO(Customer customer);

    /**
     * Converts a customer DTO to a customer entity object
     *
     * @param customer the object to be converted
     * @return the converted entity object
     */
    Customer customerDTOToCustomer(CustomerDTO customer);

    /**
     * Converts a list of customer entity objects to a list of customer DTOs
     *
     * @param all the list to be converted
     * @return the converted list
     */
    List<CustomerDTO> customerToCustomerDTO(List<Customer> all);

    /**
     * Converts a list of customer DTOs to a list of customer entity objects
     *
     * @param all the list to be converted
     * @return the converted list
     */
    List<Customer> customerDTOToCustomer(List<CustomerDTO> all);
}
