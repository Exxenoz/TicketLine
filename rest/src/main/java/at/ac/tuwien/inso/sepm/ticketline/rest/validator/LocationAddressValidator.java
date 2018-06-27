package at.ac.tuwien.inso.sepm.ticketline.rest.validator;

import at.ac.tuwien.inso.sepm.ticketline.rest.address.LocationAddressDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.exception.AddressValidationException;

public class LocationAddressValidator extends BaseAddressValidator {

    private static final String STRING_REGEX = "^[-' a-zA-ZöüöäÜÖÄ]+$";

    public static String validateLocationAddressDTO(LocationAddressDTO locationAddressDTO) throws AddressValidationException {
        return  validateLocationName(locationAddressDTO);
    }

    static String validateLocationName(LocationAddressDTO locationAddressDTO) throws AddressValidationException {
        if(!(locationAddressDTO.getLocationName().length() < 1)) {
            if (locationAddressDTO.getLocationName().length() < 1 || locationAddressDTO.getLocationName().length() > 50) {
                throw new AddressValidationException("the input hast to be at least 2 and max. 50 characters long");
            }

            if (!locationAddressDTO.getLocationName().matches(STRING_REGEX)) {
                throw new AddressValidationException("the input can only be letters");
            }
        }
        return  locationAddressDTO.getLocationName();
    }
}
