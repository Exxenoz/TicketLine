package at.ac.tuwien.inso.sepm.ticketline.rest.validator;

import at.ac.tuwien.inso.sepm.ticketline.rest.address.BaseAddressDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.address.LocationAddressDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.exception.AddressValidationException;

// TODO: implement(and include location address)
public class AddressValidator {

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

    public static void validateStreet(BaseAddressDTO baseAddressDTO) throws AddressValidationException {
        // TODO
    }

    public static void validateCity(BaseAddressDTO baseAddressDTO) throws AddressValidationException {
        // TODO
    }

    public static void validateCountry(BaseAddressDTO baseAddressDTO) throws AddressValidationException {
        // TODO
    }

    public static void validatePostalCode(BaseAddressDTO baseAddressDTO) throws AddressValidationException {
        // TODO
    }
}
