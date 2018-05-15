package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.List;

import static javafx.stage.Modality.WINDOW_MODAL;

@Component
public class EventDetailViewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
    private TableColumn<PerformanceDTO, String> endTimeColumn;

    @FXML
    private TableColumn<PerformanceDTO, String> startTimeColumn;

    @FXML
    private TableColumn<PerformanceDTO, String> locationColumn;

    private List<PerformanceDTO> performances;

    private EventDTO event;

    private ObservableList<PerformanceDTO> performanceData = FXCollections.observableArrayList();

    private final SpringFxmlLoader fxmlLoader;
    private Stage stage;

    public EventDetailViewController(
        SpringFxmlLoader fxmlLoader
    ) {
        this.fxmlLoader = fxmlLoader;
    }

    @FXML
    private Button bookButtonEvent;


    @FXML
    private void initilaize() {
        eventHeading.setText(event.getName());
        eventNameEvent.setText(event.getName());
        artistNameEvent.setText(event.getArtists().toString());
        descriptionEvent.setText(event.getDescription());
        if (event.getEventType().toString().equals("SEATS")) {
            eventTypeEvent.setText("yes");
        } else {
            eventTypeEvent.setText("no");
        }
    }

    @FXML
    private void changeToPerformanceDetailView() {
        // TODO change to selected performance
        // performanceDetailViewController.fill(, stage);
        Parent parent = fxmlLoader.load("/fxml/events/performanceDetailView.fxml");
        stage.setScene(new Scene(parent));
        stage.setTitle("Event Details");
    }

    public void fill(EventDTO event, Stage stage) {
        this.event = event;
        this.stage = stage;
    }

    private void intializeTableView() {
        startTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPerformanceStart().toString()));
        endTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPerformanceEnd().toString()));
        locationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getCity()));

        performanceData = FXCollections.observableArrayList(performances);
        performanceDatesTableView.setItems(performanceData);
    }

}
