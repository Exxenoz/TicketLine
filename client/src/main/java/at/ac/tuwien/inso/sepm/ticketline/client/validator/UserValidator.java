package at.ac.tuwien.inso.sepm.ticketline.client.validator;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.UserValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.exception.UserValidatorException;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserValidator {

    // all extended ascii characters allowed
    public static final String passwordRegex = "^[\\x00-\\xFF]*$";

    public static String validateUsername(TextField usernameTextField) throws UserValidationException {
        String username = usernameTextField.getText();

        try {
            at.ac.tuwien.inso.sepm.ticketline.rest.validator.UserValidator.validateUsername(
                UserDTO.builder().username(username).build());
        } catch (UserValidatorException e) {
            throw new UserValidationException(
                BundleManager.getExceptionBundle().getString("exception.validator.user.username.invalid"));
        }

        return username;
    }

    public static String validatePassword(PasswordField passwordField, PasswordField passwordRepeatField) throws UserValidationException {
        String password = passwordField.getText();
        String passwordRepeat = passwordRepeatField.getText();

        if (!password.equals(passwordRepeat)) {
            throw new UserValidationException(
                BundleManager.getExceptionBundle().getString("exception.validator.user.password_does_not_match"));
        }

        if(password.length() < 3 || password.length() > 30) {
            throw new UserValidationException(
                BundleManager.getExceptionBundle().getString("exception.validator.user.password_length_invalid"));

        }

        if (!password.matches(passwordRegex)) {
            throw new UserValidationException(
                BundleManager.getExceptionBundle().getString("exception.validator.user.password_characters_invalid"));
        }

        // TODO: remove and move encryption to server
        // encrypt password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        String encryptedPassword = passwordEncoder.encode(password);

        try {
            at.ac.tuwien.inso.sepm.ticketline.rest.validator.UserValidator.validateEncryptedPassword(
                UserDTO.builder().password(encryptedPassword).build());
        } catch (UserValidatorException e) {
            throw new UserValidationException(BundleManager.getExceptionBundle().getString("exception.unexpected"));
        }

        return encryptedPassword;
    }
}
