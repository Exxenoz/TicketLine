package at.ac.tuwien.inso.sepm.ticketline.client.validator;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.AddressValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.PerformanceSearchValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import javafx.scene.control.TextField;

public class BaseAddressValidator{

    private static final String ALPHABETIC_REGEX = "^[-' a-zA-ZöüäÜÖÄ]+$";
    private static final String ALPHANUMERIC_REGEX="([A-Z0-9])\\w+";
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 50;

    public static String validateStreet(TextField streetTextField) throws AddressValidationException {
        String street = streetTextField.getText();
        validateLength(street);
        validateAlphabeticFormat(street);
        return street;
    }

    public static String validateCity(TextField cityTextField) throws AddressValidationException {
        String city = cityTextField.getText();
        validateLength(city);
        validateAlphabeticFormat(city);
        return city;
    }

    public static String validateCountry(TextField countryTextField) throws AddressValidationException {
        String country = countryTextField.getText();
        validateLength(country);
        validateAlphabeticFormat(country);
        return country;
    }

    public static String validatePostalCode(TextField postalCodeTextField) throws AddressValidationException {
        String postalCode = postalCodeTextField.getText();
        validateLength(postalCode);
        validateAlphanumericFormat(postalCode);
        return postalCode;
    }

    private static void validateAlphabeticFormat(String text) throws AddressValidationException {
        if(!text.matches(ALPHABETIC_REGEX)){
            throw new AddressValidationException(BundleManager.getExceptionBundle().getString("exception.validator.address.alphabetic_invalid"), "exception.validator.address.alphabetic_invalid");
        }
    }

    private static void validateAlphanumericFormat(String text) throws AddressValidationException {
        if(!text.matches(ALPHANUMERIC_REGEX)){
            throw new AddressValidationException(BundleManager.getExceptionBundle().getString("exception.validator.address.alphanumeric_invalid"), "exception.validator.address.alphanumeric_invalid");
        }
    }

    private static void validateLength(String text) throws AddressValidationException {
        if (text.length() < MIN_LENGTH || text.length() > MAX_LENGTH) {
            throw new AddressValidationException(BundleManager.getExceptionBundle().getString("exception.validator.address.invalid_length"), "exception.validator.address.invalid_length");
        }
    }
}
