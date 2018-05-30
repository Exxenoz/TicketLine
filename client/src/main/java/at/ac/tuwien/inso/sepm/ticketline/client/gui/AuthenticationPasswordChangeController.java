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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.RowConstraints;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static javafx.scene.control.ProgressIndicator.INDETERMINATE_PROGRESS;

@Component
public class AuthenticationPasswordChangeController {

    @FXML
    public RowConstraints errorLabelRow1;

    @FXML
    public RowConstraints errorLabelRow2;

    @FXML
    private TextField txtPasswordResetKey;

    @FXML
    private PasswordField txtPassword;

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

    public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    private void handleAuthenticate(ActionEvent actionEvent) {
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