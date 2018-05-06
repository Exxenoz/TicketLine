package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
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
    private TableView<PerformanceDTO> performanceDatesTableView;

    @FXML
    private Button bookButtonEvent;

    @FXML
    private void changeToPerformanceDetailView(ActionEvent event) {

    }

    private void fill(EventDTO event) {
        eventHeading.setText(event.getName());
        eventNameEvent.setText(event.getName());
        artistNameEvent.setText(event.getArtists().toString());
        descriptionEvent.setText(event.getDescription());
        if (event.getEventType().toString().equals("SEATS")) {
            eventTypeEvent.setText("yes");
        } else {
            eventTypeEvent.setText("no");
        }

        //TODO: Get performances for table View

    }

}
