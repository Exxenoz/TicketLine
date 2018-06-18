package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.booking;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.ReservationService;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.CreateReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.List;

@Component
public class PurchaseReservationSummaryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public Button buyButtonPRS;
    public Button cancelButtonPRS;
    private final HallPlanController hallPlanController;

    public Label performancePrice;
    public Label eventName;
    public Label performanceName;
    public Label ticketsNr;
    public Label performanceHeader;

    private final SpringFxmlLoader fxmlLoader;
    private Stage stage;

    private ReservationDTO reservation;
    private final ReservationService reservationService;
    private boolean isReservation = false;
    private boolean showDetails = false;

    PurchaseReservationSummaryController(
        SpringFxmlLoader fxmlLoader,
        ReservationService reservationService,
        @Lazy HallPlanController hallPlanController) {

        this.fxmlLoader = fxmlLoader;
        this.reservationService = reservationService;
        this.hallPlanController = hallPlanController;
    }

    @FXML
    private void initialize() {
        LOGGER.debug("Entering initialize method of PRSController.");
        eventName.setText(reservation.getPerformance().getEvent().getName());
        performanceName.setText(reservation.getPerformance().getName());
        String totalAmountTickets = "" + reservation.getSeats().size();
        ticketsNr.setText(totalAmountTickets);
        performancePrice.setText(reservation.getPerformance().getPrice().toString());

        if (isReservation) {
            performanceHeader.setText("Reservation Summary");
            buyButtonPRS.setText("Continue");
        }

        if (showDetails) {
            performanceHeader.setText("Reservation Overview");
            //make sure the reservation is still unpaid
            if (!reservation.isPaid()) {
                cancelButtonPRS.setText("Edit Reservation");
            } else {
                cancelButtonPRS.setDisable(true);
                cancelButtonPRS.setVisible(false);
                cancelButtonPRS.setManaged(false);
                buyButtonPRS.setDisable(true);
                buyButtonPRS.setVisible(false);
                buyButtonPRS.setManaged(false);
            }
        }
    }

    public void buyTicketsButton(ActionEvent event) throws DataAccessException {
        CreateReservationDTO createReservationDTO = new CreateReservationDTO();
        createReservationDTO.setCustomerID(reservation.getCustomer() != null ? reservation.getCustomer().getId() : null);
        createReservationDTO.setPerformanceID(reservation.getPerformance().getId());

        List<SeatDTO> seatDTOS = new LinkedList<>();
        reservation.getSeats().forEach(seatDTO -> seatDTOS.add(seatDTO));
        createReservationDTO.setSeats(seatDTOS);

        //only reserve tickets
        if (!showDetails && isReservation) {
            ReservationDTO reservationDTO = reservationService.createNewReservation(createReservationDTO);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Print Invoice");
            alert.setHeaderText("Congratulations! Your Reservation was successful!" + reservationDTO.getReservationNumber());
            alert.showAndWait();
            closeWindow();
        } else if (!showDetails) {
            //reserve and buy tickets
            reservationService.createAndPayReservation(createReservationDTO);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Print Invoice");
            alert.setHeaderText("Congratulations! Your Purchase was successful!" + "\n" + "Do you want to print the invoice?");
            alert.showAndWait();
            closeWindow();
        } else {
            //buy already reserved tickets
            reservationService.purchaseReservation(reservation);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Print Invoice");
            alert.setHeaderText("Congratulations! Your Purchase was successful!" + "\n" + "Do you want to print the invoice?");
            alert.showAndWait();
            closeWindow();
        }
    }

    public void cancelButton(ActionEvent event) {
        if (showDetails) {
            hallPlanController.changeReservationDetails(reservation, stage);
            Parent parent = fxmlLoader.load("/fxml/events/book/hallPlanView.fxml");
            stage.setScene(new Scene(parent));
        } else {
            closeWindow();
        }
    }

    public void backButton(ActionEvent event) {
        if (showDetails) {
            closeWindow();
        } else {
            Parent parent = fxmlLoader.load("/fxml/events/book/selectCustomerView.fxml");
            stage.setScene(new Scene(parent));
            stage.setTitle("Customer Details");
            stage.centerOnScreen();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) performancePrice.getScene().getWindow();
        stage.close();
    }

    public void fill(ReservationDTO reservation, boolean isReservation, Stage stage) {
        this.reservation = reservation;
        this.isReservation = isReservation;
        this.stage = stage;
    }

    public void showReservationDetails(ReservationDTO reservation, Stage stage) {
        this.reservation = reservation;
        this.stage = stage;
        this.showDetails = true;
    }

}
