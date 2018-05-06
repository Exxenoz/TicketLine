package at.ac.tuwien.inso.sepm.ticketline.client.gui;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.AuthenticationService;
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
import org.springframework.stereotype.Component;

import static javafx.scene.control.ProgressIndicator.INDETERMINATE_PROGRESS;

@Component
public class AuthenticationController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    public Label userNameMissingLabel;

    @FXML
    public Label passwordMissingLabel;

    private final AuthenticationService authenticationService;

    private final MainController mainController;

    public AuthenticationController(AuthenticationService authenticationService, MainController mainController) {
        this.authenticationService = authenticationService;
        this.mainController = mainController;

    }

    @FXML
    private void handleAuthenticate(ActionEvent actionEvent) {
        final var task = new Task<AuthenticationTokenInfo>() {
            @Override
            protected AuthenticationTokenInfo call() throws DataAccessException {
                userNameMissingLabel.setVisible(false);
                passwordMissingLabel.setVisible(false);

                boolean usernameEmpty = false;
                if(txtUsername.getText().isEmpty()) {
                    userNameMissingLabel.setVisible(true);
                    usernameEmpty = true;
                }

                boolean passwordEmpty = false;
                if(txtPassword.getText().isEmpty()) {
                    passwordMissingLabel.setVisible(true);
                    passwordEmpty = true;
                }

                if(usernameEmpty || passwordEmpty) {
                    throw new DataAccessException("Missing credentials");
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