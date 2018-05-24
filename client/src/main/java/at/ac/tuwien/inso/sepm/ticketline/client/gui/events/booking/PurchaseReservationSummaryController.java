package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.booking;

import at.ac.tuwien.inso.sepm.ticketline.client.service.PerformanceService;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class PurchaseReservationSummaryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public Button backButtonPRS;
    public Button cancelButtonPRS;
    public Button buyButtonPRS;

    public Label performancePrice;
    public Label eventName;
    public Label performanceName;
    public Label ticketsNr;

    private final SpringFxmlLoader fxmlLoader;
    private final PerformanceService performanceService;
    private Stage stage;

    private PerformanceDTO performance;

    PurchaseReservationSummaryController(
        SpringFxmlLoader fxmlLoader,
        PerformanceService performanceService
    ){
        this.fxmlLoader = fxmlLoader;
        this.performanceService = performanceService;
    }

    private void initialize(){
        LOGGER.debug("Entering initialize method of PRSController.");
        eventName.setText(performance.getEvent().getName());
        performanceName.setText(performance.getName());
        //TODO: tickets reinmergen
        //ticketsNr.setText();
        performancePrice.setText(performance.getPrice().toString());
    }

    public void buyTickets(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Print Invoice");
        alert.setHeaderText("Do you want to print the invoice?");
        alert.showAndWait();
    }

    public void cancel(ActionEvent event){
        LOGGER.debug("Entering onClickCancel.");
        closeWindow();
    }

    public void back(ActionEvent event){
        //TODO: back to select customer
    }

    private void closeWindow() {
        LOGGER.debug("Entering closeWindow method.");
        Stage stage = (Stage) performancePrice.getScene().getWindow();
        stage.close();
    }
}
