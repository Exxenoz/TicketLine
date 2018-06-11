package at.ac.tuwien.inso.sepm.ticketline.client.gui.reservations;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.booking.PurchaseReservationSummaryController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.ReservationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationSearchDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.beans.property.SimpleStringProperty;
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

import java.lang.invoke.MethodHandles;

import static org.controlsfx.glyphfont.FontAwesome.Glyph.TICKET;


@Component
public class ReservationsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public VBox content;
    public TabHeaderController tabHeaderController;
    public Label activeFiltersListLabel;
    public TableColumn<ReservationDTO, String> reservationIDColumn;
    public TableColumn<ReservationDTO, String> eventColumn;
    public TableColumn<ReservationDTO, String> customerColumn;
    public TableColumn<ReservationDTO, String> paidColumn;
    public TableView<ReservationDTO> foundReservationsTableView;
    public TextField performanceNameField;
    public TextField customerFirstNameField;
    public TextField customerLastNameField;
    public TextField reservationNrField;
    public Button showReservationDetailsButton;

    private final SpringFxmlLoader fxmlLoader;
    private final ReservationService reservationService;
    private final PurchaseReservationSummaryController PRSController;
    private ObservableList<ReservationDTO> reservationDTOS = FXCollections.observableArrayList();
    private int page = 0;
    private int totalPages = 0;
    private static final int RESERVATIONS_PER_PAGE = 50;
    private static final int RESERVATION_NUMBER_LENGTH = 7;
    private static final int RESERVATION_FIRST_PAGE = 0;

    private String activeFilters = "";
    private String performanceName = null;
    private String customerFirstName = null;
    private String customerLastName = null;
    private boolean filtered = false;

    public ReservationsController(SpringFxmlLoader fxmlLoader,
                                  ReservationService reservationService,
                                  PurchaseReservationSummaryController PRSController) {
        this.fxmlLoader = fxmlLoader;
        this.reservationService = reservationService;
        this.PRSController = PRSController;
    }

    public void loadData() {
        loadReservations();
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(TICKET);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("bookings.tab.header"));
        initializeTableView();
    }

    public void loadReservations() {
        foundReservationsTableView.sortPolicyProperty().set(t -> {
            clear();
            loadPerformanceTable(RESERVATION_FIRST_PAGE);
            return true;
        });

        final ScrollBar scrollBar = getVerticalScrollbar(foundReservationsTableView);
        if (scrollBar != null) {
            scrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
                double value = newValue.doubleValue();
                if ((value == scrollBar.getMax()) && (!(page >= totalPages))) {
                    page++;
                    LOGGER.debug("Getting next Page: {}", page);
                    double targetValue = value * reservationDTOS.size();
                    loadPerformanceTable(page);
                    scrollBar.setValue(targetValue / reservationDTOS.size());
                }
            });
        }
    }

    private void loadPerformanceTable(int page) {
        if (filtered) {
            loadFilteredPerformanceTable(page);
        } else {
            loadUnfilteredPerformanceTable(page);
        }
        foundReservationsTableView.refresh();
    }

    private void loadUnfilteredPerformanceTable(int page) {
        PageRequestDTO pageRequestDTO;
        if (foundReservationsTableView.getSortOrder().size() > 0) {
            TableColumn<ReservationDTO, ?> sortedColumn = foundReservationsTableView.getSortOrder().get(0);
            Sort.Direction sortDirection = (sortedColumn.getSortType() == TableColumn.SortType.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC;
            pageRequestDTO = new PageRequestDTO(page, RESERVATIONS_PER_PAGE, sortDirection, getColumnNameBy(sortedColumn));
        } else {
            pageRequestDTO = new PageRequestDTO(page, RESERVATIONS_PER_PAGE, Sort.Direction.ASC, null);
        }

        try {
            PageResponseDTO<ReservationDTO> response = reservationService.findAll(pageRequestDTO);
            reservationDTOS.addAll(response.getContent());
            totalPages = response.getTotalPages();
        } catch (DataAccessException e) {
            LOGGER.error("Couldn't fetch reservations from server!");

        }
    }

    private void loadFilteredPerformanceTable(int page) {
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
                reservationService.findAllByPaidFalseByCustomerNameAndPerformanceName(reservationSearchBuilder.build());
            reservationDTOS.addAll(response.getContent());
            this.page = page;
            totalPages = response.getTotalPages();
        } catch (DataAccessException e) {
            LOGGER.error("Couldn't fetch reservations from server!");
            JavaFXUtils.createErrorDialog(e.getMessage(),
                content.getScene().getWindow()).showAndWait();
        }
    }

    private String getColumnNameBy(TableColumn<ReservationDTO, ?> sortedColumn) {
        if (sortedColumn == eventColumn) {
            return "performance.event.name";
        } else if (sortedColumn == customerColumn) {
            return "customer.lastName";
        } else if (sortedColumn == paidColumn) {
            return "paid";
        } else if(sortedColumn == reservationIDColumn){
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
            if (cellData.getValue().isPaid()) {
                return new SimpleStringProperty(BundleManager.getBundle().getString("bookings.table.paid.true"));
            } else {
                return new SimpleStringProperty(BundleManager.getBundle().getString("bookings.table.paid.false"));
            }
        });

        foundReservationsTableView.setItems(reservationDTOS);
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
        reservationDTOS.clear();
        page = 0;
        totalPages = 0;
    }

    public void showReservationDetailsButton() {
        Stage stage = new Stage();
        int row = foundReservationsTableView.getSelectionModel().getFocusedIndex();
        PRSController.showReservationDetails(reservationDTOS.get(row), stage);

        Parent parent = fxmlLoader.load("/fxml/events/book/purchaseReservationSummary.fxml");

        stage.setScene(new Scene(parent));
        stage.setTitle("Reservation Overview");

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(showReservationDetailsButton.getScene().getWindow());

        stage.showAndWait();

        clear();
        loadData();
    }

    public void searchForReservations() {
        performanceName = performanceNameField.getText();
        customerFirstName = customerFirstNameField.getText();
        customerLastName = customerLastNameField.getText();
        String reservationNumber = reservationNrField.getText();
        activeFilters = "";

        if ((!performanceName.equals(""))
            && (!customerFirstName.equals(""))
            && (!customerLastName.equals(""))
            && (reservationNumber.equals(""))) {

            clear();
            activeFilters += BundleManager.getBundle().getString("bookings.main.activefilters.performancename")
                + " " + performanceName + ", "
                + BundleManager.getBundle().getString("bookings.main.activefilters.customername")
                + " " + customerFirstName
                + " " + customerLastName;

            loadFilteredPerformanceTable(0);
            foundReservationsTableView.refresh();
            filtered = true;
            LOGGER.debug("Found {} page(s) satisfying the given criteria", totalPages);
        } else if ((performanceName.equals(""))
            && (customerFirstName.equals(""))
            && (customerLastName.equals(""))
            && (!reservationNumber.equals(""))) {

            if (reservationNumber.length() != RESERVATION_NUMBER_LENGTH) {
                LOGGER.error("The reservationnumber must be {} characters long! Was {}!", RESERVATION_NUMBER_LENGTH,
                    reservationNumber.length());
                JavaFXUtils.createErrorDialog(
                    BundleManager.getExceptionBundle().getString("exception.search.invalid.reservationnr.invalid.length"),
                    content.getScene().getWindow()
                ).showAndWait();
            } else {
                clear();
                activeFilters += BundleManager.getBundle().getString("bookings.main.activefilters.reservationnr") + " "
                    + reservationNumber;
                try {
                    ReservationDTO response =
                        reservationService.findOneByPaidFalseAndReservationNumber(reservationNumber);
                    reservationDTOS.clear();
                    reservationDTOS.addAll(response);
                } catch (DataAccessException e) {
                    LOGGER.error("Couldn't fetch reservations from server!");
                    JavaFXUtils.createErrorDialog(e.getMessage(),
                        content.getScene().getWindow()).showAndWait();
                }
                foundReservationsTableView.refresh();
                filtered = true;
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
        activeFiltersListLabel.setText(BundleManager.getBundle().getString("bookings.main.criteria.placeholder"));
        performanceNameField.setText("");
        customerFirstNameField.setText("");
        customerLastNameField.setText("");
        reservationNrField.setText("");

        performanceName = "";
        customerFirstName = "";
        customerLastName = "";
        activeFilters = "";
        clear();
        loadData();
    }
}
