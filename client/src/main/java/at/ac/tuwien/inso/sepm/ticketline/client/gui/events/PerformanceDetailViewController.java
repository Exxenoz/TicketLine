package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PerformanceDetailViewController {

    @FXML
    private Label performanceHeader;

    @FXML
    private Label locationName;

    @FXML
    private Label startTime;

    @FXML
    private Label artistNamePerformance;

    @FXML
    private Label performancePrice;

    @FXML
    private Button eventButtonPerformance;

    @FXML
    private Button bookButtonPerformance;

    @FXML
    void bookPerformance(ActionEvent event) {

    }

    @FXML
    void changeToEventDetailView(ActionEvent event) {

    }

    private void fill(PerformanceDTO performance) {
        performanceHeader.setText(performance.getEvent().getName());
        locationName.setText(performance.getAddress().getBuildingName() + ", " + performance.getAddress().getLocation());
        startTime.setText(performance.getPerformanceStart().toString());
        artistNamePerformance.setText(performance.getEvent().getArtists().toString());
        performancePrice.setText(performance.getPrice().toString());

    }
}
