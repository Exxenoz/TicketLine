package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.book;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class SelectCustomerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final int CUSTOMERS_PER_PAGE = 20;

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
    public AnchorPane content;

    private CustomerService customerService;

    private ObservableList<CustomerDTO> items = FXCollections.observableArrayList();

    private int currentPage = 0;

    private int totalPages = 0;

    public SelectCustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @FXML
    private void initialize() {
        initTable();
    }

    private void initTable() {
        customerTableColumnFirstName.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getFirstName()
        ));
        customerTableColumnLastName.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getLastName()
        ));
        customerTableColumnTelephoneNumber.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getTelephoneNumber()
        ));
        customerTableColumnEMail.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getEmail()
        ));

        customerTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        LOGGER.debug("loading the page into the table");
        customerTable.setItems(items);
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

    public void loadCustomers() {
        // Setting event handler for sort policy property calls it automatically
        customerTable.sortPolicyProperty().set(t -> {
            clear();
            loadCustomersTable(0);
            return true;
        });

        final ScrollBar scrollBar = getVerticalScrollbar(customerTable);
        if (scrollBar != null) {
            scrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
                double value = newValue.doubleValue();
                if ((value == scrollBar.getMax()) && (currentPage < totalPages)) {
                    currentPage++;
                    LOGGER.debug("Getting next Page: {}", currentPage);
                    double targetValue = value * items.size();
                    loadCustomersTable(currentPage);
                    scrollBar.setValue(targetValue / items.size());
                }
            });
        }
    }

    private String getColumnNameBy(TableColumn<CustomerDTO, ?> tableColumn) {
        if (tableColumn == customerTableColumnFirstName) {
            return "firstName";
        } else if (tableColumn == customerTableColumnLastName) {
            return "lastName";
        } else if (tableColumn == customerTableColumnTelephoneNumber) {
            return "telephoneNumber";
        } else if (tableColumn == customerTableColumnEMail) {
            return "email";
        }

        return "id";
    }

    public void loadCustomersTable(int page) {
        LOGGER.debug("Loading Customers of page {}", page);
        PageRequestDTO pageRequestDTO = null;
        if (customerTable.getSortOrder().size() > 0) {
            TableColumn<CustomerDTO, ?> sortedColumn = customerTable.getSortOrder().get(0);
            Sort.Direction sortDirection =
                (sortedColumn.getSortType() == TableColumn.SortType.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC;
            pageRequestDTO = new PageRequestDTO(page, CUSTOMERS_PER_PAGE, sortDirection, getColumnNameBy(sortedColumn));
        } else {
            pageRequestDTO = new PageRequestDTO(page, CUSTOMERS_PER_PAGE, Sort.Direction.ASC, null);
        }

        try {
            PageResponseDTO<CustomerDTO> response = customerService.findAll(pageRequestDTO);
            items.addAll(response.getContent());
            totalPages = response.getTotalPages();
        } catch (DataAccessException e) {
            LOGGER.warn("Could not access customers!");
        }

        customerTable.setItems(items);
    }

    private void clear() {
        LOGGER.debug("clearing the data");
        items.clear();
        currentPage = 0;
    }

    public void goBack(ActionEvent actionEvent) {
        //go back to previous dialog scene
        /*
        final var wrapper=fxmlLoader.loadAndWrap("/fxml/events/book/selectCustomerView.fxml");
        final var controller = (SelectCustomerController) wrapper.getController();
        final var next=(Parent)wrapper.getLoadedObject();
        controller.loadCustomers();
        stage.setScene(new Scene(next));
        */
    }

    public void goNextWithCustomer(ActionEvent actionEvent) {
        //go to the next dialog scene with a selected Customer
        /*
        final var wrapper=fxmlLoader.loadAndWrap("/fxml/events/book/selectCustomerView.fxml");
        final var controller = (SelectCustomerController) wrapper.getController();
        final var next=(Parent)wrapper.getLoadedObject();
        controller.loadCustomers();
        stage.setScene(new Scene(next));
        */
    }

    public void goNextWithoutCustomer(ActionEvent actionEvent) {
        //go to the next dialog scene without a selected customer
         /*
         final var wrapper=fxmlLoader.loadAndWrap("/fxml/events/book/selectCustomerView.fxml");
        final var controller = (SelectCustomerController) wrapper.getController();
        final var next=(Parent)wrapper.getLoadedObject();
        controller.loadCustomers();
        stage.setScene(new Scene(next));

        */
    }

    public void createNewCustomer(ActionEvent actionEvent) {
        //go to the create customer dialog scene
    }
}
