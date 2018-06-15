package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.booking;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventTypeDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.CreateReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
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
    private final SpringFxmlLoader fxmlLoader;
    private Stage stage;
    private boolean isReservation;
    private ReservationDTO reservation;
    private CustomerDTO chosenCustomer;
    private PurchaseReservationSummaryController PRSController;


    public SelectCustomerController(SpringFxmlLoader fxmlLoader,
                                    CustomerService customerService,
                                    PurchaseReservationSummaryController PRSController) {
        this.fxmlLoader = fxmlLoader;
        this.customerService = customerService;
        this.PRSController = PRSController;
    }

    @FXML
    private void initialize() {
        initTable();
    }

    private void initTable() {
        chosenCustomer = new CustomerDTO();
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

        customerTable.getSelectionModel().selectedItemProperty().addListener((ObservableValue, oldValue, newValue) -> {
            if (newValue != null) {
                chosenCustomer = newValue;
            }
        });
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
        Parent parent = fxmlLoader.load("/fxml/events/book/hallPlanView.fxml");
        stage.setScene(new Scene(parent));
        if (reservation.getPerformance().getEvent().getEventType() == EventTypeDTO.SEAT) {
            stage.setTitle(BundleManager.getBundle().getString("bookings.hallplan.title"));
        } else {
            stage.setTitle(BundleManager.getBundle().getString("bookings.sectorplan.title"));
        }
        stage.centerOnScreen();
    }

    public void goNextWithCustomer(ActionEvent event) {
        if (chosenCustomer.getId() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("You need to choose a specific customer for this option.");
            alert.showAndWait();
            return;
        }
        reservation.setCustomer(chosenCustomer);
        continueOrReserve();
    }

    public void goNextWithoutCustomer(ActionEvent actionEvent) {
        reservation.setCustomer(customerTable.getItems().get(0));
        continueOrReserve();
    }

    private void continueOrReserve(){
        PRSController.fill(reservation, isReservation, stage);
        Parent parent = fxmlLoader.load("/fxml/events/book/purchaseReservationSummary.fxml");
        stage.setScene(new Scene(parent));

        if (isReservation) {
            stage.setTitle(BundleManager.getBundle().getString("bookings.reservation.details.title"));
        } else {
            stage.setTitle(BundleManager.getBundle().getString("bookings.purchase.details.title"));
        }
        stage.centerOnScreen();
    }

    public void createNewCustomer(ActionEvent actionEvent) {
        Parent parent = fxmlLoader.load("/fxml/customers/customerEditDialog.fxml");
        stage.setScene(new Scene(parent));
        stage.centerOnScreen();
    }

    public void fill(ReservationDTO reservation, boolean isReservation, Stage stage) {
        this.reservation = reservation;
        this.isReservation = isReservation;
        this.stage = stage;
    }

}
