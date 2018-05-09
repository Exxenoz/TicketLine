package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

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

    private EventDetailViewController eventDetailViewController;

    private PerformanceDTO performance;

    public PerformanceDetailViewController() {

    }

    @FXML
    private void initialize(){
        performanceHeader.setText(performance.getEvent().getName());
        locationName.setText(performance.getAddress().getLocationName() + ", " + performance.getAddress().getCity());
        startTime.setText(performance.getPerformanceStart().toString());
        artistNamePerformance.setText(performance.getEvent().getArtists().toString());
        performancePrice.setText(performance.getPrice().toString());
    }

    @FXML
    void bookPerformance(ActionEvent event) {

    }

    @FXML
    void changeToEventDetailView(ActionEvent event) {
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/events/performanceDetailView.fxml"));
        fxmlLoader.setControllerFactory(classToLoad -> classToLoad.isInstance(eventDetailViewController) ? eventDetailViewController : null);

        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setTitle("Event Details");

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(eventButtonPerformance.getScene().getWindow());

            stage.showAndWait();
        } catch (IOException e) {
            LOGGER.error("Detail View Event window couldn't be opened!");
        }

    }

    public void fill(PerformanceDTO performance, EventDetailViewController eventDetailViewController) {
       this.performance = performance;
        this.eventDetailViewController = eventDetailViewController;

    }
}
