package at.ac.tuwien.inso.sepm.ticketline.server.unittests.customer;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.exception.CustomerValidationException;
import at.ac.tuwien.inso.sepm.ticketline.rest.validator.CustomerValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("unit-test")
public class CustomerValidatorTest {

    @Test(expected = CustomerValidationException.class)
    public void validateNewUserWithNullShouldThrowException() throws CustomerValidationException {
        CustomerValidator.validateNewCustomer(null);
    }

    @Test(expected = CustomerValidationException.class)
    public void validateExistingUserWithNullShouldThrowException() throws CustomerValidationException {
        CustomerValidator.validateExistingCustomer(null);
    }

    // FIRST NAME

    @Test
    public void validateCorrectFirstName() throws CustomerValidationException {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName("Florian");
        CustomerValidator.validateFirstName(customerDTO);
        assert true;
    }

    @Test(expected = CustomerValidationException.class)
    public void validateFirstNameWithInvalidCharactersShouldThrowException() throws CustomerValidationException {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName("Fl0r1an!");
        CustomerValidator.validateFirstName(customerDTO);
    }

    @Test(expected = CustomerValidationException.class)
    public void validateShortFirstNameShouldThrowException() throws CustomerValidationException {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName("A");
        CustomerValidator.validateFirstName(customerDTO);
    }

    @Test(expected = CustomerValidationException.class)
    public void validateTooLongFirstNameShouldThrowException() throws CustomerValidationException {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName("Abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxy");
        CustomerValidator.validateFirstName(customerDTO);
    }

    // LAST NAME

    @Test
    public void validateCorrectLastName() throws CustomerValidationException {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setLastName("Florian");
        CustomerValidator.validateLastName(customerDTO);
        assert true;
    }

    @Test(expected = CustomerValidationException.class)
    public void validateLastNameWithInvalidCharactersShouldThrowException() throws CustomerValidationException {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setLastName("Fl0r1an!");
        CustomerValidator.validateLastName(customerDTO);
    }

    @Test(expected = CustomerValidationException.class)
    public void validateShortLastNameShouldThrowException() throws CustomerValidationException {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setLastName("A");
        CustomerValidator.validateLastName(customerDTO);
    }

    @Test(expected = CustomerValidationException.class)
    public void validateTooLongLastNameShouldThrowException() throws CustomerValidationException {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setLastName("Abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxy");
        CustomerValidator.validateFirstName(customerDTO);
    }

    // TELEPHONE NUMBER

    @Test
    public void validateMalformedTelephoneNumberShouldThrowException() throws CustomerValidationException {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setTelephoneNumber("Florian");
        CustomerValidator.validateTelephoneNumber(customerDTO);
    }

    @Test
    public void validateCorrectTelephoneNumber() throws CustomerValidationException {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setTelephoneNumber("+43 699 1111 1111");
        CustomerValidator.validateTelephoneNumber(customerDTO);
        assert true;
    }

    // EMAIL

    @Test
    public void validateMalformedEmailShouldThrowException() throws CustomerValidationException {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setEmail("Florian");
        CustomerValidator.validateEmail(customerDTO);
    }

    @Test
    public void validateCorrectEmail() throws CustomerValidationException {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setEmail("flo@flo.flo");
        CustomerValidator.validateEmail(customerDTO);
        assert true;
    }
}

