package at.ac.tuwien.inso.sepm.ticketline.client.gui.customers;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

import static org.controlsfx.glyphfont.FontAwesome.Glyph.USERS;

@Component
public class CustomerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final int CUSTOMERS_PER_PAGE = 10;

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
    public Pagination customerTablePagination;

    private CustomerService customerService;

    private ObservableList<CustomerDTO> customerList = FXCollections.emptyObservableList();

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(USERS);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("customers.main.title"));

        initializeCustomerTable();
    }

    private void initializeCustomerTable() {
        customerTableColumnFirstName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        customerTableColumnLastName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        customerTableColumnTelephoneNumber.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelephoneNumber()));
        customerTableColumnEMail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));

        customerTable.setItems(customerList);
        customerTable.sortPolicyProperty().set(t -> {
            loadCustomerTable(0);
            return true;
        });

        customerTablePagination.setPageFactory(page -> {
            loadCustomerTable(page);
            return customerTable;
        });

        loadCustomerTable(0);
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

    private void loadCustomerTable(int page) {
        Pageable pageable = null;
        if (customerTable.getSortOrder().size() > 0) {
            TableColumn<CustomerDTO, ?> sortedColumn = customerTable.getSortOrder().get(0);
            Sort.Direction sortDirection = (sortedColumn.getSortType() == TableColumn.SortType.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC;
            pageable = PageRequest.of(page, CUSTOMERS_PER_PAGE, Sort.by(sortDirection, getColumnNameBy(sortedColumn)));
        }
        else {
            pageable = PageRequest.of(page, CUSTOMERS_PER_PAGE);
        }

        try {
            Page<CustomerDTO> customers = customerService.findAll(pageable);
            customerTablePagination.setPageCount(customers.getTotalPages());
            customerTablePagination.setCurrentPageIndex(page);
            customerTable.setItems(customerList = FXCollections.observableList(customers.getContent()));
        } catch (DataAccessException e) {
            LOGGER.warn("Could not access customers!");
            customerTablePagination.setPageCount(1);
            customerTablePagination.setCurrentPageIndex(0);
            customerTable.setItems(customerList = FXCollections.emptyObservableList());
        }
    }
}
