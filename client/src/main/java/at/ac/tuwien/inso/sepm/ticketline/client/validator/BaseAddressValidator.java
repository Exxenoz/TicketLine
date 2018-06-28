package at.ac.tuwien.inso.sepm.ticketline.client.validator;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.AddressValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.PerformanceSearchValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import javafx.scene.control.TextField;

public class BaseAddressValidator{

    private static final String ALPHABETIC_REGEX = "^[-' a-zA-ZöüäÜÖÄ]*$";
    private static final String ALPHANUMERIC_REGEX="^[-' a-zA-ZöüäÜÖÄ0-9]*$";
    private static final String ALPHABETIC_REGEX_REQUIRED = "^[-' a-zA-ZöüäÜÖÄ]+$";
    private static final String ALPHANUMERIC_REGEX_REQUIRED ="^[-' a-zA-ZöüäÜÖÄ0-9]+$";
    private static final int MAX_LENGTH = 50;

    public static String validateStreet(TextField streetTextField, boolean required) throws AddressValidationException {
        String street = streetTextField.getText();
        validateLength(street, required);
        validateAlphabeticFormat(street, required);
        return street;
    }

    public static String validateCity(TextField cityTextField, boolean required) throws AddressValidationException {
        String city = cityTextField.getText();
        validateLength(city, required);
        validateAlphabeticFormat(city, required);
        return city;
    }

    public static String validateCountry(TextField countryTextField, boolean required) throws AddressValidationException {
        String country = countryTextField.getText();
        validateLength(country, required);
        validateAlphabeticFormat(country, required);
        return country;
    }

    public static String validatePostalCode(TextField postalCodeTextField, boolean required) throws AddressValidationException {
        String postalCode = postalCodeTextField.getText();
        validateLength(postalCode, required);
        validateAlphanumericFormat(postalCode, required);
        return postalCode;
    }

    private static void validateAlphabeticFormat(String text, boolean required) throws AddressValidationException {
        if(required) {
            if (!text.matches(ALPHABETIC_REGEX_REQUIRED)) {
                throw new AddressValidationException(BundleManager.getExceptionBundle().getString("exception.validator.address.alphabetic_invalid"), "exception.validator.address.alphabetic_invalid");
            }
        } else if (!text.matches(ALPHABETIC_REGEX)) {
            throw new AddressValidationException(BundleManager.getExceptionBundle().getString("exception.validator.address.alphabetic_invalid"), "exception.validator.address.alphabetic_invalid");
        }
    }

    private static void validateAlphanumericFormat(String text, boolean required) throws AddressValidationException {
        if (required) {
            if (!text.matches(ALPHANUMERIC_REGEX_REQUIRED)) {
                throw new AddressValidationException(BundleManager.getExceptionBundle().getString("exception.validator.address.alphabetic_invalid"), "exception.validator.address.alphabetic_invalid");
            }
        }
        else if (!text.matches(ALPHANUMERIC_REGEX)) {
            throw new AddressValidationException(BundleManager.getExceptionBundle().getString("exception.validator.address.alphanumeric_invalid"), "exception.validator.address.alphanumeric_invalid");
        }
    }

    private static void validateLength(String text, boolean required) throws AddressValidationException {
        if ((required && text.isEmpty()) || text.length() > MAX_LENGTH) {
            throw new AddressValidationException(BundleManager.getExceptionBundle().getString("exception.validator.address.invalid_length"), "exception.validator.address.invalid_length");
        }
    }
}
