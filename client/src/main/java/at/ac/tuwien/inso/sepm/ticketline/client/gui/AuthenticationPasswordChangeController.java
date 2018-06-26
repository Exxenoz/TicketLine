package at.ac.tuwien.inso.sepm.ticketline.client.gui;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.UserValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.AuthenticationService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.client.validator.UserValidator;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationRequest;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationTokenInfo;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserPasswordChangeRequestDTO;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.RowConstraints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

import static javafx.scene.control.ProgressIndicator.INDETERMINATE_PROGRESS;

@Component
public class AuthenticationPasswordChangeController {


    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    public Label passwordResetLabel;

    @FXML
    public Label passwordResetKeyInstructionsLabel;

    @FXML
    public RowConstraints errorLabelRow1;

    @FXML
    public RowConstraints errorLabelRow2;

    @FXML
    public ButtonBar buttonBar;

    @FXML
    public Button backButton;

    @FXML
    public Label passwordResetKeyLabel;

    @FXML
    private TextField txtPasswordResetKey;

    @FXML
    public Label passwordLabel;

    @FXML
    private PasswordField txtPassword;

    @FXML
    public Label passwordRepeatLabel;

    @FXML
    private PasswordField txtPasswordRepeat;

    @FXML
    public Label passwordResetKeyMissingLabel;

    @FXML
    public Label passwordMissingLabel;

    @FXML
    public Label passwordNotMatchingLabel;

    @FXML
    public Button btnAuthenticate;

    private final AuthenticationService authenticationService;
    private final UserService userService;

    private String username;

    private final MainController mainController;

    public AuthenticationPasswordChangeController(AuthenticationService authenticationService, UserService userService, MainController mainController) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {
        ButtonBar.setButtonUniformSize(backButton, false);
        ButtonBar.setButtonUniformSize(btnAuthenticate, false);

        initI18N();
    }

    private void initI18N() {
        passwordResetLabel.textProperty().bind(BundleManager.getStringBinding("authenticate.password_reset"));
        passwordResetKeyInstructionsLabel.textProperty().bind(BundleManager.getStringBinding("authenticate.password_reset_key_instructions"));

        passwordResetKeyLabel.textProperty().bind(BundleManager.getStringBinding("authenticate.password_reset_key"));
        passwordLabel.textProperty().bind(BundleManager.getStringBinding("authenticate.password"));
        passwordRepeatLabel.textProperty().bind(BundleManager.getStringBinding("authenticate.password_repeat"));

        txtPasswordResetKey.promptTextProperty().bind(BundleManager.getStringBinding("authenticate.password_reset_key"));
        txtPassword.promptTextProperty().bind(BundleManager.getStringBinding("authenticate.password"));
        txtPasswordRepeat.promptTextProperty().bind(BundleManager.getStringBinding("authenticate.password"));

        passwordResetKeyMissingLabel.textProperty().bind(BundleManager.getStringBinding("authenticate.password_reset_key_missing"));
        passwordMissingLabel.textProperty().bind(BundleManager.getStringBinding("authenticate.password_missing"));
        passwordNotMatchingLabel.textProperty().bind(BundleManager.getStringBinding("authenticate.password_not_matching"));

        backButton.textProperty().bind(BundleManager.getStringBinding("authenticate.password_change.back"));
        btnAuthenticate.textProperty().bind(BundleManager.getStringBinding("authenticate.submit"));
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordChangeKey(String passwordChangeKey) {
        txtPasswordResetKey.setText(passwordChangeKey);
    }

    @FXML
    private void handleAuthenticate(ActionEvent actionEvent) {
        LOGGER.debug("User clicked the reset button");
        final var task = new Task<AuthenticationTokenInfo>() {
            @Override
            protected AuthenticationTokenInfo call() throws DataAccessException {
                passwordResetKeyMissingLabel.setVisible(false);
                passwordMissingLabel.setVisible(false);
                passwordNotMatchingLabel.setVisible(false);
                errorLabelRow1.setMinHeight(0);
                errorLabelRow2.setMinHeight(0);

                boolean passwordKeyEmpty = false;
                if(txtPasswordResetKey.getText().isEmpty()) {
                    passwordResetKeyMissingLabel.setVisible(true);
                    errorLabelRow1.setMinHeight(10);
                    passwordKeyEmpty = true;
                }

                boolean passwordEmpty = false;
                if(txtPassword.getText().isEmpty()) {
                    passwordMissingLabel.setVisible(true);
                    errorLabelRow2.setMinHeight(10);
                    passwordEmpty = true;
                } else if(!passwordsMatching()){
                    passwordNotMatchingLabel.setVisible(true);
                    errorLabelRow2.setMinHeight(10);
                }

                if(passwordKeyEmpty || passwordEmpty) {
                    throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.authenticate.credentials_missing"));
                }

                try {
                    UserValidator.validatePassword(txtPassword, txtPasswordRepeat);
                } catch (UserValidationException e) {
                    throw new DataAccessException(e.getMessage());
                }

                userService.changePassword(UserPasswordChangeRequestDTO.builder().
                    passwordChangeKey(txtPasswordResetKey.getText()).
                    username(username).
                    password(txtPassword.getText()).
                    build());

                return authenticationService.authenticate(
                    AuthenticationRequest.builder()
                        .username(username)
                        .password(txtPassword.getText())
                        .build());
            }

            @Override
            protected void failed() {
                super.failed();

                if(!credentialsEmpty() && passwordsMatching()) {
                    JavaFXUtils.createErrorDialog(getException().getMessage(),
                        ((Node) actionEvent.getTarget()).getScene().getWindow()).showAndWait();
                }
            }
        };
        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(running ? INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();
    }

    private boolean credentialsEmpty() {
        return txtPasswordResetKey.getText().isEmpty() || txtPassword.getText().isEmpty();
    }

    private boolean passwordsMatching() {
        return txtPassword.getText().equals(txtPasswordRepeat.getText());
    }

    public void handleBackToAuthentication(ActionEvent actionEvent) {
        mainController.switchBackToAuthentication();
    }

}
