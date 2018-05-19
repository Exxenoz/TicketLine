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
import at.ac.tuwien.inso.sepm.ticketline.rest.validator.UserValidator;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import java.lang.invoke.MethodHandles;

import static org.controlsfx.glyphfont.FontAwesome.Glyph.LOCK;

@Component
public class UsersController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int HEADER_ICON_SIZE = 25;
    private static final int USERS_PER_PAGE = 25;
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

    private ObservableList<UserDTO> items = FXCollections.observableArrayList();
    private int page = 0;
    private int totalPages = 0;

    public UsersController(MainController mainController, UserService userService) {
        this.mainController = mainController;
        this.userService = userService;

    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(LOCK);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("usertab.header"));

        initTable();
    }

    public void loadUsers() {
        // Setting event handler for sort policy property calls it automatically
        userTable.sortPolicyProperty().set(t -> {
            clear();
            loadUsersTable(0);
            return true;
        });

        final ScrollBar scrollBar = getVerticalScrollbar(userTable);
        if (scrollBar != null) {
            scrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
                double value = newValue.doubleValue();
                if ((value == scrollBar.getMax()) && (page < totalPages)) {
                    page++;
                    LOGGER.debug("Getting next Page: {}", page);
                    double targetValue = value * items.size();
                    loadUsersTable(page);
                    scrollBar.setValue(targetValue / items.size());
                }
            });
        }
    }

    public void loadUsersTable(int page) {
        LOGGER.debug("Loading Users of page {}", page);
        PageRequestDTO pageRequestDTO = null;
        if (userTable.getSortOrder().size() > 0) {
            TableColumn<UserDTO, ?> sortedColumn = userTable.getSortOrder().get(0);
            Sort.Direction sortDirection = (sortedColumn.getSortType() == TableColumn.SortType.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC;
            pageRequestDTO = new PageRequestDTO(page, USERS_PER_PAGE, sortDirection, getColumnNameBy(sortedColumn));
        } else {
            pageRequestDTO = new PageRequestDTO(page, USERS_PER_PAGE, Sort.Direction.ASC, null);
        }

        try {
            PageResponseDTO<UserDTO> response = userService.findAll(pageRequestDTO);
            items.addAll(response.getContent());
            totalPages = response.getTotalPages();
        } catch (DataAccessException e) {
            if ((e.getCause().getClass()) == HttpClientErrorException.class) {
                var httpErrorCode = ((HttpStatusCodeException) e.getCause()).getStatusCode();
                if (httpErrorCode == HttpStatus.FORBIDDEN) {
                    LOGGER.warn("The current user doesnt have the authorization to load the users-list");
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

        userTable.refresh();
    }

    private String getColumnNameBy(TableColumn<UserDTO, ?> sortedColumn) {
        if (sortedColumn == usernameCol) {
            return "username";
        } else if (sortedColumn == useraccountStatusCol) {
            return "enabled";
        } else if (sortedColumn == userAuthTriesCol) {
            return "strikes";
        }
        return "id";
    }

    private void initTable() {
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
        userTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

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
        items.clear();
        page = 0;
    }


    public void handleEnable(javafx.event.ActionEvent actionEvent) {
        try {
            UserDTO userDTO = userTable.getSelectionModel().getSelectedItem();
            UserValidator.validateExistingUser(userDTO);
            if (!userDTO.isEnabled()) {
                userService.enableUser(userDTO);
            } else {
                JavaFXUtils.createErrorDialog(BundleManager.getExceptionBundle().getString("exception.selected_user_is_enabled"),
                    content.getScene().getWindow()).showAndWait();
            }
        } catch (DataAccessException e) {
            JavaFXUtils.createErrorDialog(e.getMessage(),
                content.getScene().getWindow()).showAndWait();
        } catch (UserValidatorException e) {
            LOGGER.error("No User was selected");
            JavaFXUtils.createErrorDialog(BundleManager.getExceptionBundle().getString("exception.no_selected_user"),
                content.getScene().getWindow()).showAndWait();
        }

        this.clear();
        loadUsersTable(0);
        userTable.setItems(items);
    }
}
