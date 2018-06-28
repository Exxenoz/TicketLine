package at.ac.tuwien.inso.sepm.ticketline.rest.validator;

import at.ac.tuwien.inso.sepm.ticketline.rest.exception.PerformanceSearchValidationException;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.SearchDTO;
public class PerformanceSearchValidator {

    private static final String PRICE_REGEX = "^\\d{0,8}(,\\d{2})?$";
    private static final String STRING_REGEX = "^[-a-zA-Z ]+$";

    public static void validatePerformanceSearchDTO(SearchDTO searchDTO) throws PerformanceSearchValidationException {
        validateArtistFirstName(searchDTO);
        validateArtistLastName(searchDTO);
        validateEventName(searchDTO);
        validateDuration(searchDTO);
        validatePrice(searchDTO);
    }

    static void validateArtistFirstName(SearchDTO searchDTO) throws PerformanceSearchValidationException {
        if(!(searchDTO.getFirstName().length() < 1)) {
            validationForStingFields(searchDTO.getFirstName());

            if (!searchDTO.getFirstName().matches(STRING_REGEX)) {
                throw new PerformanceSearchValidationException("the first name of the artist contains invalid characters");
            }
        }
    }

    static void validateArtistLastName(SearchDTO searchDTO) throws PerformanceSearchValidationException {
        if(!(searchDTO.getLastName().length() < 1)) {
            validationForStingFields(searchDTO.getLastName());

            if (!searchDTO.getLastName().matches(STRING_REGEX)) {
                throw new PerformanceSearchValidationException("the last name of the artist contains invalid characters");
            }
        }
    }

    static void validateEventName(SearchDTO searchDTO) throws PerformanceSearchValidationException {
        if(!(searchDTO.getEventName().length() < 1)) {
            validationForStingFields(searchDTO.getEventName());

            if (!searchDTO.getEventName().matches(STRING_REGEX)) {
                throw new PerformanceSearchValidationException("the event name contains invalid characters");
            }
        }
    }

    static void validateDuration(SearchDTO searchDTO) throws PerformanceSearchValidationException {
        if(searchDTO.getDuration() != null) {
            if (searchDTO.getDuration().getSeconds() < 0) {
                throw new PerformanceSearchValidationException("duration must be positive");
            }
        }
    }

    static void validatePrice(SearchDTO searchDTO) throws PerformanceSearchValidationException {
        if(searchDTO.getPrice() != null) {
            if (!searchDTO.getPrice().toString().matches(PRICE_REGEX)) {
                throw new PerformanceSearchValidationException("the format of price ist not correct");
            }
        }
    }

    static void validationForStingFields(String string) throws PerformanceSearchValidationException {

        if (string.length() < 1 || string.length() > 50) {
            throw new PerformanceSearchValidationException("the input has to be between 1 and 50 characters");
        }
    }
}
