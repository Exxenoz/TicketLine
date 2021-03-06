package at.ac.tuwien.inso.sepm.ticketline.client.gui.users;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.UserValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.client.validator.UserValidator;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserCreateRequestDTO;
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
import java.util.HashSet;
import java.util.Set;

@Component
public class UserCreateDialogController {

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

    public UserCreateDialogController(UserController userController, UserService userService) {
        this.userController = userController;
        this.userService = userService;
    }

    @FXML
    private void initialize() {
        LOGGER.info("Initialize UserCreateController");
        roleChoiceBox.getSelectionModel().select(0);
    }

    @FXML
    public void onClickCreateUserButton(ActionEvent actionEvent) {
        LOGGER.debug("User clicked create user button");

        boolean valid = true;
        UserCreateRequestDTO userCreateRequestDTO = new UserCreateRequestDTO();

        try {
            String username = UserValidator.validateUsername(usernameTextField);
            userCreateRequestDTO.setUsername(username);
            usernameErrorLabel.setText("");
        } catch (UserValidationException e) {
            valid = false;
            LOGGER.warn("User validation failed: " + e.getMessage());
            usernameErrorLabel.setText(e.getMessage());
        }

        try {
            String encryptedPassword = UserValidator.validatePassword(passwordField, passwordRepeatField);
            userCreateRequestDTO.setPassword(encryptedPassword);
            passwordErrorLabel.setText("");
        } catch (UserValidationException e) {
            valid = false;
            LOGGER.warn("User validation failed: " + e.getMessage());
            passwordErrorLabel.setText(e.getMessage());
        }

        usernameTextField.getScene().getWindow().sizeToScene();

        userCreateRequestDTO.setRoles(getRoles());

        if (!valid) {
            LOGGER.error("User was invalid");
            return;
        }

        try {
            userController.addUser(userService.create(userCreateRequestDTO));
        } catch (DataAccessException e) {
            LOGGER.error("User creation failed: {}", e.getMessage());

            JavaFXUtils.createErrorDialog(
                BundleManager.getBundle().getString("users.dialog.create.dialog.error.title"),
                BundleManager.getBundle().getString("users.dialog.create.dialog.error.header_text"),
                e.getMessage(),
                usernameTextField.getScene().getWindow()
            ).showAndWait();

            return;
        }

        ((Stage) usernameTextField.getScene().getWindow()).close();

        LOGGER.debug("User creation successfully completed!");

        JavaFXUtils.createInformationDialog(
            BundleManager.getBundle().getString("users.dialog.create.dialog.success.title"),
            BundleManager.getBundle().getString("users.dialog.create.dialog.success.header_text"),
            BundleManager.getBundle().getString("users.dialog.create.dialog.success.content_text"),
            usernameTextField.getScene().getWindow()
        ).showAndWait();
    }

    private Set<String> getRoles() {
        Set<String> roles = new HashSet<>();
        String selectedRole = String.valueOf(roleChoiceBox.getSelectionModel().getSelectedItem());

        roles.add("USER");
        roles.add(selectedRole);

        return roles;
    }
}
