package at.ac.tuwien.inso.sepm.ticketline.client.validator;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.CustomerValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import javafx.scene.control.TextField;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerValidator {
    public static final String letterRegex = "^[a-zA-Z]+$";

    public static String validateFirstName(TextField firstNameTextField) throws CustomerValidationException {
        String firstName = firstNameTextField.getText();

        if(firstName == null) {
            throw new CustomerValidationException(
                BundleManager.getExceptionBundle().getString("exception.validator.customer.firstname.is_null")
            );
        }

        if(firstName.length() < 2) {
            throw new CustomerValidationException(
                BundleManager.getExceptionBundle().getString("exception.validator.customer.firstname.too_short")
            );
        }

        if(firstName.length() > 50) {
            throw new CustomerValidationException(
                BundleManager.getExceptionBundle().getString("exception.validator.customer.firstname.too_long")
            );
        }

        if(!firstName.matches(letterRegex)) {
            throw new CustomerValidationException(
                BundleManager.getExceptionBundle().getString("exception.validator.customer.firstname.invalid_characters")
            );
        }

        return firstName;
    }

    public static String validateLastName(TextField lastNameTextField) throws CustomerValidationException {
        String lastName = lastNameTextField.getText();

        if(lastName == null) {
            throw new CustomerValidationException(
                BundleManager.getExceptionBundle().getString("exception.validator.customer.lastname.is_null")
            );
        }

        if(lastName.length() < 2) {
            throw new CustomerValidationException(
                BundleManager.getExceptionBundle().getString("exception.validator.customer.lastname.too_short")
            );
        }

        if(lastName.length() > 50) {
            throw new CustomerValidationException(
                BundleManager.getExceptionBundle().getString("exception.validator.customer.lastname.too_long")
            );
        }

        if(!lastName.matches(letterRegex)) {
            throw new CustomerValidationException(
                BundleManager.getExceptionBundle().getString("exception.validator.customer.lastname.invalid_characters")
            );
        }

        return lastName;
    }

    public static String validateTelephoneNumber(TextField telephoneNumberTextField) throws CustomerValidationException {
        String telephoneNumber = telephoneNumberTextField.getText();

        if (telephoneNumber == null) {
            throw new CustomerValidationException(
                BundleManager.getExceptionBundle().getString("exception.validator.customer.telephone_number.is_null")
            );
        }

        if(!telephoneNumber.matches("^[\\d\\/\\s+-]{7,25}$")) {
            throw new CustomerValidationException(
                BundleManager.getExceptionBundle().getString("exception.validator.customer.telephone_number.invalid")
            );
        }

        return telephoneNumber;
    }

    public static String validateEmail(TextField emailTextField) throws CustomerValidationException {
        String email = emailTextField.getText();

        if(email != null && !email.isEmpty()) {
            //source: https://stackoverflow.com/a/8204716
            Pattern emailRegex =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

            Matcher matcher = emailRegex.matcher(email);
            if(!matcher.find()) {
                throw new CustomerValidationException(
                    BundleManager.getExceptionBundle().getString("exception.validator.customer.email.invalid")
                );
            }
        }

        return email;
    }
}
