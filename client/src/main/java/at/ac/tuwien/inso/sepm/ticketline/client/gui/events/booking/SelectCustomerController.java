package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.booking;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.customers.CustomerEditDialogController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventTypeDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.application.Platform;
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

    private static final int CUSTOMERS_PER_PAGE = 30;
    private static final String anonymousUser = "anonymous";

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

    @FXML
    public Button btnBack;

    @FXML
    public Button btnWithCustomer;

    @FXML
    public Button btnWithoutCustomer;

    @FXML
    public Button btnCreateNewCustomer;

    private CustomerService customerService;

    private ObservableList<CustomerDTO> items = FXCollections.observableArrayList();

    private int currentPage = 0;

    private int totalPages = 1;
    private final SpringFxmlLoader fxmlLoader;
    private Stage stage;
    private boolean isReservation;
    private boolean reservationWithNewCustomer = true;
    private ReservationDTO reservation;
    private CustomerDTO chosenCustomer;
    private final PurchaseReservationSummaryController PRSController;
    private final CustomerEditDialogController customerEditDialogController;

    private TableColumn sortedColumn;
    private CustomerDTO anonymousCustomer;

    public SelectCustomerController(SpringFxmlLoader fxmlLoader,
                                    CustomerService customerService,
                                    PurchaseReservationSummaryController PRSController,
                                    CustomerEditDialogController customerEditDialogController) {
        this.fxmlLoader = fxmlLoader;
        this.customerService = customerService;
        this.PRSController = PRSController;
        this.customerEditDialogController = customerEditDialogController;
    }

    @FXML
    private void initialize() {
        LOGGER.info("Initialize SelectCustomerController");
        ButtonBar.setButtonUniformSize(btnBack, false);
        ButtonBar.setButtonUniformSize(btnWithCustomer, false);
        ButtonBar.setButtonUniformSize(btnWithoutCustomer, false);
        ButtonBar.setButtonUniformSize(btnCreateNewCustomer, false);

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
        Platform.runLater(() -> {
            final ScrollBar scrollBar = getVerticalScrollbar(customerTable);
            if (scrollBar != null) {
                scrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
                    double value = newValue.doubleValue();
                    if ((value >= scrollBar.getMax()) && (currentPage + 1 < totalPages)) {
                        currentPage++;
                        LOGGER.debug("Getting next Page: {}", currentPage);
                        double targetValue = value * items.size();
                        loadCustomersTable(currentPage);
                        scrollBar.setValue(targetValue / items.size());
                    }
                });

                scrollBar.visibleProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (newValue == false) {
                        // Scrollbar is invisible, load next page
                        currentPage++;
                        loadCustomersTable(currentPage);
                    }
                });
            }
        });

        ChangeListener<TableColumn.SortType> tableColumnSortChangeListener = (observable, oldValue, newValue) -> {
            if (newValue != null) {
                var property = (ObjectProperty<TableColumn.SortType>) observable;
                sortedColumn = (TableColumn) property.getBean();
                for (TableColumn tableColumn : customerTable.getColumns()) {
                    if (tableColumn != sortedColumn) {
                        tableColumn.setSortType(null);
                    }
                }

                clear();
                loadCustomersTable(0);
            }
        };

        for (TableColumn tableColumn : customerTable.getColumns()) {
            tableColumn.setSortType(null);
        }
        customerTableColumnFirstName.sortTypeProperty().addListener(tableColumnSortChangeListener);
        customerTableColumnLastName.sortTypeProperty().addListener(tableColumnSortChangeListener);
        customerTableColumnTelephoneNumber.sortTypeProperty().addListener(tableColumnSortChangeListener);
        customerTableColumnEMail.sortTypeProperty().addListener(tableColumnSortChangeListener);

        loadCustomersTable(0);
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

    private void loadCustomersTable(int page) {
        if (page < 0 || page >= totalPages) {
            LOGGER.warn("Could not load customer table page, because page parameter is invalid!");
            return;
        }
        LOGGER.debug("Loading Customers of Page {}", page);
        PageRequestDTO pageRequestDTO = null;
        if (sortedColumn != null) {
            Sort.Direction sortDirection =
                (sortedColumn.getSortType() == TableColumn.SortType.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC;
            pageRequestDTO = new PageRequestDTO(page, CUSTOMERS_PER_PAGE, sortDirection, getColumnNameBy(sortedColumn));
        } else {
            pageRequestDTO = new PageRequestDTO(page, CUSTOMERS_PER_PAGE, Sort.Direction.ASC, null);
        }

        try {
            PageResponseDTO<CustomerDTO> response = customerService.findAll(pageRequestDTO);
            items.addAll(response.getContent());
            anonymousCustomer = items.get(0);
            items.removeIf(customer -> customer.getLastName().equals(anonymousUser) && customer.getFirstName().equals(anonymousUser));
            totalPages = response.getTotalPages();
            customerTable.refresh();
        } catch (DataAccessException e) {
            LOGGER.warn("Could not access customers!");
        }
    }

    private void clear() {
        LOGGER.debug("clearing the data");
        items.clear();
        currentPage = 0;
        totalPages = 1;
    }

    public void goBack(ActionEvent actionEvent) {
        LOGGER.info("User clicked the back button");
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
        LOGGER.info("User clicked the go next with selected customer button");
        if (chosenCustomer.getId() == null) {
            LOGGER.warn("No selected Customer");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(BundleManager.getExceptionBundle().getString("exception.select_customer.no_selection"));
            alert.showAndWait();
            return;
        }
        reservation.setCustomer(chosenCustomer);
        continueOrReserve();
    }

    public void goNextWithoutCustomer(ActionEvent actionEvent) {
        LOGGER.info("User clicked the go next without customer button");
        reservation.setCustomer(anonymousCustomer);
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
        LOGGER.info("User clicked the go next with new customer button");
        customerEditDialogController.fill(reservationWithNewCustomer, reservation, isReservation, stage);
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
