package at.ac.tuwien.inso.sepm.ticketline.client.gui;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.UserValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.AuthenticationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.client.validator.UserValidator;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationRequest;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationTokenInfo;
import com.sun.javafx.tools.packager.bundlers.Bundler;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.RowConstraints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import java.lang.invoke.MethodHandles;

import static javafx.scene.control.ProgressIndicator.INDETERMINATE_PROGRESS;

@Component
public class AuthenticationController {


    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    public Label usernameLabel;

    @FXML
    public Label passwordLabel;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    public Label userNameMissingLabel;

    @FXML
    public Label passwordMissingLabel;

    @FXML
    public RowConstraints errorLabelRow1;

    @FXML
    public RowConstraints errorLabelRow2;

    @FXML
    public Button btnAuthenticate;

    private final AuthenticationService authenticationService;

    private final MainController mainController;

    public AuthenticationController(AuthenticationService authenticationService, MainController mainController) {
        this.authenticationService = authenticationService;
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {
        LOGGER.info("Initialize AuthenticationController");
        initI18N();
    }

    private void initI18N() {
        usernameLabel.textProperty().bind(BundleManager.getStringBinding("authenticate.username"));
        passwordLabel.textProperty().bind(BundleManager.getStringBinding("authenticate.password"));
        txtUsername.promptTextProperty().bind(BundleManager.getStringBinding("authenticate.username"));
        txtPassword.promptTextProperty().bind(BundleManager.getStringBinding("authenticate.password"));
        userNameMissingLabel.textProperty().bind(BundleManager.getStringBinding("authenticate.username_missing"));
        passwordMissingLabel.textProperty().bind(BundleManager.getStringBinding("authenticate.password_missing"));
        btnAuthenticate.textProperty().bind(BundleManager.getStringBinding("authenticate.authenticate"));
    }

    @FXML
    private void handleAuthenticate(ActionEvent actionEvent) {
        LOGGER.info("User clicked the authenticate button");
        final var task = new Task<AuthenticationTokenInfo>() {
            @Override
            protected AuthenticationTokenInfo call() throws DataAccessException {
                userNameMissingLabel.setVisible(false);
                passwordMissingLabel.setVisible(false);
                errorLabelRow1.setMinHeight(0);
                errorLabelRow2.setMinHeight(0);

                boolean usernameEmpty = false;
                if(txtUsername.getText().isEmpty()) {
                    userNameMissingLabel.setVisible(true);
                    errorLabelRow1.setMinHeight(10);
                    usernameEmpty = true;
                }

                boolean passwordEmpty = false;
                if(txtPassword.getText().isEmpty()) {
                    passwordMissingLabel.setVisible(true);
                    errorLabelRow2.setMinHeight(10);
                    passwordEmpty = true;
                }

                if(usernameEmpty || passwordEmpty) {
                    throw new DataAccessException(BundleManager.getExceptionBundle().getString("exception.authentication.missing_credential"));
                }

                return authenticationService.authenticate(
                    AuthenticationRequest.builder()
                        .username(txtUsername.getText())
                        .password(txtPassword.getText())
                        .build());
            }

            @Override
            protected void failed() {
                super.failed();

                if(!credentialsEmpty()) {
                    if (getException().getCause() != null &&
                        getException().getCause().getClass() == HttpClientErrorException.class) {
                        var httpErrorCode = ((HttpStatusCodeException) getException().getCause()).getStatusCode();
                        if (httpErrorCode == HttpStatus.LOCKED) {
                            try {
                                UserValidator.validatePasswordChangeKey(txtPassword);
                                // if password is in valid change key format -> forward to next form
                                mainController.switchToNewPasswordAuthentication(txtUsername.getText(), txtPassword.getText());
                            } catch (UserValidationException e) {
                                // otherwise send an empty string
                                mainController.switchToNewPasswordAuthentication(txtUsername.getText(), "");
                            }

                            return;
                        }
                    }

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
        return txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty();
    }

}