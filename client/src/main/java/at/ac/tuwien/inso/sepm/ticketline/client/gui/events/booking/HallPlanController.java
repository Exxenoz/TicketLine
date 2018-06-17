package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.booking;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.PerformanceDetailViewController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.SeatMapController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.SeatSelectionListener;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.SectorController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.ReservationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.PriceUtils;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventTypeDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.beans.property.ReadOnlyLongWrapper;
import javafx.beans.property.SimpleLongProperty;
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
    private Label seatsOrSectorsLabel;
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
        if(seats != null){
            amountOfTicketsLabel.setText("" + seats.size());
        } else {
            amountOfTicketsLabel.setText("0");
        }
        //Initialize table view
        seatsRowColumn.setCellValueFactory(new PropertyValueFactory<SeatDTO, Integer>("positionY"));
        seatsSeatColumn.setCellValueFactory(new PropertyValueFactory<SeatDTO, Integer>("positionX"));
        seatsPriceColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SeatDTO, Long>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<SeatDTO, Long> param) {
                return new SimpleStringProperty(Long.toString(
                    reservationService.calculateSinglePrice(param.getValue(), reservation.getPerformance())));
            }
        });

        if (reservation.getPerformance().getEvent().getEventType() == EventTypeDTO.SECTOR) {
            seatsOrSectorsLabel.setText("Chosen Sector: ");
            hallHeading.setText("Choose your Sector");
        }

        if (!changeDetails) {
            seats = new LinkedList<>();
        } else {
            seats = reservation.getSeats();

            if(seats == null || seats.isEmpty()) {
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

        // Set performance detail to seat plan
        if(this.reservation != null && this.reservation.getPerformance() != null) {
            if(reservationDTOS != null) {
                //Set this controller als seat selection listener for the seat map
                this.seatMapController.setSeatSelectionListener(this);
                this.seatMapController.fill(this.reservation.getPerformance(), reservationDTOS);
            }
        }
    }

    public void updateSeats(List<SeatDTO> seats) {
        seatsTableView.getItems().setAll(seats);
    }

    public void updatePrice(List<SeatDTO> seats, PerformanceDTO performanceDTO) {
        totalPrice.setText(PriceUtils.priceToRepresentation(reservationService.calculateCompletePrice(seats, performanceDTO)));
    }

    @FXML
    public void backButton(ActionEvent event) {
        if(changeDetails){
            closeWindow();
        }else {
            Parent parent = fxmlLoader.load("/fxml/events/performanceDetailView.fxml");
            stage.setScene(new Scene(parent));
            stage.setTitle("Performance Details");
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
        amountOfTicketsLabel.setText("" + seats.size());

        updateSeats(this.seats);
        updatePrice(this.seats, this.reservation.getPerformance());
    }

    @Override
    public void onSeatDeselected(SeatDTO seatDTO) {
        seats.remove(seatDTO);
        amountOfTicketsLabel.setText("" + seats.size());

        updateSeats(this.seats);
        updatePrice(this.seats, this.reservation.getPerformance());
    }

    private void continueOrReserve() {
        reservation.setSeats(seats);
        if (!changeDetails) {
            selectCustomerController.fill(reservation, isReservation, stage);
            Parent parent = fxmlLoader.load("/fxml/events/book/selectCustomerView.fxml");
            selectCustomerController.loadCustomers();
            stage.setScene(new Scene(parent));
            stage.setTitle("Customer Details");
            stage.centerOnScreen();
        } else {
            try {
                reservation = reservationService.editReservation(reservation);
                PRSController.showReservationDetails(reservation, stage);
                Parent parent = fxmlLoader.load("/fxml/events/book/purchaseReservationSummary.fxml");
                stage.setScene(new Scene(parent));
                stage.setTitle("Reservation Overview");
                stage.centerOnScreen();
            } catch (DataAccessException e) {
                JavaFXUtils.createErrorDialog(e.getMessage(), stage);
            }
        }
    }

    public void fill(ReservationDTO reservation, Stage stage) {
        this.reservation = reservation;
        this.stage = stage;
    }

    public void changeReservationDetails(ReservationDTO reservation, Stage stage) {
        //Set state
        this.reservation = reservation;
        this.stage = stage;
        this.changeDetails = true;
        this.isReservation = true;

        //Just prefill the seat controller for the reservation state
        seatMapController.fillForReservationEdit(reservation);
    }

    private void closeWindow() {
        Stage stage = (Stage) performanceNameLabel.getScene().getWindow();
        stage.close();
    }
}