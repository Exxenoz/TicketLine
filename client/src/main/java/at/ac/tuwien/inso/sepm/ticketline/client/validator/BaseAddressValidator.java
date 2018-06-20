package at.ac.tuwien.inso.sepm.ticketline.client.validator;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.AddressValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.PerformanceSearchValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import javafx.scene.control.TextField;

public class BaseAddressValidator{


    public static String validateStreet(TextField streetTextField) throws AddressValidationException {
        String street = streetTextField.toString();
        return validationOfLength(street);
    }

    public static String validateCity(TextField cityTextField) throws AddressValidationException {
        String city = cityTextField.toString();
        return validationOfLength(city);
    }

    public static String validateCountry(TextField countryTextField) throws AddressValidationException {
        String country = countryTextField.toString();
        return validationOfLength(country);
    }

    public static String validatePostalCode(TextField postalCodeTextField) throws AddressValidationException {
        String postalCode = postalCodeTextField.toString();
        return validationOfLength(postalCode);
    }

    private static String validationOfLength(String stringToValidate) throws AddressValidationException {

        if (stringToValidate.length() < 1 || stringToValidate.length() > 50) {
            throw new AddressValidationException(BundleManager.getExceptionBundle().getString("exception.validator.reservation.performancename_length"));
        }

        return stringToValidate;
    }
}
