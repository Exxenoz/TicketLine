package at.ac.tuwien.inso.sepm.ticketline.rest.validator;

import at.ac.tuwien.inso.sepm.ticketline.rest.address.BaseAddressDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.address.LocationAddressDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.exception.AddressValidationException;

public class BaseAddressValidator {

    private static final String ALPHABETIC_REGEX = "^[-' a-zA-ZöüäÜÖÄ]*$";
    private static final String ALPHANUMERIC_REGEX= "^[-' a-zA-ZöüäÜÖÄ0-9]*$";
    private static final String ALPHABETIC_REGEX_REQUIRED = "^[-' a-zA-ZöüäÜÖÄ]+$";
    private static final String ALPHANUMERIC_REGEX_REQUIRED ="^[-' a-zA-ZöüäÜÖÄ0-9]+$";
    private static final int MAX_LENGTH = 50;

    public static void validate(BaseAddressDTO baseAddressDTO, boolean required) throws AddressValidationException {
       if(baseAddressDTO != null) {
           validateStreet(baseAddressDTO, required);
           validateCity(baseAddressDTO, required);
           validateCountry(baseAddressDTO, required);
           validatePostalCode(baseAddressDTO, required);
       } else {
           throw new AddressValidationException("Address not found");
       }
    }

    public static void validateStreet(BaseAddressDTO baseAddressDTO, boolean required) throws AddressValidationException {
        String street = baseAddressDTO.getStreet();
        validateLength(street);
        validateAlphabeticFormat(street, required);
    }

    public static void validateCity(BaseAddressDTO baseAddressDTO, boolean required) throws AddressValidationException {
        String city = baseAddressDTO.getCity();
        validateLength(city);
        validateAlphabeticFormat(city, required);
    }

    public static void validateCountry(BaseAddressDTO baseAddressDTO, boolean required) throws AddressValidationException {
        String country = baseAddressDTO.getCountry();
        validateLength(country);
        validateAlphabeticFormat(country, required);
    }

    public static void validatePostalCode(BaseAddressDTO baseAddressDTO, boolean required) throws AddressValidationException {
        String postalCode = baseAddressDTO.getPostalCode();
        validateLength(postalCode);
        validateAlphanumericFormat(postalCode, required);
    }

    private static void validateAlphabeticFormat(String text, boolean required) throws AddressValidationException {
        if (required) {
            if (!text.matches(ALPHABETIC_REGEX_REQUIRED)) {
                throw new AddressValidationException("the input can only be letters and can't be empty");
            }
        }
        else if(!text.matches(ALPHABETIC_REGEX)){
            throw new AddressValidationException("the input can only be letters");
        }
    }

    private static void validateAlphanumericFormat(String text, boolean required) throws AddressValidationException {
        if (required) {
            if (!text.matches(ALPHANUMERIC_REGEX_REQUIRED)) {
                throw new AddressValidationException("the input can only be letters and can't be empty");
            }
        }
        else if(!text.matches(ALPHANUMERIC_REGEX)){
            throw new AddressValidationException("the input has to be alphanumeric");
        }
    }

    private static void validateLength(String text) throws AddressValidationException {
        if (text.length() > MAX_LENGTH) {
            throw new AddressValidationException("the input has to be max. 50 characters long");
        }
    }
}
