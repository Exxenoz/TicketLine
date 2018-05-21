package at.ac.tuwien.inso.sepm.ticketline.rest.customer;

import at.ac.tuwien.inso.sepm.ticketline.rest.address.BaseAddressDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.address.LocationAddressDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

@ApiModel(value = "CustomerDTO", description = "Data Transfer Object for customer requests via REST")
public class CustomerDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(required = true, readOnly = true, name = "The first name of the customer")
    private String firstName;

    @ApiModelProperty(required = true, readOnly = true, name = "The last name of the customer")
    private String lastName;

    @ApiModelProperty(required = true, readOnly = true, name = "The telephone number of the customer")
    private String telephoneNumber;

    @ApiModelProperty(readOnly = true, name = "The email of the customer")
    private String email;

    @ApiModelProperty(readOnly = true, name = "The address of the customer")
    private BaseAddressDTO address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public BaseAddressDTO getBaseAddress() {
        return address;
    }

    public void setBaseAddress(BaseAddressDTO address) {
        this.address = address;
    }

    public void update(CustomerDTO customerDTO) {
        id = customerDTO.id;
        firstName = customerDTO.firstName;
        lastName = customerDTO.lastName;
        telephoneNumber = customerDTO.telephoneNumber;
        email = customerDTO.email;
        address.update(customerDTO.address);
    }

    @Override
    public String toString() {
        return "CustomerDTO{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", telephoneNumber='" + telephoneNumber + '\'' +
            ", email='" + email + '\'' +
            ", address=" + address +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerDTO that = (CustomerDTO) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(telephoneNumber, that.telephoneNumber) &&
            Objects.equals(email, that.email) &&
            Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, firstName, lastName, telephoneNumber, email, address);
    }
}
