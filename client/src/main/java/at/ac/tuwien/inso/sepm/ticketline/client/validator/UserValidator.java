package at.ac.tuwien.inso.sepm.ticketline.client.validator;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.UserValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.exception.UserValidatorException;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class UserValidator {

    // all extended ascii characters allowed
    public static final String passwordRegex = "^[\\x00-\\xFF]*$";

    public static final String nameRegex = "^[-' a-zA-ZöüöäÜÖÄ0-9]+$";

    public static final String passwordChangeKexRegex = "^[1-9a-zA-Z]{8}$";

    public static String validateUsername(TextField usernameTextField) throws UserValidationException {
        String username = usernameTextField.getText();

        if(!username.matches(nameRegex)) {
            throw new UserValidationException(
                BundleManager.getExceptionBundle().getString("exception.validator.user.username.invalid_characters")
            );
        }

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

        return password;
    }

    public static void validatePasswordChangeKey(PasswordField passwordField) throws UserValidationException {
        if (!passwordField.getText().matches(passwordChangeKexRegex)) {
            throw new UserValidationException("");
        }
    }
}
