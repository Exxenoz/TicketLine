package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.PerformanceService;
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.ArtistDTO;
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
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private void initialize() {

    }

    @FXML
    private void changeToPerformanceDetailView() {
        // TODO change to selected performance
        // performanceDetailViewController.fill(, stage);
        Parent parent = fxmlLoader.load("/fxml/bookings/performanceDetailView.fxml");
        stage.setScene(new Scene(parent));
        stage.setTitle("Event Details");
    }

    public void fill(PerformanceService performanceService, EventDTO event, Stage stage) throws DataAccessException {
        this.event = event;
        this.stage = stage;
        eventHeading.setText(event.getName());
        eventNameEvent.setText(event.getName());

        //TODO: finish this
        //find all performances for the given event
        List<PerformanceDTO> performancesOfEvent = performanceService.findByEventID(event.getId());

        //create a list of all artists that take part of this event - every performance might have different artists
        List<ArtistDTO> allArtists = new ArrayList<>();

        for(PerformanceDTO p: performancesOfEvent){
            allArtists.addAll(p.getArtists());
        }

        //make sure it is a list of distinct values
        List<String> uniqueArtistList = new ArrayList<>();
        for(ArtistDTO a: allArtists){
            if(!uniqueArtistList.contains(a.getFirstName())){
                uniqueArtistList.add(a.getFirstName() + " " + a.getLastName() + ", ");
            }
        }

        artistNameEvent.setText(uniqueArtistList.toString());
        descriptionEvent.setText(event.getDescription());
        if (event.getEventType().toString().equals("SEATS")) {
            eventTypeEvent.setText("yes");
        } else {
            eventTypeEvent.setText("no");
        }
        try {
            performances = performanceService.findByEventID(event.getId());
        } catch (DataAccessException e) {
            LOGGER.error("Access error while loading performances of event!", e);
        }
        intializeTableView();
    }

    private void intializeTableView() {
        startTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPerformanceStart().toString()));
        endTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPerformanceEnd().toString()));
        locationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getCity()));
        performanceData = FXCollections.observableArrayList(performances);
        performanceDatesTableView.setItems(performanceData);
    }

}
