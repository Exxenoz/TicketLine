package at.ac.tuwien.inso.sepm.ticketline.client.validator;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.UserValidationException;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static at.ac.tuwien.inso.sepm.ticketline.client.validator.UserValidator.validatePassword;
import static at.ac.tuwien.inso.sepm.ticketline.client.validator.UserValidator.validatePasswordChangeKey;
import static at.ac.tuwien.inso.sepm.ticketline.client.validator.UserValidator.validateUsername;

@RunWith(SpringRunner.class)
@ActiveProfiles("unit-test")
public class UserValidatorTest {

    private final JFXPanel fxPanel = new JFXPanel();

    //VALIDATE USERNAME

    @Test
    public void validateUserNameTest() throws UserValidationException {
        TextField textField = new TextField();
        textField.setText("user");
        validateUsername(textField);
    }

    @Test(expected = UserValidationException.class)
    public void validateUserNameInvalidCharactersTest() throws UserValidationException {
        TextField textField = new TextField();
        textField.setText("§)($97--´´<");
        validateUsername(textField);
    }

    @Test(expected = UserValidationException.class)
    public void validateUserNameTooShortTest() throws UserValidationException {
        TextField textField = new TextField();
        textField.setText("AB");
        validateUsername(textField);
    }

    @Test(expected = UserValidationException.class)
    public void validateUserNameTooLongTest() throws UserValidationException {
        TextField textField = new TextField();
        textField.setText("ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJK");
        validateUsername(textField);
    }


    //VALIDATE PASSWORD

    @Test
    public void validatePasswordTest() throws UserValidationException {
        PasswordField passwordField = new PasswordField();
        passwordField.setText("password");
        validatePassword(passwordField, passwordField);
    }

    @Test(expected = UserValidationException.class)
    public void validatePasswordsNotTheSameTest() throws UserValidationException {
        PasswordField passwordField = new PasswordField();
        passwordField.setText("password");
        PasswordField passwordFieldRepeat = new PasswordField();
        passwordFieldRepeat.setText("anotherpassword");
        validatePassword(passwordField, passwordFieldRepeat);
    }

    @Test(expected = UserValidationException.class)
    public void validatePasswordTooShortTest() throws UserValidationException {
        PasswordField passwordField = new PasswordField();
        passwordField.setText("pa");
        validatePassword(passwordField, passwordField);
    }

    @Test(expected = UserValidationException.class)
    public void validatePasswordTooLongTest() throws UserValidationException {
        PasswordField passwordField = new PasswordField();
        passwordField.setText("veBE2yN1cXvyMQahtUfppjH5x3MioudGCNH");
        validatePassword(passwordField, passwordField);
    }


    //VALIDATE PASSWORD CHANGE KEY

    @Test
    public void validatePasswordChangeKeyTest() throws UserValidationException {
        PasswordField passwordField = new PasswordField();
        passwordField.setText("12dv45od");
        validatePasswordChangeKey(passwordField);
    }

    @Test(expected = UserValidationException.class)
    public void validatePasswordChangeKeyTooShortTest() throws UserValidationException {
        PasswordField passwordField = new PasswordField();
        passwordField.setText("12dv45o");
        validatePasswordChangeKey(passwordField);
    }

    @Test(expected = UserValidationException.class)
    public void validatePasswordChangeKeyTooLongTest() throws UserValidationException {
        PasswordField passwordField = new PasswordField();
        passwordField.setText("12dv45odd");
        validatePasswordChangeKey(passwordField);
    }

    @Test(expected = UserValidationException.class)
    public void validatePasswordChangeKeyInvalidCharactersTest() throws UserValidationException {
        PasswordField passwordField = new PasswordField();
        passwordField.setText("12dv45od*");
        validatePasswordChangeKey(passwordField);
    }
}
