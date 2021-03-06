package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.booking;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.PerformanceDetailViewController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.SeatMapController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.SeatSelectionListener;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.SectorController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.ReservationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventTypeDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PriceUtils;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
    private TableColumn sectorColumn;
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
    @FXML
    AnchorPane controllerPane;

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
    private boolean isSeatMapMode = false;

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

        LOGGER.info("Initialize HallPlanController");
        eventNameLabel.setText(reservation.getPerformance().getEvent().getName());
        performanceNameLabel.setText(reservation.getPerformance().getName());
        if (seats != null) {
            amountOfTicketsLabel.setText("" + seats.size());
        } else {
            amountOfTicketsLabel.setText("0");
        }

        //Initialize correct seat picker layout
        Parent root;
        if (reservation.getPerformance().getEvent().getEventType() == EventTypeDTO.SEAT) {
            root = fxmlLoader.load("/fxml/reservation/seatMapPicker.fxml");
            isSeatMapMode = true;
        } else {
            root = fxmlLoader.load("/fxml/reservation/sectorSeatPicker.fxml");
            isSeatMapMode = false;
        }
        controllerPane.getChildren().add(root);

        sectorColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SeatDTO, Integer>, ObservableValue>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<SeatDTO, Integer> param) {
                return new SimpleStringProperty("" + param.getValue().getSector().getHallNumber() + 1);
            }
        });

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
                if (isSeatMapMode) {
                    this.seatMapController.setSeatSelectionListener(this);
                    this.seatMapController.fill(this.reservation.getPerformance(), reservationDTOS);
                } else {
                    this.sectorController.setSeatSelectionListener(this);
                    this.sectorController.fill(this.reservation.getPerformance(), reservationDTOS);
                }
            }
        }

        //Just intialize fields
        updateSeatsInformation(seats);
        updatePrice(seats, reservation.getPerformance());
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
        LOGGER.info("User clicked the back button");
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
        LOGGER.info("User clicked the buy button");
        continueOrReserve();
    }

    @FXML
    public void reserveButton() {
        LOGGER.info("User clicked the reserve button");
        isReservation = true;
        continueOrReserve();
    }

    @Override
    public void onSeatSelected(SeatDTO seatDTO) {
        LOGGER.info("User selected a Seat {}", seatDTO);
        seats.add(seatDTO);
        updateSeatsInformation(this.seats);
        updatePrice(this.seats, this.reservation.getPerformance());
    }

    @Override
    public void onSeatDeselected(SeatDTO seatDTO) {
        LOGGER.info("User deselected a Seat {}", seatDTO);
        seats.remove(seatDTO);
        updateSeatsInformation(this.seats);
        updatePrice(this.seats, this.reservation.getPerformance());
    }

    private void continueOrReserve() {
        if(!seats.isEmpty()) {
            reservation.setSeats(seats);
            if (!changeDetails) {
                selectCustomerController.fill(reservation, isReservation, stage);
                Parent parent = fxmlLoader.load("/fxml/events/book/selectCustomerView.fxml");
                selectCustomerController.loadCustomers();
                stage.setScene(new Scene(parent));
                stage.setTitle(BundleManager.getBundle().getString("bookings.hallplan.customer_select.title"));
                stage.centerOnScreen();
            } else {
                LOGGER.debug("Reservation was changed, trying to update...");
                try {
                    reservation = reservationService.editReservation(reservation);
                    PRSController.showReservationDetails(reservation, stage);
                    Parent parent = fxmlLoader.load("/fxml/events/book/purchaseReservationSummary.fxml");
                    stage.setScene(new Scene(parent));
                    stage.setTitle(BundleManager.getBundle().getString("bookings.purchase.details.title"));
                    stage.centerOnScreen();
                    LOGGER.debug("Update of Reservation was successful");
                } catch (DataAccessException e) {
                    LOGGER.debug("An error occurred during the update: {}", e.getMessage());
                    JavaFXUtils.createErrorDialog(e.getMessage(),
                        stage.getScene().getWindow()).showAndWait();
                }
            }
        } else {
            JavaFXUtils.createInformationDialog(
                BundleManager.getBundle().getString("events.seating.info.title"),
                BundleManager.getBundle().getString("events.seating.info"),
                BundleManager.getBundle().getString("events.seating.no.seats"),
                stage.getScene().getWindow()).showAndWait();
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

            if (isSeatMapMode) {
                seatMapController.fillForReservationEdit(reservation);
            } else {
                sectorController.fillForReservationEdit(reservation);
            }
        });
    }

    private void closeWindow() {
        Stage stage = (Stage) performanceNameLabel.getScene().getWindow();
        stage.close();
    }
}