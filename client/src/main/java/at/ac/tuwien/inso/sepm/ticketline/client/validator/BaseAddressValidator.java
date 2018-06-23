package at.ac.tuwien.inso.sepm.ticketline.client.validator;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.AddressValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.PerformanceSearchValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import javafx.scene.control.TextField;

public class BaseAddressValidator{

    private static final String STRING_REGEX = "^[-' a-zA-ZöüöäÜÖÄ]+$";

    public static String validateStreet(TextField streetTextField) throws AddressValidationException {
        String street = streetTextField.getText();
        return validationOfLength(street);
    }

    public static String validateCity(TextField cityTextField) throws AddressValidationException {
        String city = cityTextField.getText();
        return validationOfLength(city);
    }

    public static String validateCountry(TextField countryTextField) throws AddressValidationException {
        String country = countryTextField.getText();
        return validationOfLength(country);
    }

    public static String validatePostalCode(TextField postalCodeTextField) throws AddressValidationException {
        String postalCode = postalCodeTextField.getText();
        return validationOfLength(postalCode);
    }

    private static String validationOfLength(String stringToValidate) throws AddressValidationException {

        if(!stringToValidate.matches(STRING_REGEX)){
            throw new AddressValidationException(BundleManager.getExceptionBundle().getString("exception.validator.performance.string"));
        }

        if (stringToValidate.length() < 1 || stringToValidate.length() > 50) {
            throw new AddressValidationException(BundleManager.getExceptionBundle().getString("exception.validator.reservation.performancename_length"));
        }

        return stringToValidate;
    }
}
