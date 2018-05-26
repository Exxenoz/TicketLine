package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.booking;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.PerformanceService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.ReservationService;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.CreateReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class PurchaseReservationSummaryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public Button buyButtonPRS;

    public Label performancePrice;
    public Label eventName;
    public Label performanceName;
    public Label ticketsNr;
    public Label performanceHeader;

    private final SpringFxmlLoader fxmlLoader;
    private Stage stage;

    private ReservationDTO reservation;
    private boolean isReservation;
    private final ReservationService reservationService;

    PurchaseReservationSummaryController(
        SpringFxmlLoader fxmlLoader,
        ReservationService reservationService
    ){
        this.fxmlLoader = fxmlLoader;
        this.reservationService = reservationService;
    }

    @FXML
    private void initialize(){
        LOGGER.debug("Entering initialize method of PRSController.");
        eventName.setText(reservation.getPerformance().getEvent().getName());
        performanceName.setText(reservation.getPerformance().getName());
        String totalAmountTickets = "" + reservation.getSeats().size();
        ticketsNr.setText(totalAmountTickets);
        performancePrice.setText(reservation.getPerformance().getPrice().toString());

        if(isReservation){
            performanceHeader.setText("Reservation Summary");
            buyButtonPRS.setText("Continue");
        }
    }

    public void buyTickets(ActionEvent event) throws DataAccessException {

            CreateReservationDTO createReservationDTO = new CreateReservationDTO();
            createReservationDTO.setCustomerID(reservation.getCustomer().getId());
            createReservationDTO.setPerformanceID(reservation.getPerformance().getId());
            List<Long> seatsID = new LinkedList<>();
            reservation.getSeats().forEach(seatDTO -> seatsID.add(seatDTO.getId()));
            createReservationDTO.setSeatIDs(seatsID);

        if(isReservation){
            reservationService.createNewReservation(createReservationDTO);
            closeWindow();
        } else {
            reservationService.createAndPayReservation(createReservationDTO);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Print Invoice");
            alert.setHeaderText("Congratulations! Your Purchase was successful!" + "\n" + "Do you want to print the invoice?");
            alert.showAndWait();
            closeWindow();
        }
    }

    public void cancelButton(ActionEvent event){
        LOGGER.debug("Entering onClickCancel.");
        closeWindow();
    }

    public void backButton(ActionEvent event){
        Parent parent = fxmlLoader.load("/fxml/events/book/selectCustomerView.fxml");
        stage.setScene(new Scene(parent));
        stage.setTitle("Customer Details");
        stage.centerOnScreen();
    }

    private void closeWindow() {
        LOGGER.debug("Entering closeWindow method.");
        Stage stage = (Stage) performancePrice.getScene().getWindow();
        stage.close();
    }

    public void fill(ReservationDTO reservation, boolean isReservation, Stage stage){
        this.reservation = reservation;
        this.isReservation = isReservation;
        this.stage = stage;

    }

}
