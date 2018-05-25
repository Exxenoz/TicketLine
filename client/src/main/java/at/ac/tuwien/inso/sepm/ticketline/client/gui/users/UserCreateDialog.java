package at.ac.tuwien.inso.sepm.ticketline.client.gui.users;

import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
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
import java.util.List;

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
    public Label nameErrorLabel;

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

        UserDTO user = new UserDTO();
        user.setUsername(usernameTextField.getText());
        user.setPassword(passwordField.getText());
        user.setAuthorities(getRoles());

        validateUser(user);
    }

    private List<String> getRoles() {
        List<String> roles = new ArrayList<>();
        String selectedRole = String.valueOf(roleChoiceBox.getSelectionModel().getSelectedItem());

        // user role is implicit
        if(!selectedRole.equals("USER")) {
            roles.add(selectedRole);
        }
        return roles;
    }

    private void validateUser(UserDTO user) {
        boolean valid = true;

        if(!passwordRepeatField.getText().equals(user.getPassword())) {
            valid = false;
            LOGGER.debug("User validation failed: Password does not match");
            passwordRepeatErrorLabel.setText(BundleManager.getExceptionBundle().getString("exception.password_does_not_match"));
        } else {
            passwordRepeatErrorLabel.setText("");
        }

        if (!valid) {
            return;
        }

        // TODO: service.create()

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
