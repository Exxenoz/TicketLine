package at.ac.tuwien.inso.sepm.ticketline.client.validator;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.AddressValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.PerformanceSearchValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import javafx.scene.control.TextField;

public class BaseAddressValidator{


    public static String validateStreet(TextField streetTextField) throws AddressValidationException {

        return streetTextField.getText();
    }

    public static String validateCity(TextField cityTextField) throws AddressValidationException {
        // TODO
        return cityTextField.getText();
    }

    public static String validateCountry(TextField countryTextField) throws AddressValidationException {
        // TODO
        return countryTextField.getText();
    }

    public static String validatePostalCode(TextField postalCodeTextField) throws AddressValidationException {
        // TODO
        return postalCodeTextField.getText();
    }

    static void validationForStingFields(String stringToValidate) throws PerformanceSearchValidationException {

        if (stringToValidate.length() < 2 || stringToValidate.length() > 100) {
            throw new PerformanceSearchValidationException(BundleManager.getExceptionBundle().getString("exception.validator.reservation.performancename_length"));
        }

    }
}
