package at.ac.tuwien.inso.sepm.ticketline.client.gui.users;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.UserValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.client.validator.UserValidator;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class UserCreateDialog {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    public TextField usernameTextField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public PasswordField passwordRepeatField;

    @FXML
    public ChoiceBox roleChoiceBox;

    @FXML
    public Label usernameErrorLabel;

    @FXML
    public Label passwordErrorLabel;

    @FXML
    public Label passwordRepeatErrorLabel;

    @FXML
    public Label roleErrorLabel;

    private final UserController userController;
    private final UserService userService;

    private UserDTO userDTO;

    public UserCreateDialog(UserController userController, UserService userService) {
        this.userController = userController;
        this.userService = userService;
    }

    @FXML
    private void initialize() {
        roleChoiceBox.getSelectionModel().select(0);
    }

    @FXML
    public void onClickCreateUserButton(ActionEvent actionEvent) {
        LOGGER.debug("Clicked create user button");

    }

    private Set<String> getRoles() {
        Set<String> roles = new HashSet<>();
        String selectedRole = String.valueOf(roleChoiceBox.getSelectionModel().getSelectedItem());

        // user role is implicit
        if(!selectedRole.equals("USER")) {
            roles.add(selectedRole);
        }
        return roles;
    }

    private void validateUser() {
        boolean valid = true;
        UserDTO userDTO = new UserDTO();

        try {
            String username = UserValidator.validateUsername(usernameTextField);
            userDTO.setUsername(username);
            usernameErrorLabel.setText("");
        } catch (UserValidationException e) {
            valid = false;
            LOGGER.debug("User validation failed: " + e.getMessage());
            passwordRepeatErrorLabel.setText(e.getMessage());
        }

        try {
            String encryptedPassword = UserValidator.validatePassword(passwordField, passwordRepeatField);
            userDTO.setPassword(encryptedPassword);
            passwordRepeatErrorLabel.setText("");
        } catch (UserValidationException e) {
            valid = false;
            LOGGER.debug("User validation failed: " + e.getMessage());
            passwordRepeatErrorLabel.setText(e.getMessage());
        }

        userDTO.setRoles(getRoles());

        if (!valid) {
            return;
        }

        try {
            userService.create(userDTO);
        } catch (DataAccessException e) {
            LOGGER.error("User creation failed: " + e.getMessage());

            JavaFXUtils.createErrorDialog(
                BundleManager.getBundle().getString("users.dialog.create.dialog.error.title"),
                BundleManager.getBundle().getString("users.dialog.create.dialog.error.header_text"),
                BundleManager.getBundle().getString("users.dialog.create.dialog.error.content_text"),
                usernameTextField.getScene().getWindow()
            ).showAndWait();

            return;
        }

        LOGGER.debug("User creation successfully completed!");

        JavaFXUtils.createInformationDialog(
            BundleManager.getBundle().getString("users.dialog.create.dialog.success.title"),
            BundleManager.getBundle().getString("users.dialog.create.dialog.success.header_text"),
            BundleManager.getBundle().getString("users.dialog.create.dialog.success.content_text"),
            usernameTextField.getScene().getWindow()
        ).showAndWait();

        ((Stage) usernameTextField.getScene().getWindow()).close();
    }
}
