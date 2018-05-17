package at.ac.tuwien.inso.sepm.ticketline.rest.validator;

import at.ac.tuwien.inso.sepm.ticketline.rest.address.AddressDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.exception.AddressValidationException;

public class AddressValidator {

    public static void validate(AddressDTO addressDTO) throws AddressValidationException {
       if(addressDTO != null) {
           validateStreet(addressDTO);
           validateCity(addressDTO);
           validateCountry(addressDTO);
           validatePostalCode(addressDTO);
       }
    }

    public static void validateStreet(AddressDTO addressDTO) throws AddressValidationException {
        // TODO
    }

    public static void validateCity(AddressDTO addressDTO) throws AddressValidationException {
        // TODO
    }

    public static void validateCountry(AddressDTO addressDTO) throws AddressValidationException {
        // TODO
    }

    public static void validatePostalCode(AddressDTO addressDTO) throws AddressValidationException {
        // TODO
    }
}
