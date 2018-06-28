package at.ac.tuwien.inso.sepm.ticketline.client.gui.customers;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

import static javafx.stage.Modality.APPLICATION_MODAL;
import static org.controlsfx.glyphfont.FontAwesome.Glyph.USERS;

@Component
public class CustomerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final int CUSTOMERS_PER_PAGE = 50;
    public static final int FIRST_CUSTOMER_TABLE_PAGE = 0;
    private static final String anonymousUser = "anonymous";

    @FXML
    private TabHeaderController tabHeaderController;

    @FXML
    public TableView<CustomerDTO> customerTable;

    @FXML
    public TableColumn<CustomerDTO, String> customerTableColumnFirstName;

    @FXML
    public TableColumn<CustomerDTO, String> customerTableColumnLastName;

    @FXML
    public TableColumn<CustomerDTO, String> customerTableColumnTelephoneNumber;

    @FXML
    public TableColumn<CustomerDTO, String> customerTableColumnEMail;

    @FXML
    public Button customerEditButton;

    @FXML
    public Button customerCreateButton;

    private final SpringFxmlLoader springFxmlLoader;

    private CustomerService customerService;

    private ObservableList<CustomerDTO> customerList = FXCollections.observableArrayList();

    private int customerTablePage = 0;
    private int customerTablePageCount = 1;

    private TableColumn sortedColumn;

    public CustomerController(SpringFxmlLoader springFxmlLoader, CustomerService customerService) {
        this.springFxmlLoader = springFxmlLoader;
        this.customerService = customerService;
    }

    @FXML
    private void initialize() {
        LOGGER.info("Initialize CustomerController");
        tabHeaderController.setIcon(USERS);
        tabHeaderController.setTitleBinding(BundleManager.getStringBinding("customers.main.title"));

        ButtonBar.setButtonUniformSize(customerEditButton, false);
        ButtonBar.setButtonUniformSize(customerCreateButton, false);

        initI18N();
        initializeCustomerTable();
    }

    private void initI18N() {
        customerTableColumnFirstName.textProperty().bind(BundleManager.getStringBinding("customers.main.table.column.first_name"));
        customerTableColumnLastName.textProperty().bind(BundleManager.getStringBinding("customers.main.table.column.last_name"));
        customerTableColumnTelephoneNumber.textProperty().bind(BundleManager.getStringBinding("customers.main.table.column.telephone_number"));
        customerTableColumnEMail.textProperty().bind(BundleManager.getStringBinding("customers.main.table.column.email"));
        customerEditButton.textProperty().bind(BundleManager.getStringBinding("customers.main.button.edit"));
        customerCreateButton.textProperty().bind(BundleManager.getStringBinding("customers.main.button.create"));
    }

    private void initializeCustomerTable() {
        customerTableColumnFirstName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        customerTableColumnLastName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        customerTableColumnTelephoneNumber.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelephoneNumber()));
        customerTableColumnEMail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));

        customerTable.setItems(customerList);

        customerTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            customerEditButton.setDisable(newValue == null);
        });
    }

    public void loadCustomers() {
        final ScrollBar scrollBar = getVerticalScrollbar(customerTable);
        if (scrollBar != null) {
            scrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
                double scrollValue = newValue.doubleValue();
                if (scrollValue == scrollBar.getMax() && (customerTablePage + 1) < customerTablePageCount) {
                    double targetValue = scrollValue * customerList.size();
                    customerTablePage += 1;
                    LOGGER.debug("Getting next customer Page {}", customerTablePage);
                    loadCustomerTable(customerTablePage);
                    scrollBar.setValue(targetValue / customerList.size());
                }
            });

            scrollBar.visibleProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (newValue == false) {
                    // Scrollbar is invisible, load pages until scrollbar is shown again
                    loadCustomerTable(customerTablePage + 1);
                }
            });
        }

        ChangeListener<TableColumn.SortType> tableColumnSortChangeListener = (observable, oldValue, newValue) -> {
            if(newValue != null) {
                var property = (ObjectProperty<TableColumn.SortType>) observable;
                sortedColumn = (TableColumn) property.getBean();
                for (TableColumn tableColumn : customerTable.getColumns()) {
                    if (tableColumn != sortedColumn) {
                        tableColumn.setSortType(null);
                    }
                }
                clearCustomerList();
                loadCustomerTable(FIRST_CUSTOMER_TABLE_PAGE);
            }
        };

        for(TableColumn tableColumn : customerTable.getColumns()) {
            tableColumn.setSortType(null);
        }
        customerTableColumnFirstName.sortTypeProperty().addListener(tableColumnSortChangeListener);
        customerTableColumnLastName.sortTypeProperty().addListener(tableColumnSortChangeListener);
        customerTableColumnEMail.sortTypeProperty().addListener(tableColumnSortChangeListener);
        customerTableColumnTelephoneNumber.sortTypeProperty().addListener(tableColumnSortChangeListener);

        // Clear customer list here, because it could still contain data from the last login
        customerList.clear();

        loadCustomerTable(FIRST_CUSTOMER_TABLE_PAGE);
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

    private String getColumnNameBy(TableColumn<CustomerDTO, ?> tableColumn) {
        if (tableColumn == customerTableColumnFirstName) {
            return "firstName";
        }
        else if (tableColumn == customerTableColumnLastName) {
            return "lastName";
        }
        else if (tableColumn == customerTableColumnTelephoneNumber) {
            return "telephoneNumber";
        }
        else if (tableColumn == customerTableColumnEMail) {
            return "email";
        }

        return "id";
    }

    public void loadCustomerTable(int page) {
        LOGGER.debug("Loading Customers of page {}", page);
        if (page < 0 || page >= customerTablePageCount) {
            LOGGER.warn("Could not load customer table page, because page parameter is invalid!");
            return;
        }
        PageRequestDTO pageRequestDTO = null;

        if (sortedColumn != null) {
            Sort.Direction sortDirection = (sortedColumn.getSortType() == TableColumn.SortType.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC;
            pageRequestDTO = new PageRequestDTO(page, CUSTOMERS_PER_PAGE, sortDirection, getColumnNameBy(sortedColumn));
        }
        else {
            pageRequestDTO = new PageRequestDTO(page, CUSTOMERS_PER_PAGE, Sort.Direction.ASC, null);
        }

        customerTablePage = page;

        try {
            PageResponseDTO<CustomerDTO> response = customerService.findAll(pageRequestDTO);
            customerTablePageCount = response.getTotalPages() > 0 ? response.getTotalPages() : 1;
            for (CustomerDTO customerDTO : response.getContent()) {
                customerList.remove(customerDTO); // New created entries must be removed first, so they can be re-added at their sorted location in the next line
                if(!customerDTO.getLastName().equals(anonymousUser) || !customerDTO.getFirstName().equals(anonymousUser)) {
                    customerList.add(customerDTO);
                }
            }
        } catch (DataAccessException e) {
            LOGGER.error("Could not access customers!");
        }
    }

    public void addCustomer(CustomerDTO customerDTO) {
        if (customerDTO == null) {
            LOGGER.warn("Could not add customer to table, because object reference is null!");
            return;
        }
        customerList.add(customerDTO);
        customerTable.sort();
        if (customerList.get(customerList.size() - 1) == customerDTO) {
            customerList.remove(customerDTO);
        } else {
            // remove last item, so it doesn't appear twice when loading next page
            customerList.remove(customerList.size() - 1);
        }
    }

    public void refreshAndSortCustomerTable() {
        customerTable.refresh();
        customerTable.sort();
    }

    public void clearCustomerList() {
        LOGGER.debug("Clear Customers");
        customerList.clear();

        ScrollBar scrollBar = getVerticalScrollbar(customerTable);
        if (scrollBar != null) {
            scrollBar.setValue(0);
        }
    }

    @FXML
    public void onClickCreateCustomerButton(ActionEvent actionEvent) {
        LOGGER.info("User clicked create customer button");

        final var stage = (Stage) customerTable.getScene().getWindow();
        final var dialog = new Stage();

        dialog.getIcons().add(new Image(CustomerController.class.getResourceAsStream("/image/ticketlineIcon.png")));
        dialog.setResizable(false);
        dialog.initModality(APPLICATION_MODAL);
        dialog.initOwner(stage);
        dialog.setScene(new Scene(springFxmlLoader.load("/fxml/customers/customerEditDialog.fxml")));
        dialog.setTitle(BundleManager.getBundle().getString("customers.dialog.create.title"));
        dialog.showAndWait();
    }

    @FXML
    public void onClickEditCustomerButton(ActionEvent actionEvent) {
        LOGGER.info("User clicked edit customer button");

        CustomerDTO selectedCustomerDTO = customerTable.getSelectionModel().getSelectedItem();

        if (selectedCustomerDTO == null) {
            LOGGER.warn("Could not edit customer, because no customer is selected!");
            return;
        }

        var wrap = springFxmlLoader.loadAndWrap("/fxml/customers/customerEditDialog.fxml");
        final CustomerEditDialogController controller = (CustomerEditDialogController) wrap.getController();
        final var stage = (Stage) customerTable.getScene().getWindow();
        final var dialog = new Stage();

        controller.setCustomerToEdit(selectedCustomerDTO);

        dialog.getIcons().add(new Image(CustomerController.class.getResourceAsStream("/image/ticketlineIcon.png")));
        dialog.setResizable(false);
        dialog.initModality(APPLICATION_MODAL);
        dialog.initOwner(stage);
        dialog.setScene(new Scene((Parent)wrap.getLoadedObject()));
        dialog.setTitle(BundleManager.getBundle().getString("customers.dialog.edit.title"));
        dialog.showAndWait();
    }
}
