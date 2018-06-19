package at.ac.tuwien.inso.sepm.ticketline.client.validator;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.AddressValidationException;
import javafx.scene.control.TextField;

public class LocationAddressValidator extends BaseAddressValidator{

    public static String validateLocationName(TextField locationNameTextField) throws AddressValidationException {
        return locationNameTextField.getText();
    }
}
