package at.ac.tuwien.inso.sepm.ticketline.client.gui.users;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.customers.CustomerController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.exception.UserValidatorException;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserPasswordResetRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.validator.UserValidator;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import java.lang.invoke.MethodHandles;
import java.util.UUID;

import static javafx.stage.Modality.APPLICATION_MODAL;
import static org.controlsfx.glyphfont.FontAwesome.Glyph.LOCK;

@Component
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    public TableColumn<UserDTO, Integer> userAuthTriesCol;

    @FXML
    private VBox content;

    @FXML
    private TableView<UserDTO> userTable;

    @FXML
    private TableColumn<UserDTO, String> usernameCol;

    @FXML
    private TableColumn<UserDTO, String> useraccountStatusCol;

    @FXML
    public Button toggleEnableButton;

    @FXML
    public Button passwordResetButton;

    @FXML
    private TabHeaderController tabHeaderController;


    private final SpringFxmlLoader springFxmlLoader;
    private final MainController mainController;
    private final UserService userService;

    private static final int USERS_PER_PAGE = 30;
    private ObservableList<UserDTO> items;
    private int page = 0;
    private int totalPages = 0;

    public UserController(SpringFxmlLoader springFxmlLoader, MainController mainController, UserService userService) {
        this.springFxmlLoader = springFxmlLoader;
        this.mainController = mainController;
        this.userService = userService;
        this.items = FXCollections.observableArrayList();
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(LOCK);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("usertab.header"));
        registerTableSelectionListener();
    }

    private void registerTableSelectionListener() {
        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(newSelection != null) {
                // update button text according to enabled status of selected user
                String toggleEnableButtonText = "";
                if (newSelection.isEnabled()) {
                    toggleEnableButtonText = BundleManager.getBundle().getString("usertab.user.disable");
                } else {
                    toggleEnableButtonText = BundleManager.getBundle().getString("usertab.user.enable");
                }
                toggleEnableButton.setText(toggleEnableButtonText);
                toggleEnableButton.setDisable(false);
                passwordResetButton.setDisable(false);
            } else {
                // no selection, disable toogle button and password reset button
                toggleEnableButton.setDisable(true);
                passwordResetButton.setDisable(true);
            }
        });
    }

    private void scrolled(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        double value = newValue.doubleValue();
        ScrollBar bar = getVerticalScrollbar(userTable);
        if ((value == bar.getMax()) && (page < totalPages)) {
            page++;
            LOGGER.debug("Getting next Page: {}", page);
            double targetValue = value * items.size();
            loadUsers(page);
            bar.setValue(targetValue / items.size());
        }
    }

    public void loadUsers(int page) {
        ScrollBar bar = getVerticalScrollbar(userTable);
        bar.valueProperty().addListener(this::scrolled);
        LOGGER.debug("Loading Users of page {}", page);
        try {
            PageRequestDTO request = new PageRequestDTO(page, USERS_PER_PAGE, null, null);
            PageResponseDTO<UserDTO> response = userService.findAll(request);
            items.addAll(response.getContent());
            totalPages = response.getTotalPages();
        } catch (DataAccessException e) {
            if ((e.getCause().getClass()) == HttpClientErrorException.class) {
                var httpErrorCode = ((HttpStatusCodeException) e.getCause()).getStatusCode();
                if (httpErrorCode == HttpStatus.FORBIDDEN) {
                    LOGGER.debug("The current user doesnt have the authorization to load the users-list");
                    mainController.getTpContent().getTabs().get(2).setDisable(true);
                } else {
                    JavaFXUtils.createExceptionDialog(e,
                        content.getScene().getWindow()).showAndWait();
                }
            } else {
                JavaFXUtils.createExceptionDialog(e,
                    content.getScene().getWindow()).showAndWait();
            }
        }

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
        LOGGER.debug("loading the page into the table");
        userTable.setItems(items);
    }

    private ScrollBar getVerticalScrollbar(TableView<?> table) {
        ScrollBar result = null;
        for (Node n : table.lookupAll(".scroll-bar")) {
            if (n instanceof ScrollBar) {
                ScrollBar bar = (ScrollBar) n;
                if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                    result = bar;
                }
            }
        }
        return result;
    }

    private void clear() {
        LOGGER.debug("clearing the data");
        items = FXCollections.observableArrayList();
        page = 0;
        this.getVerticalScrollbar(userTable).setValue(0);
    }

    public void toggleEnable(javafx.event.ActionEvent actionEvent) {
        LOGGER.debug("Clicked toggle user button");
        try {
            UserDTO userDTO = userTable.getSelectionModel().getSelectedItem();
            UserValidator.validateExistingUser(userDTO);
            if (userDTO.isEnabled()) {
                LOGGER.debug("Trying to disable user");
                userService.disableUser(userDTO);
            } else {
                LOGGER.debug("Trying to enable user");
                userService.enableUser(userDTO);
            }
        } catch (DataAccessException e) {
            String errorMessage = e.getMessage();
            if ((e.getCause().getClass()) == HttpClientErrorException.class) {
                var httpErrorCode = ((HttpStatusCodeException) e.getCause()).getStatusCode();
                if (httpErrorCode == HttpStatus.FORBIDDEN) {
                    LOGGER.debug("Cannot disable own account");
                    errorMessage = BundleManager.getExceptionBundle().getString("exception.user.disable_self");
                }
            }

            JavaFXUtils.createErrorDialog(errorMessage,
                content.getScene().getWindow()).showAndWait();
        } catch (UserValidatorException e) {
            LOGGER.error("No User was selected");
            JavaFXUtils.createErrorDialog(BundleManager.getExceptionBundle().getString("exception.no_selected_user"),
                content.getScene().getWindow()).showAndWait();
        }
        this.clear();
        userTable.refresh();
        loadUsers(0);
    }

    public void onClickCreateUserButton(ActionEvent actionEvent) {
        LOGGER.debug("Clicked create user button");

        final var stage = (Stage) userTable.getScene().getWindow();
        final var dialog = new Stage();

        dialog.getIcons().add(new Image(UserController.class.getResourceAsStream("/image/ticketlineIcon.png")));
        dialog.setResizable(false);
        dialog.initModality(APPLICATION_MODAL);
        dialog.initOwner(stage);
        dialog.setScene(new Scene(springFxmlLoader.load("/fxml/users/userCreateDialog.fxml")));
        dialog.setTitle(BundleManager.getBundle().getString("usertab.user.create"));
        dialog.showAndWait();
    }

    public void onClickResetPassword(ActionEvent actionEvent) {
        LOGGER.debug("Clicked reset user password button");
        UserDTO userDTO = userTable.getSelectionModel().getSelectedItem();

        try {
            UserValidator.validateExistingUser(userDTO);
        } catch (UserValidatorException e) {
            LOGGER.error("No User was selected");
            JavaFXUtils.createErrorDialog(BundleManager.getExceptionBundle().getString("exception.no_selected_user"),
                content.getScene().getWindow()).showAndWait();
            return;
        }

        String resetKey = generateResetKey();

        UserPasswordResetRequestDTO userPasswordResetRequestDTO =
            UserPasswordResetRequestDTO.builder().
                passwordChangeKey(resetKey).
                userDTO(userDTO).
                build();

        try {
            userService.resetPassword(userPasswordResetRequestDTO);
            LOGGER.error("Password reset was successful");
            JavaFXUtils.createInformationDialog(
                BundleManager.getBundle().getString("usertab.password_reset.dialog.success.title"),
                BundleManager.getBundle().getString("usertab.password_reset.dialog.success.header_text") + " " + userDTO.getUsername(),
                BundleManager.getBundle().getString("usertab.password_reset.dialog.success.content_text") + " " + resetKey,
                passwordResetButton.getScene().getWindow()
            ).showAndWait();
        } catch (DataAccessException e) {
            JavaFXUtils.createErrorDialog(e.getMessage(),
                content.getScene().getWindow()).showAndWait();
        }
    }

    private String generateResetKey() {
        return UUID.randomUUID().toString();
    }
}
