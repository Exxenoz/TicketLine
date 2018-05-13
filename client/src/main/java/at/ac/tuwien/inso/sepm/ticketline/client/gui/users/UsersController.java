package at.ac.tuwien.inso.sepm.ticketline.client.gui.users;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.InvalidObjectDataException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.client.validation.UserValidator;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import java.lang.invoke.MethodHandles;
import java.util.List;

import static javafx.scene.control.ProgressIndicator.INDETERMINATE_PROGRESS;
import static org.controlsfx.glyphfont.FontAwesome.Glyph.USERS;

@Component
public class UsersController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int HEADER_ICON_SIZE = 25;
    private final MainController mainController;
    private final UserService userService;
    @FXML
    public TableColumn<UserDTO, Integer> userAuthTriesCol;
    @FXML
    private Label lblHeaderTitle;
    @FXML
    private Label lblHeaderIcon;
    @FXML
    private AnchorPane content;
    @FXML
    private TableView<UserDTO> userTable;
    @FXML
    private TableColumn<UserDTO, String> usernameCol;
    @FXML
    private TableColumn<UserDTO, String> useraccountStatusCol;
    @FXML
    private TabHeaderController tabHeaderController;

    public UsersController(MainController mainController, UserService userService) {
        this.mainController = mainController;
        this.userService = userService;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(USERS);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("usertab.header"));
    }

    public void loadUsers() {
        final var task = new Task<List<UserDTO>>() {
            @Override
            protected List<UserDTO> call() throws DataAccessException {
                return userService.findAll();
            }


            @Override
            protected void succeeded() {
                super.succeeded();

                usernameCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                    cellData.getValue().getUsername())
                );
                useraccountStatusCol.setCellValueFactory(cellData -> {
                    if (cellData.getValue().isEnabled()) {
                        return new SimpleStringProperty(BundleManager.getBundle().getString(
                            "usertab.usertable.user_is_enabled.true"));
                    } else {
                        return new SimpleStringProperty(BundleManager.getBundle().getString(
                            "usertab.usertable.user_is_enabled.false"));
                    }
                });
                userAuthTriesCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(
                    cellData.getValue().getStrikes()).asObject()
                );
                userTable.setItems(FXCollections.observableArrayList(getValue()));

                userTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            }


            @Override
            protected void failed() {
                super.failed();
                if ((getException().getCause().getClass()) == HttpClientErrorException.class) {
                    var httpErrorCode = ((HttpStatusCodeException) getException().getCause()).getStatusCode();
                    if (httpErrorCode == HttpStatus.FORBIDDEN) {
                        mainController.getTpContent().getTabs().get(2).setDisable(true);
                    }
                } else {
                    JavaFXUtils.createExceptionDialog(getException(),
                        content.getScene().getWindow()).showAndWait();
                }
            }
        };
        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(running ? INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();

    }

    public void handleEnable(javafx.event.ActionEvent actionEvent) {
        try {
            UserDTO userDTO = userTable.getSelectionModel().getSelectedItem();
            if (!UserValidator.validateUser(userDTO)) {
                throw new InvalidObjectDataException("Invalid Data in the UserDTO object");
            } else {
                userService.enableUser(userDTO);
            }
        } catch (DataAccessException e) {
            JavaFXUtils.createErrorDialog(e.getMessage(),
                content.getScene().getWindow()).showAndWait();
        } catch (InvalidObjectDataException e) {
            LOGGER.error("The Data of the UserDTO object was invalid");
            JavaFXUtils.createErrorDialog(BundleManager.getExceptionBundle().getString("exception.no_selected_user"),
                content.getScene().getWindow()).showAndWait();
        }
        this.loadUsers();
    }
}
