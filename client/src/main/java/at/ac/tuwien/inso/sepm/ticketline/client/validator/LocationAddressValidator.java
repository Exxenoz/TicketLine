package at.ac.tuwien.inso.sepm.ticketline.client.validator;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.AddressValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import javafx.scene.control.TextField;

public class LocationAddressValidator extends BaseAddressValidator{

    private static final String STRING_REGEX = "^[-' a-zA-ZöüöäÜÖÄ]+$";

    public static String validateLocationName(TextField locationNameTextField) throws AddressValidationException {
        String locationName = locationNameTextField.getText();
        return validationOfLength(locationName);
    }

    private static String validationOfLength(String stringToValidate) throws AddressValidationException {
        if (stringToValidate.length() < 1 || stringToValidate.length() > 50) {
            throw new AddressValidationException(BundleManager.getExceptionBundle().getString("exception.validator.reservation.performancename_length"), "exception.validator.reservation.performancename_length");
        }

        if(!stringToValidate.matches(STRING_REGEX)){
            throw new AddressValidationException(BundleManager.getExceptionBundle().getString("exception.validator.performance.string"), "exception.validator.performance.string");
        }

        return stringToValidate;
    }
}
