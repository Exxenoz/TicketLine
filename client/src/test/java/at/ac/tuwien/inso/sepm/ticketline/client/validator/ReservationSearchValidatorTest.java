package at.ac.tuwien.inso.sepm.ticketline.client.validator;


import at.ac.tuwien.inso.sepm.ticketline.client.exception.ReservationSearchValidationException;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TextField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static at.ac.tuwien.inso.sepm.ticketline.client.validator.ReservationSearchValidator.validatePerformanceName;
import static at.ac.tuwien.inso.sepm.ticketline.client.validator.ReservationSearchValidator.validateReservationNumber;

@RunWith(SpringRunner.class)
@ActiveProfiles("unit-test")
public class ReservationSearchValidatorTest {

    private final JFXPanel fxPanel = new JFXPanel();


    //VALIDATE RESERVATION NUMBER

    @Test
    public void validateReservationNumberTest() throws ReservationSearchValidationException {
        TextField textField = new TextField();
        textField.setText("A123456");
        validateReservationNumber(textField);
    }

    @Test(expected = ReservationSearchValidationException.class)
    public void validateReservationNumberInvalidLengthTest() throws ReservationSearchValidationException {
        TextField textField = new TextField();
        textField.setText("A1234567");
        validateReservationNumber(textField);
    }

    @Test(expected = ReservationSearchValidationException.class)
    public void validateReservationNumberInvalidCharactersTest() throws ReservationSearchValidationException {
        TextField textField = new TextField();
        textField.setText("A1234567Z");
        validateReservationNumber(textField);
    }


    //VALIDATE PERFORMANCE NAME

    @Test
    public void validatePerformanceNameTest() throws ReservationSearchValidationException {
        TextField textField = new TextField();
        textField.setText("Name");
        validatePerformanceName(textField);
    }

    @Test(expected = ReservationSearchValidationException.class)
    public void validatePerformanceNameTooShortTest() throws ReservationSearchValidationException {
        TextField textField = new TextField();
        textField.setText("");
        validatePerformanceName(textField);
    }

    @Test(expected = ReservationSearchValidationException.class)
    public void validatePerformanceNameTooLongTest() throws ReservationSearchValidationException {
        TextField textField = new TextField();
        textField.setText("veBE2yN1cXvyMQahtUfppjH5x3MioudGCNHA5io6ZGFwkEJKQxN");
        validatePerformanceName(textField);
    }

    @Test(expected = ReservationSearchValidationException.class)
    public void validatePerformanceNameInvalidCharactersTest() throws ReservationSearchValidationException {
        TextField textField = new TextField();
        textField.setText("na*e");
        validatePerformanceName(textField);
    }


}
