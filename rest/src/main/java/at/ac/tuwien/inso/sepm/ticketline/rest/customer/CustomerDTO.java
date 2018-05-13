package at.ac.tuwien.inso.sepm.ticketline.rest.customer;

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

    public CustomerDTO() {

    }

    public CustomerDTO(String firstName, String lastName, String email, String telephoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerDTO that = (CustomerDTO) o;
        return Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(telephoneNumber, that.telephoneNumber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(firstName, lastName, email, telephoneNumber);
    }
}
