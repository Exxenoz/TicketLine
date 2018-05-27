package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.booking;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.PerformanceDetailViewController;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventTypeDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.List;

@Component
public class HallPlanController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public Label eventNameLabel;
    public Label performanceNameLabel;
    public Label amountOfTicketsLabel;
    public Label seatsOrSectorsLabel;
    public Label rowsSeatsOrSectorLabel;
    public Label amountTickets;
    public Label pricePerTicket;
    public Label totalPrice;
    public Label hallHeading;
    private final PurchaseReservationSummaryController PRSController;
    public Button continueButton;
    public Button backButton;

    private final SpringFxmlLoader fxmlLoader;
    private final SelectCustomerController selectCustomerController;
    public Button reserveButton;
    private Stage stage;
    private ReservationDTO reservation;
    private List<SeatDTO> seats;
    private boolean isReservation = false;
    private boolean changeDetails = false;


    public HallPlanController(SpringFxmlLoader fxmlLoader,
                              SelectCustomerController selectCustomerController,
                              @Lazy PerformanceDetailViewController performanceDetailViewController,
                              @Lazy PurchaseReservationSummaryController PRSController) {
        this.fxmlLoader = fxmlLoader;
        this.selectCustomerController = selectCustomerController;
        this.PRSController = PRSController;
    }

    @FXML
    private void initialize() {

        eventNameLabel.setText(reservation.getPerformance().getEvent().getName());
        performanceNameLabel.setText(reservation.getPerformance().getName());
        amountOfTicketsLabel.setText("0");
        //TODO: amountOfTicketsLabel should change with amount of chosen seats
        //TODO: Connect display of chosen seats/sectors with hallplan (rowsSeatsOrSectorsLabel)
        //TODO: pricePerTicket & totalPrice
        if (reservation.getPerformance().getEvent().getEventType() == EventTypeDTO.SECTOR) {
            seatsOrSectorsLabel.setText("Chosen Sector: ");
            hallHeading.setText("Choose your Sector");
        }

        if (!changeDetails) {
            seats = new LinkedList<>();
        } else {
            seats = reservation.getSeats();
            continueButton.setText("Save Changes");
            backButton.setText("Cancel Editing");

            reserveButton.setDisable(true);
            reserveButton.setVisible(false);
            reserveButton.setManaged(false);
        }
    }

    @FXML
    public void backButton(ActionEvent event) {
        Parent parent = fxmlLoader.load("/fxml/events/performanceDetailView.fxml");
        stage.setScene(new Scene(parent));
        stage.setTitle("Performance Details");
        stage.centerOnScreen();
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
            PRSController.fill(reservation, isReservation, stage);
            Parent parent = fxmlLoader.load("/fxml/events/book/purchaseReservationSummary.fxml");
            stage.setScene(new Scene(parent));
            stage.setTitle("Reservation Overview");
            stage.centerOnScreen();
        }
    }


    public void fill(ReservationDTO reservation, Stage stage) {
        this.reservation = reservation;
        this.stage = stage;
    }

    public void changeReservationDetails(ReservationDTO reservation, Stage stage) {
        this.reservation = reservation;
        this.stage = stage;
        this.changeDetails = true;
        this.isReservation = true;
    }
}