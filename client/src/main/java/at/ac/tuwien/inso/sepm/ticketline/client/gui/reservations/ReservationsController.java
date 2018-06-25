package at.ac.tuwien.inso.sepm.ticketline.client.gui.reservations;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.CustomerValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.InvoiceFileException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.ReservationSearchValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.booking.PurchaseReservationSummaryController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.InvoiceService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.ReservationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationSearchDTO;
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
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.ResourceBundle;

import static at.ac.tuwien.inso.sepm.ticketline.client.validator.CustomerValidator.validateFirstName;
import static at.ac.tuwien.inso.sepm.ticketline.client.validator.CustomerValidator.validateLastName;
import static at.ac.tuwien.inso.sepm.ticketline.client.validator.ReservationSearchValidator.validatePerformanceName;
import static at.ac.tuwien.inso.sepm.ticketline.client.validator.ReservationSearchValidator.validateReservationNumber;
import static javafx.scene.control.ButtonType.OK;
import static org.controlsfx.glyphfont.FontAwesome.Glyph.TICKET;


@Component
public class ReservationsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public VBox content;
    public TabHeaderController tabHeaderController;
    public Label activeFiltersListLabel;
    public Label reservationNumberErrorLabel;
    public Label customerLastNameErrorLabel;
    public Label customerFirstNameErrorLabel;
    public Label performanceNameErrorLabel;
    public TableColumn<ReservationDTO, String> reservationIDColumn;
    public TableColumn<ReservationDTO, String> eventColumn;
    public TableColumn<ReservationDTO, String> customerColumn;
    public TableColumn<ReservationDTO, String> paidColumn;
    public TableView<ReservationDTO> foundReservationsTableView;
    public TextField performanceNameField;
    public TextField customerFirstNameField;
    public TextField customerLastNameField;
    public TextField reservationNrField;

    @FXML
    public Label activeFiltersLabel;

    @FXML
    public Button clearButton;

    @FXML
    public Button showReservationDetailsButton;

    @FXML
    public Button cancelReservationButton;

    @FXML
    public Button searchButton;

    @FXML
    public TitledPane eventPerformanceTitledPane;

    @FXML
    public Label performanceNameLabel;

    @FXML
    public Label customerFirstNameLabel;

    @FXML
    public Label customerLastNameLabel;

    @FXML
    public TitledPane reservationNrTitledPane;

    @FXML
    public Label reservationNumberLabel;

    private final SpringFxmlLoader fxmlLoader;
    private final ReservationService reservationService;
    private static final String INVOICES_FOLDER = "invoices/";
    private final PurchaseReservationSummaryController PRSController;
    private ObservableList<ReservationDTO> reservationList;
    private int page = 0;
    private int totalPages = 0;
    private static final int RESERVATIONS_PER_PAGE = 50;
    private static final int RESERVATION_FIRST_PAGE = 0;
    private final InvoiceService invoiceService;

    private String activeFilters = "";
    private String performanceName = null;
    private String customerFirstName = null;
    private String customerLastName = null;
    private boolean filtered = false;

    private TableColumn sortedColumn;

    public ReservationsController(SpringFxmlLoader fxmlLoader,
                                  ReservationService reservationService,
                                  InvoiceService invoiceService,
                                  PurchaseReservationSummaryController PRSController) {
        this.fxmlLoader = fxmlLoader;
        this.reservationService = reservationService;
        this.PRSController = PRSController;
        this.reservationList = FXCollections.observableArrayList();
        this.invoiceService = invoiceService;
    }

    public void loadData() {
        loadReservations();
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(TICKET);
        tabHeaderController.setTitleBinding(BundleManager.getStringBinding("bookings.tab.header"));

        ButtonBar.setButtonUniformSize(showReservationDetailsButton, false);
        ButtonBar.setButtonUniformSize(cancelReservationButton, false);

        initI18N();
        initializeTableView();
    }

    private void initI18N() {
        reservationIDColumn.textProperty().bind(BundleManager.getStringBinding("bookings.table.reservationID"));
        eventColumn.textProperty().bind(BundleManager.getStringBinding("bookings.table.event"));
        customerColumn.textProperty().bind(BundleManager.getStringBinding("bookings.table.customer"));
        paidColumn.textProperty().bind(BundleManager.getStringBinding("bookings.table.paid"));

        activeFiltersLabel.textProperty().bind(BundleManager.getStringBinding("bookings.main.activefilters"));

        eventPerformanceTitledPane.textProperty().bind(BundleManager.getStringBinding("bookings.main.search.eventperformance"));
        performanceNameLabel.textProperty().bind(BundleManager.getStringBinding("bookings.search.performancename"));
        customerFirstNameLabel.textProperty().bind(BundleManager.getStringBinding("bookings.search.customerfirstname"));
        customerLastNameLabel.textProperty().bind(BundleManager.getStringBinding("bookings.search.customerlastname"));

        reservationNrTitledPane.textProperty().bind(BundleManager.getStringBinding("bookings.main.search.reservationnr"));
        reservationNumberLabel.textProperty().bind(BundleManager.getStringBinding("bookings.main.search.reservationnr"));

        clearButton.textProperty().bind(BundleManager.getStringBinding("bookings.main.button.clear"));
        showReservationDetailsButton.textProperty().bind(BundleManager.getStringBinding("bookings.main.button.details"));
        cancelReservationButton.textProperty().bind(BundleManager.getStringBinding("bookings.main.cancelReservationsButton"));
        searchButton.textProperty().bind(BundleManager.getStringBinding("bookings.main.button.search"));
    }

    private void printInvoiceDialog(ReservationDTO reservationDTO) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(BundleManager.getBundle().getString("bookings.cancel.print.title"));
        alert.setHeaderText(BundleManager.getBundle().getString("bookings.cancel.print.text"));
        ButtonType buttonTypeYes = new ButtonType(
            BundleManager.getBundle().getString("bookings.cancel.print.yes")
        );
        ButtonType buttonTypeCancel = new ButtonType(
            BundleManager.getBundle().getString("bookings.cancel.print.no"),
            ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeYes) {
            LOGGER.debug("print invoice");
            File pdf = openPDFFile(reservationDTO);
//            invoiceService.deletePDF(pdf);
        } else {
            LOGGER.debug("do not print invoice");
        }
    }

    private File openPDFFile(ReservationDTO reservationDTO) {
        String filepath = INVOICES_FOLDER
            + reservationDTO.getReservationNumber()
            + "_cancelled"
            + ".pdf";
        File invoiceFile = new File(filepath);
        try {
            LOGGER.debug("getting the file and storing it in {}...", filepath);
            invoiceService.downloadAndStorePDF(reservationDTO.getReservationNumber());
            invoiceService.openPDF(reservationDTO.getReservationNumber());
        } catch (DataAccessException d) {
            LOGGER.error("An error occurred whilst handling the file: {}", d.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, BundleManager.getExceptionBundle().getString("exception.invoice.error"), OK);
            alert.showAndWait();
        } catch (InvoiceFileException i) {
            LOGGER.error("An error occured while trying to store the file: {}", i.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, BundleManager.getExceptionBundle().getString("exception.invoice.file"), OK);
            alert.showAndWait();
        }
        return invoiceFile;
    }

    public void cancelReservation(ActionEvent event) {
        ResourceBundle ex = BundleManager.getExceptionBundle();
        int row = foundReservationsTableView.getSelectionModel().getFocusedIndex();
        ReservationDTO selected = reservationList.get(row);
        if (!selected.isPaid()) {
            try {
                selected = reservationService.cancelReservation(selected.getId());
                foundReservationsTableView.getItems().get(row).setCanceled(true);
                foundReservationsTableView.refresh();
                //   reservationList.remove(reservationDTO);
                //   foundReservationsTableView.getItems().remove(row);
                printInvoiceDialog(selected);
            } catch (DataAccessException e) {
                LOGGER.error("The reservation with id {} couldn't be canceled", selected.getId(), e);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getString("exception.reservation.cancel.alreadypaid"), OK);
            alert.showAndWait();
        }

    }

    public void loadReservations() {
        final ScrollBar scrollBar = getVerticalScrollbar(foundReservationsTableView);
        if (scrollBar != null) {
            scrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
                double value = newValue.doubleValue();
                if ((value == scrollBar.getMax()) && (!(page >= (totalPages - 1)))) {
                    page++;
                    LOGGER.debug("Getting next Page: {}", page);
                    double targetValue = value * reservationList.size();
                    loadReservationTable(page);
                    scrollBar.setValue(targetValue / reservationList.size());
                }
            });

            scrollBar.visibleProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (newValue == false) {
                    // Scrollbar is invisible, load next page
                    page++;
                    loadReservationTable(page);
                }
            });
        }

        ChangeListener<TableColumn.SortType> tableColumnSortChangeListener = (observable, oldValue, newValue) -> {
            if (newValue != null) {
                var property = (ObjectProperty<TableColumn.SortType>) observable;
                sortedColumn = (TableColumn) property.getBean();
                for (TableColumn tableColumn : foundReservationsTableView.getColumns()) {
                    if (tableColumn != sortedColumn) {
                        tableColumn.setSortType(null);
                    }
                }

                clear();
                loadReservationTable(RESERVATION_FIRST_PAGE);
            }
        };

        for (TableColumn tableColumn : foundReservationsTableView.getColumns()) {
            tableColumn.setSortType(null);
        }
        eventColumn.sortTypeProperty().addListener(tableColumnSortChangeListener);
        customerColumn.sortTypeProperty().addListener(tableColumnSortChangeListener);
        paidColumn.sortTypeProperty().addListener(tableColumnSortChangeListener);
        reservationIDColumn.sortTypeProperty().addListener(tableColumnSortChangeListener);

        loadReservationTable(RESERVATION_FIRST_PAGE);
    }

    private void loadReservationTable(int page) {
        if (filtered) {
            loadFilteredReservationsTable(page);
        } else {
            loadUnfilteredReservationsTable(page);
        }
        foundReservationsTableView.refresh();
    }

    private void loadUnfilteredReservationsTable(int page) {
        PageRequestDTO pageRequestDTO;

        if (sortedColumn != null) {
            Sort.Direction sortDirection = (sortedColumn.getSortType() == TableColumn.SortType.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC;
            pageRequestDTO = new PageRequestDTO(page, RESERVATIONS_PER_PAGE, sortDirection, getColumnNameBy(sortedColumn));
        } else {
            pageRequestDTO = new PageRequestDTO(page, RESERVATIONS_PER_PAGE, Sort.Direction.ASC, null);
        }

        try {
            PageResponseDTO<ReservationDTO> response = reservationService.findAll(pageRequestDTO);
            reservationList.addAll(response.getContent());
            totalPages = response.getTotalPages();
        } catch (DataAccessException e) {
            LOGGER.error("Couldn't fetch reservations from server!");

        }
    }

    private void loadFilteredReservationsTable(int page) {
        if (performanceName == null || customerFirstName == null || customerLastName == null) {
            return;
        }

        ReservationSearchDTO.Builder reservationSearchBuilder = ReservationSearchDTO.Builder.aReservationSearchDTO()
            .withPage(page)
            .withSize(RESERVATIONS_PER_PAGE)
            .withSortDirection(Sort.Direction.ASC)
            .withPerformanceName(performanceName)
            .withFirstName(customerFirstName)
            .withLastName(customerLastName);

        if (foundReservationsTableView.getSortOrder().size() > 0) {
            TableColumn<ReservationDTO, ?> sortedColumn = foundReservationsTableView.getSortOrder().get(0);
            Sort.Direction sortDirection = (sortedColumn.getSortType() == TableColumn.SortType.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC;
            reservationSearchBuilder.withSortColumnName(getColumnNameBy(sortedColumn));
            reservationSearchBuilder.withSortDirection(sortDirection);
        }

        try {
            PageResponseDTO<ReservationDTO> response =
                reservationService.findAllByCustomerNameAndPerformanceName(reservationSearchBuilder.build());
            reservationList.addAll(response.getContent());
            this.page = page;
            totalPages = response.getTotalPages();
        } catch (DataAccessException e) {
            LOGGER.error("Couldn't fetch reservations from server!");
            //JavaFXUtils.createErrorDialog(e.getMessage(),
            //content.getScene().getWindow()).showAndWait();
        }
    }

    private String getColumnNameBy(TableColumn<ReservationDTO, ?> sortedColumn) {
        if (sortedColumn == eventColumn) {
            return "performance.event.name";
        } else if (sortedColumn == customerColumn) {
            return "customer.lastName";
        } else if (sortedColumn == paidColumn) {
            return "paid";
        } else if (sortedColumn == reservationIDColumn) {
            return "reservationNumber";
        }
        return "id";
    }

    private void initializeTableView() {
        reservationIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getReservationNumber()));
        eventColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getPerformance().getEvent().getName()));
        customerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getCustomer().getFirstName() + " " +
                cellData.getValue().getCustomer().getLastName()));
        paidColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().isCanceled() == false) {
                if (cellData.getValue().isPaid()) {
                    return BundleManager.getStringBinding("bookings.table.paid.true");
                } else {
                    return BundleManager.getStringBinding("bookings.table.paid.false");
                }
            } else {
                return BundleManager.getStringBinding("bookings.table.canceled.true");
            }
        });

        foundReservationsTableView.setItems(reservationList);
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
        reservationList.clear();
        page = 0;
        totalPages = 0;
        ScrollBar scrollBar = getVerticalScrollbar(foundReservationsTableView);
        if (scrollBar != null) {
            scrollBar.setValue(0);
        }
    }

    public void showReservationDetailsButton() {
        Stage stage = new Stage();
        int row = foundReservationsTableView.getSelectionModel().getFocusedIndex();
        PRSController.showReservationDetails(reservationList.get(row), stage);

        Parent parent = fxmlLoader.load("/fxml/events/book/purchaseReservationSummary.fxml");

        stage.setScene(new Scene(parent));
        stage.setTitle(BundleManager.getBundle().getString("reservation.details.title"));

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(showReservationDetailsButton.getScene().getWindow());

        stage.showAndWait();

        clear();
        loadData();
    }

    public void searchForReservations() {

        clearfilters();

        performanceName = performanceNameField.getText();
        customerFirstName = customerFirstNameField.getText();
        customerLastName = customerLastNameField.getText();
        String reservationNumber = reservationNrField.getText();
        activeFilters = "";

        if ((!performanceName.equals(""))
            && (!customerFirstName.equals(""))
            && (!customerLastName.equals(""))
            && (reservationNumber.equals(""))) {

            try {
                customerFirstName = validateFirstName(customerFirstNameField);
                customerFirstNameErrorLabel.textProperty().unbind();
                customerFirstNameErrorLabel.setText("");
            } catch (CustomerValidationException e) {
                LOGGER.error("Error with customer first name value, ", e);
                customerFirstNameErrorLabel.textProperty().bind(BundleManager.getExceptionStringBinding(e.getExceptionBundleKey()));
            }
            try {
                customerLastName = validateLastName(customerLastNameField);
                customerLastNameErrorLabel.textProperty().unbind();
                customerLastNameErrorLabel.setText("");
            } catch (CustomerValidationException e) {
                LOGGER.error("Error with customer last name value, ", e);
                customerLastNameErrorLabel.textProperty().bind(BundleManager.getExceptionStringBinding(e.getExceptionBundleKey()));
            }

            try {
                performanceName = validatePerformanceName(performanceNameField);
                clear();
                activeFilters += BundleManager.getBundle().getString("bookings.main.activefilters.performancename")
                    + " " + performanceName + ", "
                    + BundleManager.getBundle().getString("bookings.main.activefilters.customername")
                    + " " + customerFirstName
                    + " " + customerLastName;

                loadFilteredReservationsTable(0);
                foundReservationsTableView.refresh();
                filtered = true;
                LOGGER.debug("Found {} page(s) satisfying the given criteria", totalPages);
                performanceNameErrorLabel.textProperty().unbind();
                performanceNameErrorLabel.setText("");
            } catch (ReservationSearchValidationException e) {
                LOGGER.error("Error with performance name value, ", e);
                performanceNameErrorLabel.textProperty().bind(BundleManager.getExceptionStringBinding(e.getExceptionBundleKey()));
            }

        } else if ((performanceName.equals(""))
            && (customerFirstName.equals(""))
            && (customerLastName.equals(""))
            && (!reservationNumber.equals(""))) {

            try {
                reservationNumber = validateReservationNumber(reservationNrField);

                clear();
                activeFilters += BundleManager.getBundle().getString("bookings.main.activefilters.reservationnr") + " "
                    + reservationNumber;
                try {
                    ReservationDTO response =
                        reservationService.findOneByReservationNumber(reservationNumber);
                    reservationList.clear();
                    reservationList.addAll(response);
                } catch (DataAccessException e) {
                    LOGGER.error("Couldn't fetch reservations from server!");
                    JavaFXUtils.createErrorDialog(e.getMessage(),
                        content.getScene().getWindow()).showAndWait();
                }
                foundReservationsTableView.refresh();
                filtered = true;
                reservationNumberErrorLabel.textProperty().unbind();
                reservationNumberErrorLabel.setText("");
            } catch (ReservationSearchValidationException e) {
                LOGGER.error("Error with given ReservationNumber", e);
                reservationNumberErrorLabel.textProperty().bind(BundleManager.getExceptionStringBinding(e.getExceptionBundleKey()));
            }
        } else {
            filtered = false;
            JavaFXUtils.createErrorDialog(
                BundleManager.getExceptionBundle().getString("exception.search.invalid.parameters"),
                content.getScene().getWindow()
            ).showAndWait();
        }
        activeFiltersListLabel.setText(activeFilters);
    }

    public void clearFilters(ActionEvent actionEvent) {
        filtered = false;
        activeFiltersListLabel.setText("");
        performanceNameField.setText("");
        customerFirstNameField.setText("");
        customerLastNameField.setText("");
        reservationNrField.setText("");

        performanceName = "";
        customerFirstName = "";
        customerLastName = "";
        activeFilters = "";

        reservationNumberErrorLabel.textProperty().unbind();
        customerLastNameErrorLabel.textProperty().unbind();
        customerFirstNameErrorLabel.textProperty().unbind();
        performanceNameErrorLabel.textProperty().unbind();

        reservationNumberErrorLabel.setText("");
        customerLastNameErrorLabel.setText("");
        customerFirstNameErrorLabel.setText("");
        performanceNameErrorLabel.setText("");

        clear();
        loadData();
    }

    private void clearfilters(){
        reservationNumberErrorLabel.setText("");
        customerLastNameErrorLabel.setText("");
        customerFirstNameErrorLabel.setText("");
        performanceNameErrorLabel.setText("");
    }
}
