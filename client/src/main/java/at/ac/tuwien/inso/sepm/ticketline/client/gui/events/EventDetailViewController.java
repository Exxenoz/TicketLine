package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.PerformanceService;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventTypeDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static at.ac.tuwien.inso.sepm.ticketline.rest.event.EventTypeDTO.SEAT;

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

    private ObservableList<PerformanceDTO> performanceData = FXCollections.observableArrayList();
    private static final int PERFORMANCES_PER_PAGE = 10;

    private final SpringFxmlLoader fxmlLoader;
    private final PerformanceDetailViewController performanceDetailViewController;
    private final EventService eventService;
    private Stage stage;

    private PerformanceDTO chosenPerformance;

    public EventDetailViewController(
        SpringFxmlLoader fxmlLoader,

        @Lazy
            PerformanceDetailViewController performanceDetailViewController,
        EventService eventService
    ) {
        this.fxmlLoader = fxmlLoader;
        this.performanceDetailViewController = performanceDetailViewController;
        this.eventService = eventService;
    }

    public Button performanceButtonEvent;
    @FXML
    private Button bookButtonEvent;


    @FXML
    private void initialize() {

    }

    @FXML
    public void changeToPerformanceDetailView(ActionEvent event) {
        if (chosenPerformance == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("You need to choose a specific performance.");
            alert.showAndWait();
            return;
        }

        performanceDetailViewController.fill(chosenPerformance, stage);
        Parent parent = fxmlLoader.load("/fxml/events/performanceDetailView.fxml");
        stage.setScene(new Scene(parent));
        stage.setTitle("Performance Details");
        stage.centerOnScreen();
    }

    public void backButton(ActionEvent event) {
        Stage stage = (Stage) eventHeading.getScene().getWindow();
        stage.close();
    }

    public void fill(PerformanceService performanceService, EventDTO event, Stage stage) {
        this.stage = stage;
        eventHeading.setText(event.getName());
        eventNameEvent.setText(event.getName());

        //find all performances for the given event
        List<PerformanceDTO> performanceDTOs;
        performances = new ArrayList<>();
        try {
            PageRequestDTO pageRequestDTO = new PageRequestDTO();
            pageRequestDTO.setPage(0);
            pageRequestDTO.setSize(PERFORMANCES_PER_PAGE);
            PageResponseDTO<PerformanceDTO> performancesOfEvent;
            performancesOfEvent = performanceService.findByEventID(event.getId(), pageRequestDTO);
            performanceDTOs = performancesOfEvent.getContent();
        } catch (DataAccessException e) {
            LOGGER.error("Access error while loading performances of event!", e);
            return;
        }

        final var artistString = performanceDTOs.stream()
            .flatMap(performance -> performance.getArtists().stream())
            .map(artist -> artist.getFirstName() + " " + artist.getLastName())
            .distinct()
            .collect(Collectors.joining(", "));

        artistNameEvent.setText(artistString);
        descriptionEvent.setText(event.getDescription());
        eventTypeEvent.setText(event.getEventType() == SEAT ? "yes" : "no");

        performances.addAll(performanceDTOs);
        intializeTableView();
    }

    private void intializeTableView() {
        startTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPerformanceStart().toString()));

        endTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDuration().toString()));
        locationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocationAddress().getCity()));
        performanceData = FXCollections.observableArrayList(performances);
        performanceDatesTableView.setItems(performanceData);

        performanceDatesTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                chosenPerformance = newValue;
            }
        });
    }

}
