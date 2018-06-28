package at.ac.tuwien.inso.sepm.ticketline.client.validator;


import at.ac.tuwien.inso.sepm.ticketline.client.exception.CustomerValidationException;
import javafx.embed.swing.JFXPanel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import javafx.scene.control.TextField;

import static at.ac.tuwien.inso.sepm.ticketline.client.validator.CustomerValidator.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("unit-test")
public class CustomerValidatorTest {

    private final JFXPanel fxPanel = new JFXPanel();

    //VALIDATE FIRST NAME

    @Test
    public void validateCustomerFirstNameTest() throws CustomerValidationException {
        TextField textField = new TextField();
        textField.setText("Name");
        validateFirstName(textField);
    }

    @Test(expected = CustomerValidationException.class)
    public void validateCustomerFirstNameNullTest() throws CustomerValidationException {
        TextField textField = new TextField();
        validateFirstName(textField);
    }

    @Test(expected = CustomerValidationException.class)
    public void validateCustomerFirstNameTooShortTest() throws CustomerValidationException {
        TextField textField = new TextField();
        textField.setText("N");
        validateFirstName(textField);
    }

    @Test(expected = CustomerValidationException.class)
    public void validateCustomerFirstNameTooLongTest() throws CustomerValidationException {
        TextField textField = new TextField();
        textField.setText("veBE2yN1cXvyMQahtUfppjH5x3MioudGCNHA5io6ZGFwkEJKQxN");
        validateFirstName(textField);
    }

    @Test(expected = CustomerValidationException.class)
    public void validateCustomerFirstNameInvalidCharactersTest() throws CustomerValidationException {
        TextField textField = new TextField();
        textField.setText("N*me");
        validateFirstName(textField);
    }


    //VALIDATE LAST NAME


    @Test
    public void validateCustomerLastNameTest() throws CustomerValidationException {
        TextField textField = new TextField();
        textField.setText("Name");
        validateLastName(textField);
    }

    @Test(expected = CustomerValidationException.class)
    public void validateCustomerLastNameNullTest() throws CustomerValidationException {
        TextField textField = new TextField();
        validateLastName(textField);
    }

    @Test(expected = CustomerValidationException.class)
    public void validateCustomerLastNameTooShortTest() throws CustomerValidationException {
        TextField textField = new TextField();
        textField.setText("N");
        validateLastName(textField);
    }

    @Test(expected = CustomerValidationException.class)
    public void validateCustomerLastNameTooLongTest() throws CustomerValidationException {
        TextField textField = new TextField();
        textField.setText("veBE2yN1cXvyMQahtUfppjH5x3MioudGCNHA5io6ZGFwkEJKQxN");
        validateLastName(textField);
    }

    @Test(expected = CustomerValidationException.class)
    public void validateCustomerLastNameInvalidCharactersTest() throws CustomerValidationException {
        TextField textField = new TextField();
        textField.setText("N*me");
        validateLastName(textField);
    }


    //VALIDATE TELEPHONE NUMBER


    @Test
    public void validateCustomerTelephoneNumberTest() throws CustomerValidationException {
        TextField textField = new TextField();
        textField.setText("0123456");
        validateTelephoneNumber(textField);
    }

    @Test(expected = CustomerValidationException.class)
    public void validateCustomerTelephoneNumberNullTest() throws CustomerValidationException {
        TextField textField = new TextField();
        validateTelephoneNumber(textField);
    }


    @Test(expected = CustomerValidationException.class)
    public void validateCustomerTelephoneNumberTooShortTest() throws CustomerValidationException {
        TextField textField = new TextField();
        textField.setText("012345");
        validateTelephoneNumber(textField);
    }

    @Test(expected = CustomerValidationException.class)
    public void validateCustomerTelephoneNumberTooLongTest() throws CustomerValidationException {
        TextField textField = new TextField();
        textField.setText("2222222222222222222222222222222222222222222222222222");
        validateTelephoneNumber(textField);
    }

    @Test(expected = CustomerValidationException.class)
    public void validateCustomerTelephoneNumberInvalidCharactersTest() throws CustomerValidationException {
        TextField textField = new TextField();
        textField.setText("012345*6");
        validateTelephoneNumber(textField);
    }

    //VALIDATE E-MAIL

    @Test
    public void validateCustomerEmailTest() throws CustomerValidationException {
        TextField textField = new TextField();
        textField.setText("email@email.com");
        validateEmail(textField);
    }

    @Test(expected = CustomerValidationException.class)
    public void validateCustomerEmailInvalidTest() throws CustomerValidationException {
        TextField textField = new TextField();
        textField.setText("notamailadress");
        validateEmail(textField);
    }
}
