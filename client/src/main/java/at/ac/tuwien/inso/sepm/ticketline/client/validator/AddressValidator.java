package at.ac.tuwien.inso.sepm.ticketline.client.validator;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.AddressValidationException;
import javafx.scene.control.TextField;

public class AddressValidator {
    public static String validateStreet(TextField streetTextField) throws AddressValidationException {
        // TODO
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
}
