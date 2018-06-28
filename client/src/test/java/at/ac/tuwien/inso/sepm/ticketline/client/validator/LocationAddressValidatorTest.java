package at.ac.tuwien.inso.sepm.ticketline.client.validator;


import at.ac.tuwien.inso.sepm.ticketline.client.exception.AddressValidationException;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("unit-test")
public class LocationAddressValidatorTest {


    private final JFXPanel fxPanel = new JFXPanel();

    @Test
    public void validateLocationNameTest() throws AddressValidationException {
        TextField textField = new TextField();
        textField.setText("Name");
        LocationAddressValidator.validateLocationName(textField);
    }

    @Test(expected = AddressValidationException.class)
    public void validateLocationNameTooLongTest() throws AddressValidationException {
        TextField textField = new TextField();
        textField.setText("veBE2yN1cXvyMQahtUfppjH5x3MioudGCNHA5io6ZGFwkEJKQxN");
        LocationAddressValidator.validateLocationName(textField);
    }

    @Test(expected = AddressValidationException.class)
    public void validateLocationNameInvalidCharactersTest() throws AddressValidationException {
        TextField textField = new TextField();
        textField.setText("ad*");
        LocationAddressValidator.validateLocationName(textField);
    }
}
