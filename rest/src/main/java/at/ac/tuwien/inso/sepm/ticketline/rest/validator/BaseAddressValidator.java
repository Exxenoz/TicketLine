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

    public static String validateStreet(BaseAddressDTO baseAddressDTO) throws AddressValidationException {
        String street = baseAddressDTO.getStreet();
        if(street.length() >= 1) {
            return validationOfLength(street);
        }
        return street;
    }

    public static String validateCity(BaseAddressDTO baseAddressDTO) throws AddressValidationException {
        String city = baseAddressDTO.getCity();
        if(city.length() >= 1) {
            return validationOfLength(city);
        }
        return city;
    }

    public static String validateCountry(BaseAddressDTO baseAddressDTO) throws AddressValidationException {
        String country = baseAddressDTO.getCountry();
        if(country.length() >= 1) {
            return validationOfLength(country);
        }
        return country;
    }

    public static String validatePostalCode(BaseAddressDTO baseAddressDTO) throws AddressValidationException {
        String postalCode = baseAddressDTO.getPostalCode();
        if(postalCode.length() >= 1) {
            return validationOfLength(postalCode);
        }
        return postalCode;
    }
    private static String validationOfLength(String stringToValidate) throws AddressValidationException {
        if (stringToValidate.length() < 1 || stringToValidate.length() > 50) {
            throw new AddressValidationException("the input hast to be at least 2 and max. 50 characters long");
        }

        if(!stringToValidate.matches(STRING_REGEX)){
            throw new AddressValidationException("the input can only be letters");
        }

        return stringToValidate;
    }
}
