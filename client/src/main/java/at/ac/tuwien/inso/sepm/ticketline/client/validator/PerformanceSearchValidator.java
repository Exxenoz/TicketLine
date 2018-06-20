package at.ac.tuwien.inso.sepm.ticketline.client.validator;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.PerformanceSearchValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import javafx.scene.control.TextField;


public class PerformanceSearchValidator {

    private static final String NAME_REGEX = "[0-9]*";
    private static final String PRICE_REGEX = "^\\d{0,8}(,\\d{2})?$";

    public static String validateArtistFirstName(TextField artistFirstNameTextField) throws PerformanceSearchValidationException {
        String artistFirstName = artistFirstNameTextField.getText();
        return validationOfLength(artistFirstName);
    }

    public static String validateArtistLastName(TextField artistLastNameTextField) throws PerformanceSearchValidationException {
        String artistLastName = artistLastNameTextField.getText();

        if (!artistLastName.matches(NAME_REGEX)) {
            throw new PerformanceSearchValidationException(BundleManager.getExceptionBundle().getString("exception.validator.performance.numberFormat"));
        }

        return validationOfLength(artistLastName);
    }

    public static String validateEventName(TextField eventNameTextField) throws PerformanceSearchValidationException {
        String eventName = eventNameTextField.getText();
        return validationOfLength(eventName);
    }

    public static String validateDuration(TextField durationTextField) throws PerformanceSearchValidationException {
        String durationString = durationTextField.getText();

        if(!durationString.matches(NAME_REGEX)){
            throw new PerformanceSearchValidationException(BundleManager.getExceptionBundle().getString("exception.validator.performance.numberFormat"));
        }

        return validationOfLength(durationString);
    }

    public static String validatePrice(TextField priceTextField) throws PerformanceSearchValidationException {
        String price = priceTextField.getText();

        if(!price.matches(PRICE_REGEX)){
            throw new PerformanceSearchValidationException(BundleManager.getExceptionBundle().getString("exception.validator.performance.numberFormatPositive"));
        }

        return price;
    }

    private static String validationOfLength(String stringToValidate) throws PerformanceSearchValidationException {

         if (stringToValidate.length() < 1 || stringToValidate.length() > 50) {
         throw new PerformanceSearchValidationException(BundleManager.getExceptionBundle().getString("exception.validator.reservation.performancename_length"));
         }

         return stringToValidate;
    }
}
