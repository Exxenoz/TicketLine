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
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
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
    public Button createUserButton;

    @FXML
    private TabHeaderController tabHeaderController;

    private ScrollBar verticalScrollbar;

    private final SpringFxmlLoader springFxmlLoader;
    private final MainController mainController;
    private final UserService userService;

    private ObservableList<UserDTO> userList;
    private int page = 0;
    private int totalPages = 1;

    private TableColumn sortedColumn;

    public UserController(SpringFxmlLoader springFxmlLoader, MainController mainController, UserService userService) {
        this.springFxmlLoader = springFxmlLoader;
        this.mainController = mainController;
        this.userService = userService;
        this.userList = FXCollections.observableArrayList();
    }

    @FXML
    private void initialize() {
        LOGGER.info("Initialize UserController");
        tabHeaderController.setIcon(LOCK);
        tabHeaderController.setTitleBinding(BundleManager.getStringBinding("usertab.header"));

        ButtonBar.setButtonUniformSize(toggleEnableButton, false);
        ButtonBar.setButtonUniformSize(passwordResetButton, false);
        ButtonBar.setButtonUniformSize(createUserButton, false);

        initI18N();
        initializeUserTable();
    }

    private void initI18N() {
        usernameCol.textProperty().bind(BundleManager.getStringBinding("usertab.usertable.username"));
        useraccountStatusCol.textProperty().bind(BundleManager.getStringBinding("usertab.usertable.user_is_enabled"));
        userAuthTriesCol.textProperty().bind(BundleManager.getStringBinding("usertab.usertable.user_auth_tries"));
        passwordResetButton.textProperty().bind(BundleManager.getStringBinding("usertab.user.password_reset"));
        toggleEnableButton.textProperty().bind(BundleManager.getStringBinding("usertab.user.enable"));
        createUserButton.textProperty().bind(BundleManager.getStringBinding("usertab.user.create"));
    }

    private void initializeUserTable() {
        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(newSelection != null) {
                // update button text according to enabled status of selected user
                StringBinding toggleEnableButtonBinding = null;
                if (newSelection.isEnabled()) {
                    toggleEnableButtonBinding = BundleManager.getStringBinding("usertab.user.disable");
                } else {
                    toggleEnableButtonBinding = BundleManager.getStringBinding("usertab.user.enable");
                }
                toggleEnableButton.textProperty().bind(toggleEnableButtonBinding);
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
                return BundleManager.getStringBinding("usertab.usertable.user_is_enabled.true");
            } else {
                return BundleManager.getStringBinding("usertab.usertable.user_is_enabled.false");
            }
        });
        userAuthTriesCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(
            cellData.getValue().getStrikes()).asObject()
        );

        userTable.setItems(userList);
    }

    public void loadUsers() {
        final ScrollBar scrollBar = getVerticalScrollbar(userTable);
        if (scrollBar != null) {
            scrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
                double scrollValue = newValue.doubleValue();
                if (scrollValue == scrollBar.getMax() && (page + 1) < totalPages) {
                    double targetValue = scrollValue * userList.size();
                    LOGGER.debug("Getting next Page {}", page + 1);
                    loadUserTable(page + 1);
                    scrollBar.setValue(targetValue / userList.size());
                }
            });

            verticalScrollbar.visibleProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (newValue == false) {
                    // Scrollbar is invisible, load next page
                    loadUserTable(page + 1);
                }
            });
        }

        ChangeListener<TableColumn.SortType> tableColumnSortChangeListener = (observable, oldValue, newValue) -> {
            if(newValue != null) {
                var property = (ObjectProperty<TableColumn.SortType>) observable;
                sortedColumn = (TableColumn) property.getBean();
                for (TableColumn tableColumn : userTable.getColumns()) {
                    if (tableColumn != sortedColumn) {
                        tableColumn.setSortType(null);
                    }
                }

                clearUserList();
                loadUserTable(FIRST_USER_TABLE_PAGE);
            }
        };

        for(TableColumn tableColumn : userTable.getColumns()) {
            tableColumn.setSortType(null);
        }
        usernameCol.sortTypeProperty().addListener(tableColumnSortChangeListener);
        useraccountStatusCol.sortTypeProperty().addListener(tableColumnSortChangeListener);
        userAuthTriesCol.sortTypeProperty().addListener(tableColumnSortChangeListener);

        // Clear user list here, because it could still contain data from the last login
        userList.clear();

        loadUserTable(FIRST_USER_TABLE_PAGE);
    }

    private ScrollBar getVerticalScrollbar(TableView<?> table) {
        if(verticalScrollbar == null) {
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
        LOGGER.debug("Loading users of Page {}", page);
        if (page < 0 || page >= totalPages) {
            LOGGER.warn("Could not load user table page, because page parameter is invalid: " + page);
            return false;
        }

        PageRequestDTO pageRequestDTO = null;

        if (sortedColumn != null) {
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
            for (UserDTO userDTO : response.getContent()) {
                userList.remove(userDTO); // New created entries must be removed first, so they can be re-added at their sorted location in the next line
                userList.add(userDTO);
            }
        } catch (DataAccessException e) {
            if ((e.getCause().getClass()) == HttpClientErrorException.class) {
                var httpErrorCode = ((HttpStatusCodeException) e.getCause()).getStatusCode();
                if (httpErrorCode == HttpStatus.FORBIDDEN) {
                    LOGGER.debug("The current user does not have the authorization to load the users-list");
                    mainController.getTpContent().getTabs().get(4).setDisable(true);
                } else {
                    JavaFXUtils.createErrorDialog(e.getMessage(),
                        content.getScene().getWindow()).showAndWait();
                }
            } else {
                JavaFXUtils.createErrorDialog(e.getMessage(),
                    content.getScene().getWindow()).showAndWait();
            }
        }

        return true;
    }

    public void addUser(UserDTO userDTO) {
        if (userDTO == null) {
            LOGGER.warn("Could not add user to table, because object reference is null!");
            return;
        }
        userList.add(userDTO);
        userTable.sort();
        // if the user is on the last entry, that means that he doesn't belong in this current page
        if (userList.get(userList.size() - 1) == userDTO) {
            // remove user, because he should be loaded later on with an other page
            userList.remove(userDTO);
        } else {
            // user is part of first table
            // remove last item, so it doesn't appear twice when loading next page
            userList.remove(userList.size() - 1);
        }
    }

    public void refreshAndSortUserTable() {
        userTable.refresh();
        userTable.sort();
    }

    public void clearUserList() {
        LOGGER.debug("Clear Users");
        userList.clear();

        ScrollBar scrollBar = getVerticalScrollbar(userTable);
        if (scrollBar != null) {
            scrollBar.setValue(0);
        }
    }

    public void toggleEnable(javafx.event.ActionEvent actionEvent) {
        LOGGER.info("User clicked toggle user button");
        try {
            UserDTO userDTO = userTable.getSelectionModel().getSelectedItem();
            UserValidator.validateExistingUser(userDTO);
            if (userDTO.isEnabled()) {
                LOGGER.debug("Trying to disable user");
                userService.disableUser(userDTO);
                userDTO.setEnabled(false);
                toggleEnableButton.textProperty().bind(BundleManager.getStringBinding("usertab.user.enable"));
            } else {
                LOGGER.debug("Trying to enable user");
                userService.enableUser(userDTO);
                userDTO.setEnabled(true);
                userDTO.setStrikes(0);
                toggleEnableButton.textProperty().bind(BundleManager.getStringBinding("usertab.user.disable"));
            }
        } catch (DataAccessException e) {
            String errorMessage = e.getMessage();
            if (e.getCause() != null && e.getCause().getClass() == HttpClientErrorException.class) {
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

        refreshAndSortUserTable();
    }

    public void onClickCreateUserButton(ActionEvent actionEvent) {
        LOGGER.info("User clicked create user button");

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
        LOGGER.info("User clicked reset user password button");
        UserDTO userDTO = userTable.getSelectionModel().getSelectedItem();

        try {
            UserValidator.validateExistingUser(userDTO);
        } catch (UserValidatorException e) {
            LOGGER.warn("User not valid / No User was selected");
            JavaFXUtils.createErrorDialog(BundleManager.getExceptionBundle().getString("exception.no_selected_user"),
                content.getScene().getWindow()).showAndWait();
            return;
        }

        UserPasswordResetRequestDTO userPasswordResetRequestDTO =
            UserPasswordResetRequestDTO.builder().
                userDTO(userDTO).
                build();

        try {
            UserDTO updatedUserDTO = userService.resetPassword(userPasswordResetRequestDTO);

            if (updatedUserDTO != null) {
                userDTO.update(updatedUserDTO);
                refreshAndSortUserTable();
            }

            LOGGER.debug("Password reset was successful");

            JavaFXUtils.createCopyTextDialog(
                BundleManager.getBundle().getString("usertab.password_reset.dialog.success.title"),
                BundleManager.getBundle().getString("usertab.password_reset.dialog.success.header_text") + " " + userDTO.getUsername(),
                BundleManager.getBundle().getString("usertab.password_reset.dialog.success.content_text"),
                userPasswordResetRequestDTO.getPasswordChangeKey(),
                passwordResetButton.getScene().getWindow()
            ).showAndWait();
        } catch (DataAccessException e) {
            LOGGER.error("Password reset was unsuccessful");
            JavaFXUtils.createErrorDialog(e.getMessage(),
                content.getScene().getWindow()).showAndWait();
        }
    }
}
