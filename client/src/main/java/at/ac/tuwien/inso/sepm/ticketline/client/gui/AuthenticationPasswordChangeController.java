package at.ac.tuwien.inso.sepm.ticketline.client.gui;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.AuthenticationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationRequest;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationTokenInfo;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.RowConstraints;
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

    private final AuthenticationService authenticationService;

    private final MainController mainController;

    public AuthenticationPasswordChangeController(AuthenticationService authenticationService, MainController mainController) {
        this.authenticationService = authenticationService;
        this.mainController = mainController;

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
                boolean passwordNotMatching = false;
                if(txtPassword.getText().isEmpty()) {
                    passwordMissingLabel.setVisible(true);
                    errorLabelRow2.setMinHeight(10);
                    passwordEmpty = true;
                } else if(!passwordsMatching()){
                    passwordNotMatchingLabel.setVisible(true);
                    errorLabelRow2.setMinHeight(10);
                    passwordNotMatching = true;
                }

                if(passwordKeyEmpty || passwordEmpty) {
                    throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.authenticate.credentials_missing"));
                } else if(passwordNotMatching) {
                    throw new DataAccessException(BundleManager.getBundle().getString("authenticate.password_not_matching"));
                }

                // TODO
                return authenticationService.authenticate(
                    AuthenticationRequest.builder()
                        .username(txtPasswordResetKey.getText())
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
}
