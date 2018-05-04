package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

public class EventDetailViewController {

    @FXML
    private Label eventHeading;

    @FXML
    private Label eventNameEvent;

    @FXML
    private Label artistNameEvent;

    @FXML
    private Label descriptionEvent;

    @FXML
    private Label eventTypeEvent;

    @FXML
    private TableView<?> performanceDatesTableView;

    @FXML
    private Button bookButtonEvent;

    @FXML
    private void changeToPerformanceDetailView(ActionEvent event) {

    }

    private void fill(){

    }

}
