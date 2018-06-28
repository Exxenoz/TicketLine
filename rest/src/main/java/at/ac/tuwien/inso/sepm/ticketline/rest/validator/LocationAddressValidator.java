package at.ac.tuwien.inso.sepm.ticketline.rest.validator;

import at.ac.tuwien.inso.sepm.ticketline.rest.address.LocationAddressDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.exception.AddressValidationException;

public class LocationAddressValidator {

    private static final String ALPHABETIC_REGEX = "^[-' a-zA-ZöüäÜÖÄ]*$";
    private static final int MAX_LENGTH = 50;

    public static void validateLocationAddressDTO(LocationAddressDTO locationAddressDTO) throws AddressValidationException {
        validateLocationName(locationAddressDTO);
    }

    public static void validateLocationName(LocationAddressDTO locationAddressDTO) throws AddressValidationException {
        validateLength(locationAddressDTO);
        validateAlphabeticFormat(locationAddressDTO);
    }

    private static void validateLength(LocationAddressDTO locationAddressDTO) throws AddressValidationException {
        String locationName = locationAddressDTO.getLocationName();
        if (locationName.length() > MAX_LENGTH) {
            throw new AddressValidationException("the input hast to be at least 1 and max. 50 characters long");
        }
    }

    private static void validateAlphabeticFormat(LocationAddressDTO locationAddressDTO) throws AddressValidationException {
        String locationName = locationAddressDTO.getLocationName();
        if(!locationName.matches(ALPHABETIC_REGEX)) {
            throw new AddressValidationException("the input can only be letters");
        }
    }
}
