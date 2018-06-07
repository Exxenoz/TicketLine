package at.ac.tuwien.inso.sepm.ticketline.client.gui.users;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import java.lang.invoke.MethodHandles;
import java.security.SecureRandom;

import static javafx.stage.Modality.APPLICATION_MODAL;
import static org.controlsfx.glyphfont.FontAwesome.Glyph.LOCK;

@Component
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final int USERS_PER_PAGE = 50;
    public static final int FIRST_USER_TABLE_PAGE = 0;

    @FXML
    private VBox content;

    @FXML
    private TableView<UserDTO> userTable;

    @FXML
    private TableColumn<UserDTO, String> usernameCol;

    @FXML
    private TableColumn<UserDTO, String> useraccountStatusCol;

    @FXML
    public TableColumn<UserDTO, Integer> userAuthTriesCol;

    @FXML
    public Button toggleEnableButton;

    @FXML
    public Button passwordResetButton;

    @FXML
    private TabHeaderController tabHeaderController;

    private ScrollBar verticalScrollbar;

    private final SpringFxmlLoader springFxmlLoader;
    private final MainController mainController;
    private final UserService userService;

    private ObservableList<UserDTO> items;
    private int page = 0;
    private int totalPages = 1;

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

        initializeUserTable();
    }

    private void initializeUserTable() {
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

        userTable.setItems(items);
    }

    // TODO: call when ui was loaded/currently ther visibility wont be updated until authentication is done
    private void loadPageUntilTableIsFilled(int page) {
        if(loadUserTable(page)) {
            var scrollbar = getVerticalScrollbar(userTable);
            if (!scrollbar.visibleProperty().get()) {
                loadPageUntilTableIsFilled(page + 1);
            }
        }
    }

    public void loadUsers() {
        final ScrollBar scrollBar = getVerticalScrollbar(userTable);
        if (scrollBar != null) {
            scrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
                double scrollValue = newValue.doubleValue();
                if (scrollValue == scrollBar.getMax() && (page + 1) < totalPages) {
                    double targetValue = scrollValue * items.size();
                    loadUserTable(page + 1);
                    scrollBar.setValue(targetValue / items.size());
                }
            });

            verticalScrollbar.visibleProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (newValue == false) {
                    // Scrollbar is invisible, load pages until scrollbar is shown again
                    loadUserTable(page + 1);
                }
            });
        }

        // Setting event handler for sort policy property calls it automatically
        userTable.sortPolicyProperty().set(t -> {
            items.clear();
            loadUserTable(FIRST_USER_TABLE_PAGE);
            return true;
        });
    }

    private ScrollBar getVerticalScrollbar(TableView<?> table) {
        if(verticalScrollbar == null) {
            ScrollBar result = null;
            for (Node n : table.lookupAll(".scroll-bar")) {
                if (n instanceof ScrollBar) {
                    ScrollBar bar = (ScrollBar) n;
                    if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                        verticalScrollbar = bar;

                        break;
                    }
                }
            }
        }

        return verticalScrollbar;
    }

    private String getColumnNameBy(TableColumn<UserDTO, ?> tableColumn) {
        if (tableColumn == usernameCol) {
            return "username";
        }
        else if (tableColumn == useraccountStatusCol) {
            return "enabled";
        }
        else if (tableColumn == userAuthTriesCol) {
            return "strikes";
        }

        return "id";
    }

    public boolean loadUserTable(int page) {
        if (page < 0 || page >= totalPages) {
            LOGGER.error("Could not load user table page, because page parameter is invalid: " + page);
            return false;
        }

        PageRequestDTO pageRequestDTO = null;
        if (userTable.getSortOrder().size() > 0) {
            TableColumn<UserDTO, ?> sortedColumn = userTable.getSortOrder().get(0);
            Sort.Direction sortDirection = (sortedColumn.getSortType() == TableColumn.SortType.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC;
            pageRequestDTO = new PageRequestDTO(page, USERS_PER_PAGE, sortDirection, getColumnNameBy(sortedColumn));
        }
        else {
            pageRequestDTO = new PageRequestDTO(page, USERS_PER_PAGE, Sort.Direction.ASC, null);
        }

        this.page = page;

        try {
            PageResponseDTO<UserDTO> response = userService.findAll(pageRequestDTO);
            this.totalPages = response.getTotalPages() > 0 ? response.getTotalPages() : 1;
            items.addAll(response.getContent());
        } catch (DataAccessException e) {
            if ((e.getCause().getClass()) == HttpClientErrorException.class) {
                var httpErrorCode = ((HttpStatusCodeException) e.getCause()).getStatusCode();
                if (httpErrorCode == HttpStatus.FORBIDDEN) {
                    LOGGER.debug("The current user doesnt have the authorization to load the users-list");
                    mainController.getTpContent().getTabs().get(4).setDisable(true);
                } else {
                    JavaFXUtils.createExceptionDialog(e,
                        content.getScene().getWindow()).showAndWait();
                }
            } else {
                JavaFXUtils.createExceptionDialog(e,
                    content.getScene().getWindow()).showAndWait();
            }
        }

        userTable.refresh();
        return true;
    }

    public void refreshUserTable() {
        userTable.refresh();
    }

    public void clearUserList() {
        items.clear();

        ScrollBar scrollBar = getVerticalScrollbar(userTable);
        if (scrollBar != null) {
            scrollBar.setValue(0);
        }
    }

    public void toggleEnable(javafx.event.ActionEvent actionEvent) {
        LOGGER.debug("Clicked toggle user button");
        try {
            UserDTO userDTO = userTable.getSelectionModel().getSelectedItem();
            UserValidator.validateExistingUser(userDTO);
            if (userDTO.isEnabled()) {
                LOGGER.debug("Trying to disable user");
                userService.disableUser(userDTO);
                userDTO.setEnabled(false);
                toggleEnableButton.setText(BundleManager.getBundle().getString("usertab.user.enable"));
            } else {
                LOGGER.debug("Trying to enable user");
                userService.enableUser(userDTO);
                userDTO.setEnabled(true);
                toggleEnableButton.setText(BundleManager.getBundle().getString("usertab.user.disable"));
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

        userTable.refresh();
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
            LOGGER.error("User not valid / No User was selected");
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
            UserDTO updatedUserDTO = userService.resetPassword(userPasswordResetRequestDTO);

            if (updatedUserDTO != null) {
                userDTO.update(updatedUserDTO);
                userTable.refresh();
            }

            LOGGER.error("Password reset was successful");

            JavaFXUtils.createCopyTextDialog(
                BundleManager.getBundle().getString("usertab.password_reset.dialog.success.title"),
                BundleManager.getBundle().getString("usertab.password_reset.dialog.success.header_text") + " " + userDTO.getUsername(),
                BundleManager.getBundle().getString("usertab.password_reset.dialog.success.content_text"),
                resetKey,
                passwordResetButton.getScene().getWindow()
            ).showAndWait();
        } catch (DataAccessException e) {
            JavaFXUtils.createErrorDialog(e.getMessage(),
                content.getScene().getWindow()).showAndWait();
        }
    }

    // https://stackoverflow.com/a/157202
    private String generateResetKey() {
        final String AB = "123456789ABCDEFGHIJKLMNPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(8);
        for(int i = 0; i < 8; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
}
