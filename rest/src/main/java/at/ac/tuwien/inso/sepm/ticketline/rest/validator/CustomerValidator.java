package at.ac.tuwien.inso.sepm.ticketline.rest.validator;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.exception.AddressValidationException;
import at.ac.tuwien.inso.sepm.ticketline.rest.exception.CustomerValidationException;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.RestBundleManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerValidator {

    public static final String letterRegex = "^[a-zA-Z]+$";

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
            throw new CustomerValidationException(
                RestBundleManager.getExceptionBundle().getString("exception.validator.customer.is_null")
            );
        }
    }

    public static void validateID(CustomerDTO customerDTO) throws CustomerValidationException {
        validateDTO(customerDTO);
        if(customerDTO.getId() == null || customerDTO.getId() < 0) {
            throw new CustomerValidationException(
                RestBundleManager.getExceptionBundle().getString("exception.validator.customer.id.invalid")
            );
        }
    }

    public static void validateFirstName(CustomerDTO customerDTO) throws CustomerValidationException {
        validateDTO(customerDTO);
        if(customerDTO.getFirstName() == null) {
            throw new CustomerValidationException(
                RestBundleManager.getExceptionBundle().getString("exception.validator.customer.firstname.is_null")
            );
        }

        if(customerDTO.getFirstName().length() < 2) {
            throw new CustomerValidationException(
                RestBundleManager.getExceptionBundle().getString("exception.validator.customer.firstname.too_short")
            );
        }

        if(customerDTO.getFirstName().length() > 50) {
            throw new CustomerValidationException(
                RestBundleManager.getExceptionBundle().getString("exception.validator.customer.firstname.too_long")
            );
        }

        if(!customerDTO.getFirstName().matches(letterRegex)) {
            throw new CustomerValidationException(
                RestBundleManager.getExceptionBundle().getString("exception.validator.customer.firstname.invalid_characters")
            );
        }
    }

    public static void validateLastName(CustomerDTO customerDTO) throws CustomerValidationException {
        validateDTO(customerDTO);
        if(customerDTO.getLastName() == null) {
            throw new CustomerValidationException(
                RestBundleManager.getExceptionBundle().getString("exception.validator.customer.lastname.is_null")
            );
        }

        if(customerDTO.getLastName().length() < 2) {
            throw new CustomerValidationException(
                RestBundleManager.getExceptionBundle().getString("exception.validator.customer.lastname.too_short")
            );
        }

        if(customerDTO.getLastName().length() > 50) {
            throw new CustomerValidationException(
                RestBundleManager.getExceptionBundle().getString("exception.validator.customer.lastname.too_long")
            );
        }

        if(!customerDTO.getLastName().matches(letterRegex)) {
            throw new CustomerValidationException(
                RestBundleManager.getExceptionBundle().getString("exception.validator.customer.lastname.invalid_characters")
            );
        }
    }

    public static void validateTelephoneNumber(CustomerDTO customerDTO) throws CustomerValidationException {
        validateDTO(customerDTO);
        if (customerDTO.getTelephoneNumber() == null) {
            throw new CustomerValidationException(
                RestBundleManager.getExceptionBundle().getString("exception.validator.customer.telephone_number.is_null")
            );
        }

        if(!customerDTO.getTelephoneNumber().matches("^[\\d\\/\\s+-]{7,25}$")) {
            throw new CustomerValidationException(
                RestBundleManager.getExceptionBundle().getString("exception.validator.customer.telephone_number.invalid")
            );
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
                throw new CustomerValidationException(
                    RestBundleManager.getExceptionBundle().getString("exception.validator.customer.email.invalid")
                );
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
