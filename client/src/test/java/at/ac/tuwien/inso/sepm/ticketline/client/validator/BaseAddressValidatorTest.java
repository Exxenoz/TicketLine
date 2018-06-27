package at.ac.tuwien.inso.sepm.ticketline.client.validator;


import at.ac.tuwien.inso.sepm.ticketline.client.exception.AddressValidationException;
import javafx.embed.swing.JFXPanel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import javafx.scene.control.TextField;

import static at.ac.tuwien.inso.sepm.ticketline.client.validator.BaseAddressValidator.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("unit-test")
public class BaseAddressValidatorTest {

    private final JFXPanel fxPanel = new JFXPanel();

    //VALIDATE STREET

    @Test
    public void validateStreetNameTest() throws AddressValidationException {
        TextField textField = new TextField();
        textField.setText("street");
        validateStreet(textField);
    }

    @Test(expected = AddressValidationException.class)
    public void validateStreetNameTooShortTest() throws AddressValidationException {
        TextField textField = new TextField();
        textField.setText("");
        validateStreet(textField);
    }

    @Test(expected = AddressValidationException.class)
    public void validateStreetNameTooLongTest() throws AddressValidationException {
        TextField textField = new TextField();
        textField.setText("veBE2yN1cXvyMQahtUfppjH5x3MioudGCNHA5io6ZGFwkEJKQxN");
        validateStreet(textField);
    }

    @Test(expected = AddressValidationException.class)
    public void validateStreetNameInvalidCharactersTest() throws AddressValidationException {
        TextField textField = new TextField();
        textField.setText("str*t");
        validateStreet(textField);
    }

    //VALIDATE CITY

    @Test
    public void validateCityTest() throws AddressValidationException {
        TextField textField = new TextField();
        textField.setText("city");
        validateCity(textField);
    }

    @Test(expected = AddressValidationException.class)
    public void validateCityTooShortTest() throws AddressValidationException {
        TextField textField = new TextField();
        textField.setText("");
        validateCity(textField);
    }

    @Test(expected = AddressValidationException.class)
    public void validateCityTooLongTest() throws AddressValidationException {
        TextField textField = new TextField();
        textField.setText("veBE2yN1cXvyMQahtUfppjH5x3MioudGCNHA5io6ZGFwkEJKQxN");
        validateCity(textField);
    }

    @Test(expected = AddressValidationException.class)
    public void validateCityInvalidCharactersTest() throws AddressValidationException {
        TextField textField = new TextField();
        textField.setText("str*t");
        validateCity(textField);
    }

    //VALIDATE COUNTRY


    @Test
    public void validateCountryTest() throws AddressValidationException {
        TextField textField = new TextField();
        textField.setText("country");
        validateCountry(textField);
    }

    @Test(expected = AddressValidationException.class)
    public void validateCountryTooShortTest() throws AddressValidationException {
        TextField textField = new TextField();
        textField.setText("");
        validateCountry(textField);
    }

    @Test(expected = AddressValidationException.class)
    public void validateCountryTooLongTest() throws AddressValidationException {
        TextField textField = new TextField();
        textField.setText("veBE2yN1cXvyMQahtUfppjH5x3MioudGCNHA5io6ZGFwkEJKQxN");
        validateCountry(textField);
    }

    @Test(expected = AddressValidationException.class)
    public void validateCountryInvalidCharactersTest() throws AddressValidationException {
        TextField textField = new TextField();
        textField.setText("str*t");
        validateCountry(textField);
    }

    //VALIDATE POSTAL CODE

    @Test
    public void validatePostalCodeTest() throws AddressValidationException {
        TextField textField = new TextField();
        textField.setText("4840");
        validatePostalCode(textField);
    }

    @Test(expected = AddressValidationException.class)
    public void validatePostalCodeTooShortTest() throws AddressValidationException {
        TextField textField = new TextField();
        textField.setText("");
        validatePostalCode(textField);
    }

    @Test(expected = AddressValidationException.class)
    public void validatePostalCodeTooLongTest() throws AddressValidationException {
        TextField textField = new TextField();
        textField.setText("2222222222222222222222222222222222222222222222222222");
        validatePostalCode(textField);
    }

    @Test(expected = AddressValidationException.class)
    public void validatePostalCodeInvalidCharactersTest() throws AddressValidationException {
        TextField textField = new TextField();
        textField.setText("notaPostalc0de");
        validatePostalCode(textField);
    }

}
