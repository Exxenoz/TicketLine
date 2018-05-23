package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.booking;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.PerformanceDetailViewController;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventTypeDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

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

    private final SpringFxmlLoader fxmlLoader;
    private final SelectCustomerController selectCustomerController;
    private final PerformanceDetailViewController performanceDetailViewController;
    private PerformanceDTO performance;
    private Stage stage;

    public HallPlanController(SpringFxmlLoader fxmlLoader, SelectCustomerController selectCustomerController, @Lazy PerformanceDetailViewController performanceDetailViewController){
        this.fxmlLoader = fxmlLoader;
        this.selectCustomerController = selectCustomerController;
        this.performanceDetailViewController = performanceDetailViewController;
    }

    @FXML
    private void initialize(){
        eventNameLabel.setText(performance.getEvent().getName());
        performanceNameLabel.setText(performance.getName());
        //TODO: amountOfTicketsLabel should change with amount of chosen seats
        amountOfTicketsLabel.setText("0");
        if(performance.getEvent().getEventType() == EventTypeDTO.SECTOR){
           seatsOrSectorsLabel.setText("Chosen Sector: ");
           hallHeading.setText("Choose your Sector");
        }
        //TODO: Connect display of chosen seats/sectors with hallplan (rowsSeatsOrSectorsLabel)
        //TODO: pricePerTicket & totalPrice

    }

    @FXML
    public void backButton(ActionEvent event){
        Parent parent = fxmlLoader.load("/fxml/events/performanceDetailView.fxml");
        stage.setScene(new Scene(parent));
        stage.setTitle("Performance Details");
        stage.centerOnScreen();
    }

    @FXML
    public void continueButton(ActionEvent event){
        selectCustomerController.fill(performance, stage);
        Parent parent = fxmlLoader.load("/fxml/events/book/selectCustomerView.fxml");
        stage.setScene(new Scene(parent));
        stage.setTitle("Customer Details");
        stage.centerOnScreen();

        //TODO: save chosen seats/sectors to event
    }

    @FXML
    public void reserveButton(){

    }

    public void fill(PerformanceDTO performance, Stage stage){
        this.performance = performance;
        this.stage = stage;
    }
}
