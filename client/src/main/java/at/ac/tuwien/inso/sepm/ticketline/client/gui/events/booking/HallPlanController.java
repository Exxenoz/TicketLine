package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.booking;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.PerformanceDetailViewController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.SeatMapController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.SeatSelectionListener;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.SectorController;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.service.ReservationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PriceUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventTypeDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.List;

import static javafx.application.Platform.runLater;

@Component
public class HallPlanController implements SeatSelectionListener {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private Stage stage;

    @FXML
    private Label eventNameLabel;
    @FXML
    private Label performanceNameLabel;
    @FXML
    private Label amountOfTicketsLabel;
    @FXML
    private Label totalPrice;
    @FXML
    private Label hallHeading;
    @FXML
    private TableView seatsTableView;
    @FXML
    public TableColumn seatsRowColumn;
    @FXML
    public TableColumn seatsSeatColumn;
    @FXML
    public TableColumn seatsPriceColumn;
    @FXML

    public Button continueButton;
    @FXML
    public Button backButton;
    @FXML
    public Button reserveButton;

    private final SpringFxmlLoader fxmlLoader;
    private final SelectCustomerController selectCustomerController;
    private final PurchaseReservationSummaryController PRSController;
    private final SeatMapController seatMapController;
    private final SectorController sectorController;
    private final ReservationService reservationService;

    private ReservationDTO reservation;
    private List<SeatDTO> seats;

    private boolean isReservation = false;
    private boolean changeDetails = false;

    public HallPlanController(SpringFxmlLoader fxmlLoader,
                              SelectCustomerController selectCustomerController,
                              @Lazy PerformanceDetailViewController performanceDetailViewController,
                              @Lazy PurchaseReservationSummaryController PRSController,
                              @Lazy SeatMapController seatMapController,
                              @Lazy SectorController sectorController,
                              ReservationService reservationService) {

        this.fxmlLoader = fxmlLoader;
        this.selectCustomerController = selectCustomerController;
        this.PRSController = PRSController;
        this.seatMapController = seatMapController;
        this.sectorController = sectorController;
        this.reservationService = reservationService;
    }

    @FXML
    private void initialize() {
        eventNameLabel.setText(reservation.getPerformance().getEvent().getName());
        performanceNameLabel.setText(reservation.getPerformance().getName());
        if (seats != null) {
            amountOfTicketsLabel.setText("" + seats.size());
        } else {
            amountOfTicketsLabel.setText("0");
        }
        //Initialize table view
        seatsRowColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SeatDTO, Integer>, ObservableValue>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<SeatDTO, Integer> param) {
                if (reservation.getPerformance().getEvent().getEventType() == EventTypeDTO.SEAT) {
                    //Increase position by one so we dont start at position 0
                    return new SimpleStringProperty(Integer.toString(param.getValue().getPositionY() + 1));
                } else {
                    return new SimpleStringProperty(BundleManager.getBundle().getString("events.seating.hallplan.freeseating"));
                }
            }
        });

        seatsSeatColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SeatDTO, Integer>, ObservableValue>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<SeatDTO, Integer> param) {
                if (reservation.getPerformance().getEvent().getEventType() == EventTypeDTO.SEAT) {
                    //Increase position by one so we dont start at position 0
                    return new SimpleStringProperty(Integer.toString(param.getValue().getPositionX() + 1));
                } else {
                    return new SimpleStringProperty(BundleManager.getBundle().getString("events.seating.hallplan.freeseating"));
                }
            }
        });

        seatsPriceColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SeatDTO, Long>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<SeatDTO, Long> param) {
                return new SimpleStringProperty(
                    PriceUtils.priceToRepresentation(reservationService.calculateSinglePrice(param.getValue(), reservation.getPerformance())));
            }
        });

        if (reservation.getPerformance().getEvent().getEventType() == EventTypeDTO.SECTOR) {
            hallHeading.setText("Choose your Sector");
        }

        if (!changeDetails) {
            seats = new LinkedList<>();
            continueButton.setText("Continue");
            backButton.setText("Back");

            reserveButton.setDisable(false);
            reserveButton.setVisible(true);
            reserveButton.setManaged(true);

        } else {
            seats = reservation.getSeats();

            if (seats == null || seats.isEmpty()) {
                seats = new LinkedList<>();
            }
            continueButton.setText("Save Changes");
            backButton.setText("Cancel Editing");

            reserveButton.setDisable(true);
            reserveButton.setVisible(false);
            reserveButton.setManaged(false);
        }

        List<ReservationDTO> reservationDTOS = null;
        try {
            reservationDTOS = this.reservationService.findReservationsForPerformance(reservation.getPerformance().getId());
        } catch (DataAccessException d) {
            d.printStackTrace();
        }

        // Set performance detail to seat or sector plan plan
        if (this.reservation != null && this.reservation.getPerformance() != null) {
            if (reservationDTOS != null) {
                //Set this controller als seat selection listener for the seat map
                if (seatMapController.isInitialized()) {
                    this.seatMapController.setSeatSelectionListener(this);
                    this.seatMapController.fill(this.reservation.getPerformance(), reservationDTOS);
                } else if (sectorController != null) {
                    this.sectorController.setSeatSelectionListener(this);
                    this.sectorController.fill(this.reservation.getPerformance(), reservationDTOS);
                }
            }
        }
    }

    public void updateSeatsInformation(List<SeatDTO> seats) {
        amountOfTicketsLabel.setText("" + seats.size());
        seatsTableView.getItems().setAll(seats);
    }

    public void updatePrice(List<SeatDTO> seats, PerformanceDTO performanceDTO) {
        totalPrice.setText(PriceUtils.priceToRepresentation(reservationService.calculateCompletePrice(seats, performanceDTO)));
    }

    @FXML
    public void backButton(ActionEvent event) {
        if (changeDetails) {
            closeWindow();
        } else {
            Parent parent = fxmlLoader.load("/fxml/events/performanceDetailView.fxml");
            stage.setScene(new Scene(parent));
            stage.setTitle(BundleManager.getBundle().getString("bookings.performance.details.title"));
            stage.centerOnScreen();
        }
    }

    @FXML
    public void continueButton(ActionEvent event) {
        continueOrReserve();
    }

    @FXML
    public void reserveButton() {
        isReservation = true;
        continueOrReserve();
    }

    @Override
    public void onSeatSelected(SeatDTO seatDTO) {
        seats.add(seatDTO);
        updateSeatsInformation(this.seats);
        updatePrice(this.seats, this.reservation.getPerformance());
    }

    @Override
    public void onSeatDeselected(SeatDTO seatDTO) {
        seats.remove(seatDTO);
        updateSeatsInformation(this.seats);
        updatePrice(this.seats, this.reservation.getPerformance());
    }

    private void continueOrReserve() {
        reservation.setSeats(seats);
        if (!changeDetails) {
            selectCustomerController.fill(reservation, isReservation, stage);
            Parent parent = fxmlLoader.load("/fxml/events/book/selectCustomerView.fxml");
            selectCustomerController.loadCustomers();
            stage.setScene(new Scene(parent));
            stage.setTitle(BundleManager.getBundle().getString("bookings.hallplan.customer_select.title"));
            stage.centerOnScreen();
        } else {
            try {
                reservation = reservationService.editReservation(reservation);
                PRSController.showReservationDetails(reservation, stage);
                Parent parent = fxmlLoader.load("/fxml/events/book/purchaseReservationSummary.fxml");
                stage.setScene(new Scene(parent));
                stage.setTitle(BundleManager.getBundle().getString("bookings.purchase.details.title"));
                stage.centerOnScreen();
            } catch (DataAccessException e) {
                JavaFXUtils.createErrorDialog(e.getMessage(), stage);
            }
        }
    }

    public void fill(ReservationDTO reservation, Stage stage) {
        this.changeDetails = false;
        this.isReservation = false;
        this.reservation = reservation;
        this.stage = stage;
    }

    public void changeReservationDetails(ReservationDTO reservation, Stage stage) {
        //Set state
        this.reservation = reservation;
        this.stage = stage;
        this.changeDetails = true;
        this.isReservation = true;


        runLater(() -> {
            updateSeatsInformation(reservation.getSeats());
            updatePrice(reservation.getSeats(), this.reservation.getPerformance());

            if (seatMapController.isInitialized()) {
                seatMapController.fillForReservationEdit(reservation);
            } else {

            }
        });
    }

    private void closeWindow() {
        Stage stage = (Stage) performanceNameLabel.getScene().getWindow();
        stage.close();
    }
}