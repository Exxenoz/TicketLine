package at.ac.tuwien.inso.sepm.ticketline.client.validator;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.AddressValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import javafx.scene.control.TextField;

public class LocationAddressValidator {

    private static final String ALPHABETIC_REGEX = "^[-' a-zA-ZöüäÜÖÄ]*$";
    private static final int MAX_LENGTH = 50;

    public static String validateLocationName(TextField locationNameTextField) throws AddressValidationException {
        String locationName = locationNameTextField.getText();
        validateLength(locationName);
        validateAlphabeticFormat(locationName);
        return locationName;
    }

    private static void validateAlphabeticFormat(String text) throws AddressValidationException {
        if (!text.matches(ALPHABETIC_REGEX)){
            throw new AddressValidationException(BundleManager.getExceptionBundle().getString("exception.validator.address.alphabetic_invalid"), "exception.validator.address.alphabetic_invalid");
        }
    }

    private static void validateLength(String text) throws AddressValidationException {
        if (text.length() > MAX_LENGTH) {
            throw new AddressValidationException(BundleManager.getExceptionBundle().getString("exception.validator.address.invalid_length"), "exception.validator.address.invalid_length");
        }
    }
}
