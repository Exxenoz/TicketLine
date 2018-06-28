package at.ac.tuwien.inso.sepm.ticketline.client.validator;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.PerformanceSearchValidationException;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static at.ac.tuwien.inso.sepm.ticketline.client.validator.PerformanceSearchValidator.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("unit-test")
public class PerformanceSearchValidatorTest {

    private final JFXPanel fxPanel = new JFXPanel();

    //VALIDATE ARTIST FIRST NAME

    @Test
    public void validateArtistFirstNameTest() throws PerformanceSearchValidationException {
        TextField textField = new TextField();
        textField.setText("Name");
        validateArtistFirstName(textField);
    }

    @Test(expected = PerformanceSearchValidationException.class)
    public void validateArtistFirstNameTooShortTest() throws PerformanceSearchValidationException {
        TextField textField = new TextField();
        textField.setText("");
        validateArtistFirstName(textField);
    }

    @Test(expected = PerformanceSearchValidationException.class)
    public void validateArtistFirstNameTooLongTest() throws PerformanceSearchValidationException {
        TextField textField = new TextField();
        textField.setText("veBE2yN1cXvyMQahtUfppjH5x3MioudGCNHA5io6ZGFwkEJKQxN");
        validateArtistFirstName(textField);
    }

    @Test(expected = PerformanceSearchValidationException.class)
    public void validateArtistFirstNameInvalidCharactersTest() throws PerformanceSearchValidationException {
        TextField textField = new TextField();
        textField.setText("n*ds");
        validateArtistFirstName(textField);
    }

    //VALIDATE ARTIST LAST NAME

    @Test
    public void validateArtistLastNameTest() throws PerformanceSearchValidationException {
        TextField textField = new TextField();
        textField.setText("Name");
        validateArtistLastName(textField);
    }

    @Test(expected = PerformanceSearchValidationException.class)
    public void validateArtistLastNameTooShortTest() throws PerformanceSearchValidationException {
        TextField textField = new TextField();
        textField.setText("");
        validateArtistLastName(textField);
    }

    @Test(expected = PerformanceSearchValidationException.class)
    public void validateArtistLastNameTooLongTest() throws PerformanceSearchValidationException {
        TextField textField = new TextField();
        textField.setText("veBE2yN1cXvyMQahtUfppjH5x3MioudGCNHA5io6ZGFwkEJKQxN");
        validateArtistLastName(textField);
    }

    @Test(expected = PerformanceSearchValidationException.class)
    public void validateArtistLastNameInvalidCharactersTest() throws PerformanceSearchValidationException {
        TextField textField = new TextField();
        textField.setText("n*ds");
        validateArtistLastName(textField);
    }

    //VALIDATE EVENT NAME

    @Test
    public void validateEventNameTest() throws PerformanceSearchValidationException {
        TextField textField = new TextField();
        textField.setText("Name");
        validateEventName(textField);
    }

    @Test(expected = PerformanceSearchValidationException.class)
    public void validateEventNameTooShortTest() throws PerformanceSearchValidationException {
        TextField textField = new TextField();
        textField.setText("");
        validateEventName(textField);
    }

    @Test(expected = PerformanceSearchValidationException.class)
    public void validateEventNameTooLongTest() throws PerformanceSearchValidationException {
        TextField textField = new TextField();
        textField.setText("veBE2yN1cXvyMQahtUfppjH5x3MioudGCNHA5io6ZGFwkEJKQxN");
        validateEventName(textField);
    }

    @Test(expected = PerformanceSearchValidationException.class)
    public void validateEventNameInvalidCharactersTest() throws PerformanceSearchValidationException {
        TextField textField = new TextField();
        textField.setText("n*ds");
        validateDuration(textField);
    }


    //VALIDATE DURATION

    @Test
    public void validateDurationTest() throws PerformanceSearchValidationException {
        TextField textField = new TextField();
        textField.setText("99");
        validateDuration(textField);
    }

    @Test(expected = PerformanceSearchValidationException.class)
    public void validateDurationTooShortTest() throws PerformanceSearchValidationException {
        TextField textField = new TextField();
        textField.setText("");
        validateDuration(textField);
    }

    @Test(expected = PerformanceSearchValidationException.class)
    public void validateDurationTooLongTest() throws PerformanceSearchValidationException {
        TextField textField = new TextField();
        textField.setText("2222222222222222222222222222222222222222222222222222");
        validateDuration(textField);
    }

    @Test(expected = PerformanceSearchValidationException.class)
    public void validateDurationInvalidCharactersTest() throws PerformanceSearchValidationException {
        TextField textField = new TextField();
        textField.setText("letters");
        validateDuration(textField);
    }

    //VALIDATE PRICE

    @Test
    public void validatePriceTest() throws PerformanceSearchValidationException {
        TextField textField = new TextField();
        textField.setText("12,00");
        validatePrice(textField);
    }

    @Test(expected = PerformanceSearchValidationException.class)
    public void validatePriceInvalidCharactersTest() throws PerformanceSearchValidationException {
        TextField textField = new TextField();
        textField.setText("xy");
        validatePrice(textField);
    }
}
