package at.ac.tuwien.inso.sepm.ticketline.rest.validator;

import at.ac.tuwien.inso.sepm.ticketline.rest.address.BaseAddressDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.address.LocationAddressDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.exception.AddressValidationException;

public class BaseAddressValidator {

    private static final String STRING_REGEX = "^[-a-zA-Z ]+$";

    public static void validate(BaseAddressDTO baseAddressDTO) throws AddressValidationException {
       if(baseAddressDTO != null) {
           validateStreet(baseAddressDTO);
           validateCity(baseAddressDTO);
           validateCountry(baseAddressDTO);
           validatePostalCode(baseAddressDTO);
       } else {
           throw new AddressValidationException("Address not found");
       }
    }

    private static final String ALPHABETIC_REGEX = "^[-' a-zA-ZöüäÜÖÄ]+$";
    private static final String ALPHANUMERIC_REGEX="([A-Z0-9])\\w+";
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 50;

    public static void validateStreet(BaseAddressDTO baseAddressDTO) throws AddressValidationException {
        String street = baseAddressDTO.getStreet();
        validateLength(street);
        validateAlphabeticFormat(street);
    }

    public static void validateCity(BaseAddressDTO baseAddressDTO) throws AddressValidationException {
        String city = baseAddressDTO.getCity();
        validateLength(city);
        validateAlphabeticFormat(city);
    }

    public static void validateCountry(BaseAddressDTO baseAddressDTO) throws AddressValidationException {
        String country = baseAddressDTO.getCountry();
        validateLength(country);
        validateAlphabeticFormat(country);
    }

    public static void validatePostalCode(BaseAddressDTO baseAddressDTO) throws AddressValidationException {
        String postalCode = baseAddressDTO.getPostalCode();
        validateLength(postalCode);
        validateAlphanumericFormat(postalCode);
    }

    private static void validateAlphabeticFormat(String text) throws AddressValidationException {
        if(!text.matches(ALPHABETIC_REGEX)){
            throw new AddressValidationException("the input can only be letters");
        }
    }

    private static void validateAlphanumericFormat(String text) throws AddressValidationException {
        if(!text.matches(ALPHANUMERIC_REGEX)){
            throw new AddressValidationException("the input has to be alphanumeric");
        }
    }

    private static void validateLength(String text) throws AddressValidationException {
        if (text.length() < MIN_LENGTH || text.length() > MAX_LENGTH) {
            throw new AddressValidationException("the input hast to be at least 1 and max. 50 characters long");
        }
    }
}
