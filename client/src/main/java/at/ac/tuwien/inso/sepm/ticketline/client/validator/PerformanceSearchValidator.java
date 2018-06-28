package at.ac.tuwien.inso.sepm.ticketline.client.validator;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.PerformanceSearchValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import javafx.scene.control.TextField;


public class PerformanceSearchValidator {

    private static final String NUMBER_REGEX = "[0-9]*";
    private static final String PRICE_REGEX = "^\\d{0,8}([,.]\\d{1,2})?$";
    private static final String STRING_REGEX = "^[-' a-zA-ZöüäÜÖÄ]+$";
    private static final int MAX_LONG_DIGITS = 18;

    public static String validateArtistFirstName(TextField artistFirstNameTextField) throws PerformanceSearchValidationException {
        String artistFirstName = artistFirstNameTextField.getText();

        if(!artistFirstName.matches(STRING_REGEX)){
            throw new PerformanceSearchValidationException(BundleManager.getExceptionBundle().getString("exception.validator.performance.string"), "exception.validator.performance.string");
        }

        return validationOfLength(artistFirstName);
    }

    public static String validateArtistLastName(TextField artistLastNameTextField) throws PerformanceSearchValidationException {
        String artistLastName = artistLastNameTextField.getText();

        if (!artistLastName.matches(STRING_REGEX)) {
            throw new PerformanceSearchValidationException(BundleManager.getExceptionBundle().getString("exception.validator.performance.string"), "exception.validator.performance.string");
        }

        return validationOfLength(artistLastName);
    }

    public static String validateEventName(TextField eventNameTextField) throws PerformanceSearchValidationException {
        String eventName = eventNameTextField.getText();

        if(!eventName.matches(STRING_REGEX)){
            throw new PerformanceSearchValidationException(BundleManager.getExceptionBundle().getString("exception.validator.performance.string"), "exception.validator.performance.string");
        }

        return validationOfLength(eventName);
    }

    public static String validateDuration(TextField durationTextField) throws PerformanceSearchValidationException {
        String durationString = durationTextField.getText();

        if(!durationString.matches(NUMBER_REGEX)){
            throw new PerformanceSearchValidationException(BundleManager.getExceptionBundle().getString("exception.validator.performance.numberFormat"), "exception.validator.performance.numberFormat");
        }

        if(durationString.isEmpty() || durationString.length() > MAX_LONG_DIGITS) {
            throw new PerformanceSearchValidationException(BundleManager.getExceptionBundle().getString("exception.performance_search.long_digits_too_long"), "exception.performance_search.long_digits_too_long");
        }

        return durationString;
    }

    public static String validatePrice(TextField priceTextField) throws PerformanceSearchValidationException {
        String price = priceTextField.getText();

        if(!price.matches(PRICE_REGEX)){
            throw new PerformanceSearchValidationException(BundleManager.getExceptionBundle().getString("exception.validator.performance.numberFormatPositive"), "exception.validator.performance.numberFormatPositive");
        }

        return price;
    }

    private static String validationOfLength(String stringToValidate) throws PerformanceSearchValidationException {

         if (stringToValidate.length() < 1 || stringToValidate.length() > 50) {
         throw new PerformanceSearchValidationException(BundleManager.getExceptionBundle().getString("exception.validator.reservation.performancename_length"), "exception.validator.reservation.performancename_length");
         }

         return stringToValidate;
    }
}
