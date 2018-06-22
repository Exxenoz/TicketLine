package at.ac.tuwien.inso.sepm.ticketline.rest.validator;

import at.ac.tuwien.inso.sepm.ticketline.rest.exception.PerformanceSearchValidationException;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.SearchDTO;
public class PerformanceSearchValidator {

    private static final String NAME_REGEX = "[0-9]*";

    public static void validateReservationSearchDTO(SearchDTO searchDTO) throws PerformanceSearchValidationException {
        validateArtistFirstName(searchDTO);
        validateArtistLastName(searchDTO);
        validateEventName(searchDTO);
        validateDuration(searchDTO);
        validateTime(searchDTO);
        validatePrice(searchDTO);
        validateAddress(searchDTO);

    }

    static void validateArtistFirstName(SearchDTO searchDTO) throws PerformanceSearchValidationException {
       validationForStingFields(searchDTO, "artist");
    }

    static void validateArtistLastName(SearchDTO searchDTO) throws PerformanceSearchValidationException {
        validationForStingFields(searchDTO, "artist");

        if (!searchDTO.getLastName().matches(NAME_REGEX)) {
            throw new PerformanceSearchValidationException("the last name of the artist can only contain numbers");
        }
    }

    static void validateEventName(SearchDTO searchDTO) throws PerformanceSearchValidationException {
        validationForStingFields(searchDTO, "event");
    }

    static void validateDuration(SearchDTO searchDTO){}

    static void validateTime(SearchDTO searchDTO){}

    static void validatePrice(SearchDTO searchDTO){}

    static void validateAddress(SearchDTO searchDTO){}

    static void validationForStingFields(SearchDTO searchDTO, String string) throws PerformanceSearchValidationException {
        if (searchDTO.getFirstName() == null) {
            throw new PerformanceSearchValidationException("first name of the" + string + "for the search can not be null");
        }
        if (searchDTO.getFirstName().length() < 2) {
            throw new PerformanceSearchValidationException("first name of the" + string + "for the search has to be at least 2 characters long");
        }
        if (searchDTO.getFirstName().length() > 50) {
            throw new PerformanceSearchValidationException("first name of the" + string + "for the search can not be longer than 50 characters");
        }
    }
}
