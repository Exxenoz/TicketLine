package at.ac.tuwien.inso.sepm.ticketline.rest.validator;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.exception.AddressValidationException;
import at.ac.tuwien.inso.sepm.ticketline.rest.exception.CustomerValidationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerValidator {

    public static final String nameRegex = "^[-' a-zA-ZöüöäÜÖÄ]+$";

    // validate whole customer without id
    public static void validateNewCustomer(CustomerDTO customerDTO) throws CustomerValidationException {
        validateFirstName(customerDTO);
        validateLastName(customerDTO);
        validateTelephoneNumber(customerDTO);
        validateEmail(customerDTO);
        validateAddress(customerDTO);
    }

    // validate whole customer with id
    public static void validateExistingCustomer(CustomerDTO customerDTO) throws CustomerValidationException {
        validateID(customerDTO);
        validateNewCustomer(customerDTO);
    }

    public static void validateDTO(CustomerDTO customerDTO) throws CustomerValidationException {
        if(customerDTO == null) {
            throw new CustomerValidationException("Customer validation failed, because object reference is null!");
        }
    }

    public static void validateID(CustomerDTO customerDTO) throws CustomerValidationException {
        validateDTO(customerDTO);
        if(customerDTO.getId() == null || customerDTO.getId() < 0) {
            throw new CustomerValidationException("Customer validation failed, because ID is invalid!");
        }
    }

    public static void validateFirstName(CustomerDTO customerDTO) throws CustomerValidationException {
        validateDTO(customerDTO);
        if(customerDTO.getFirstName() == null) {
            throw new CustomerValidationException("Customer validation failed, because first name is null!");
        }

        if(customerDTO.getFirstName().length() < 2) {
            throw new CustomerValidationException("Customer validation failed, because first name is too short!");
        }

        if(customerDTO.getFirstName().length() > 50) {
            throw new CustomerValidationException("Customer validation failed, because first name is too long!");
        }

        if(!customerDTO.getFirstName().matches(nameRegex)) {
            throw new CustomerValidationException("Customer validation failed, because first name contains invalid characters!");
        }
    }

    public static void validateLastName(CustomerDTO customerDTO) throws CustomerValidationException {
        validateDTO(customerDTO);
        if(customerDTO.getLastName() == null) {
            throw new CustomerValidationException("Customer validation failed, because last name is null!");
        }

        if(customerDTO.getLastName().length() < 2) {
            throw new CustomerValidationException("Customer validation failed, because last name is too short!");
        }

        if(customerDTO.getLastName().length() > 50) {
            throw new CustomerValidationException("Customer validation failed, because last name is too long!");
        }

        if(!customerDTO.getLastName().matches(nameRegex)) {
            throw new CustomerValidationException("Customer validation failed, because last name contains invalid characters!");
        }
    }

    public static void validateTelephoneNumber(CustomerDTO customerDTO) throws CustomerValidationException {
        validateDTO(customerDTO);
        if (customerDTO.getTelephoneNumber() == null) {
            throw new CustomerValidationException("Customer validation failed, because telephone number is null!");
        }

        if(!customerDTO.getTelephoneNumber().matches("^[\\d\\/\\s+-]{7,25}$")) {
            throw new CustomerValidationException("Customer validation failed, because telephone number is invalid!");
        }
    }

    public static void validateEmail(CustomerDTO customerDTO) throws CustomerValidationException {
        validateDTO(customerDTO);

        if(customerDTO.getEmail() != null && !customerDTO.getEmail().isEmpty()) {
            //source: https://stackoverflow.com/a/8204716
            Pattern emailRegex =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

            Matcher matcher = emailRegex.matcher(customerDTO.getEmail());
            if(!matcher.find()) {
                throw new CustomerValidationException("Customer validation failed, because email is invalid!");
            }
        }
    }

    public static void validateAddress(CustomerDTO customerDTO) throws CustomerValidationException {
        validateDTO(customerDTO);
        try {
            AddressValidator.validate(customerDTO.getBaseAddress());
        } catch (AddressValidationException e) {
            throw new CustomerValidationException(e.getMessage());
        }
    }
}
