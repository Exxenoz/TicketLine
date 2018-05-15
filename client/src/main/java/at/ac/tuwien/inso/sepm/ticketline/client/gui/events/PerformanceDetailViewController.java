package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.stream.Collectors;

@Component
public class PerformanceDetailViewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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

    private PerformanceDTO performance;

    private final SpringFxmlLoader fxmlLoader;
    private final EventDetailViewController eventDetailViewController;
    private Stage stage;

    public PerformanceDetailViewController(
        SpringFxmlLoader fxmlLoader,
        EventDetailViewController eventDetailViewController
    ) {
        this.fxmlLoader = fxmlLoader;
        this.eventDetailViewController = eventDetailViewController;
    }

    @FXML
    private void initialize() {
        performanceHeader.setText(performance.getEvent().getName());
        locationName.setText(performance.getAddress().getLocationName() + ", " + performance.getAddress().getCity());
        startTime.setText(performance.getPerformanceStart().toString());

        String artistList = performance.getEvent().getArtists()
            .stream()
            .map(artist -> artist.getFirstName() + " " + artist.getLastName())
            .collect(Collectors.joining(", "));
        artistNamePerformance.setText(artistList);
        performancePrice.setText(performance.getPrice().toString());
    }

    @FXML
    void bookPerformance(ActionEvent event) {

    }

    @FXML
    void changeToEventDetailView(ActionEvent event) {
        eventDetailViewController.fill(performance.getEvent(), stage);
        final var parent = fxmlLoader.<Parent>load("/fxml/events/eventDetailView.fxml");
        stage.setScene(new Scene(parent));
        stage.setTitle("Event Details");
        stage.centerOnScreen();
    }

    public void fill(PerformanceDTO performance, Stage stage) {
        this.performance = performance;
        this.stage = stage;

    }
}
