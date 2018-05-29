package at.ac.tuwien.inso.sepm.ticketline.client.gui.reservations;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.booking.PurchaseReservationSummaryController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.ReservationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import java.lang.invoke.MethodHandles;

import static org.controlsfx.glyphfont.FontAwesome.Glyph.TICKET;

@Component
public class ReservationsSearchController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ReservationService reservationService;
    private final PurchaseReservationSummaryController PRSController;
    public TableColumn<ReservationDTO, String> reservationIDColumn;
    public TableColumn<ReservationDTO, String> eventColumn;
    public TableColumn<ReservationDTO, String> customerColumn;
    public TableColumn<ReservationDTO, String> paidColumn;
    public TableView<ReservationDTO> foundReservationsTableView;
    public Button showReservationDetailsButton;
    private final SpringFxmlLoader fxmlLoader;
    private ObservableList<ReservationDTO> reservationDTOS = FXCollections.observableArrayList();


    @FXML
    private TabHeaderController tabHeaderController;
    private int page = 0;
    private int totalPages = 0;
    private static final int RESERVATIONS_PER_PAGE = 25;

    public ReservationsSearchController(SpringFxmlLoader fxmlLoader,
                                        ReservationService reservationService,
                                        PurchaseReservationSummaryController PRSController) {
        this.fxmlLoader = fxmlLoader;
        this.reservationService = reservationService;
        this.PRSController = PRSController;
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
            loadPerformanceTable(0);
            return true;
        });

        final ScrollBar scrollBar = getVerticalScrollbar(foundReservationsTableView);
        if (scrollBar != null) {
            scrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
                double value = newValue.doubleValue();
                if ((value == scrollBar.getMax()) && (page < totalPages)) {
                    page++;
                    LOGGER.debug("Getting next Page: {}", page);
                    double targetValue = value * reservationDTOS.size();
                    loadPerformanceTable(page);
                    scrollBar.setValue(targetValue / reservationDTOS.size());
                }
            });
        }
    }

    public void loadPerformanceTable(int page){
        PageRequestDTO pageRequestDTO = null;
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
            LOGGER.error("Couldn't fetch performances from server!", e);
        }
        foundReservationsTableView.refresh();
    }

    private String getColumnNameBy(TableColumn<ReservationDTO, ?> sortedColumn) {
        if (sortedColumn == eventColumn) {
            return "eventName";
        } else if (sortedColumn == customerColumn) {
            return "customerName";
        } else if (sortedColumn == paidColumn) {
            return "status";
        }
        return "id";
    }

    private void initializeTableView() {

        reservationIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getId().toString()));
        eventColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getPerformance().getEvent().getName()));
        customerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getCustomer().getFirstName() + " " +
                cellData.getValue().getCustomer().getLastName()));
        paidColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().isPaid().toString()));

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
    }

    public void searchForReservations() {
    }

}
