package at.ac.tuwien.inso.sepm.ticketline.client.validator;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.PerformanceSearchValidationException;

import java.awt.*;

public class PerformanceSearchValidator {

    private static final String NAME_REGEX = "[0-9]*";

    static void validateArtistFirstName(TextField artistFirstNameTextField) throws PerformanceSearchValidationException {
        String artistFirstName = artistFirstNameTextField.getText();
        validationForStingFields(artistFirstName);
    }

    static void validateArtistLastName(TextField artistLastNameTextField) throws PerformanceSearchValidationException {
        String artistLastName = artistLastNameTextField.getText();
        validationForStingFields(artistLastName);

        if (!artistLastName.matches(NAME_REGEX)) {
            throw new PerformanceSearchValidationException("the last name of the artist can only contain numbers");
        }
    }

    static void validateEventName(TextField eventNameTextField) throws PerformanceSearchValidationException {
        String eventName = eventNameTextField.getText();
        validationForStingFields(eventName);
    }

    static void validateDuration(TextField durationTextField){}

    static void validateTime(){}

    static void validatePrice(TextField priceTextField){}



    static void validationForStingFields(String stringToValidate) throws PerformanceSearchValidationException {
        /**
         if (stringToValidate == null) {
         throw new PerformanceSearchValidationException("first name of the" + string + "for the search can not be null");
         }
         if (searchDTO.getFirstName().length() < 2) {
         throw new PerformanceSearchValidationException("first name of the" + string + "for the search has to be at least 2 characters long");
         }
         if (searchDTO.getFirstName().length() > 50) {
         throw new PerformanceSearchValidationException("first name of the" + string + "for the search can not be longer than 50 characters");
         }

         */
    }
}
