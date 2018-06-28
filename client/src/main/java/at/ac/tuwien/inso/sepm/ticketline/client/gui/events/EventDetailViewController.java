package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.PerformanceService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import static at.ac.tuwien.inso.sepm.ticketline.rest.event.EventTypeDTO.SEAT;

@Component
public class EventDetailViewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public TableColumn<PerformanceDTO, String> nameColumn;

    @FXML
    public Text descriptionText;

    @FXML
    private Label artistNameEvent;

    @FXML
    private Label eventHeading;

    @FXML
    private Label eventNameEvent;

    @FXML
    private Label eventTypeEvent;

    @FXML
    private Button backButton;

    @FXML
    private Button performanceDetailButton;

    @FXML
    private TableView<PerformanceDTO> performanceDatesTableView;

    @FXML
    private TableColumn<PerformanceDTO, String> startTimeColumn;

    @FXML
    private TableColumn<PerformanceDTO, String> endTimeColumn;

    @FXML
    private TableColumn<PerformanceDTO, String> locationColumn;

    private ObservableList<PerformanceDTO> performanceData = FXCollections.observableArrayList();

    private final SpringFxmlLoader fxmlLoader;
    private final PerformanceDetailViewController performanceDetailViewController;
    private final EventService eventService;
    private final PerformanceService performanceService;
    private Stage stage;

    private PerformanceDTO chosenPerformance;

    public EventDetailViewController(
        SpringFxmlLoader fxmlLoader,

        @Lazy
            PerformanceDetailViewController performanceDetailViewController,
        EventService eventService,
        PerformanceService performanceService
    ) {
        this.fxmlLoader = fxmlLoader;
        this.performanceDetailViewController = performanceDetailViewController;
        this.eventService = eventService;
        this.performanceService = performanceService;
    }

    @FXML
    private void initialize() {
        LOGGER.info("Initialize EventDetailViewController");
        initializeTableView();
        ButtonBar.setButtonUniformSize(backButton, false);
        ButtonBar.setButtonUniformSize(performanceDetailButton, false);
    }

    private ScrollBar getVerticalScrollbar(TableView<?> table) {
        ScrollBar result = null;

        for (Node n : table.lookupAll(".scroll-bar")) {
            if (n instanceof ScrollBar) {
                ScrollBar bar = (ScrollBar) n;
                if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                    result = bar;
                }
            }
        }

        return result;
    }

    @FXML
    public void changeToPerformanceDetailView(ActionEvent event) {
        LOGGER.info("User clicked the show performanceDetailView button with {}", chosenPerformance);
        if (chosenPerformance == null) {
            LOGGER.warn("No performance was chosen by the user");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(BundleManager.getExceptionBundle().getString("exception.event_details.no_selection"));
            alert.showAndWait();
            return;
        }

        performanceDetailViewController.fill(chosenPerformance, stage);
        Parent parent = fxmlLoader.load("/fxml/events/performanceDetailView.fxml");
        stage.setScene(new Scene(parent));
        stage.setTitle(BundleManager.getBundle().getString("bookings.performance.details.title"));
        stage.centerOnScreen();
    }

    public void backButton(ActionEvent event) {
        LOGGER.info("User clicked the back button");
        Stage stage = (Stage) eventHeading.getScene().getWindow();
        stage.close();
    }

    public void fill(EventDTO event, Stage stage) {
        this.stage = stage;
        eventHeading.setText(event.getName());
        eventNameEvent.setText(event.getName());
        descriptionText.setText(event.getDescription());
        eventTypeEvent.setText(event.getEventType() == SEAT ? BundleManager.getBundle().getString("events.eventDetailView.seatingOption.yes") : BundleManager.getBundle().getString("events.eventDetailView.seatingOption.no"));

        //find performances for the given event
        loadData();
    }

    private void loadData() {
        LOGGER.debug("Loading Performances");
        PageRequestDTO pageRequestDTO = new PageRequestDTO(0, Integer.MAX_VALUE, Sort.Direction.ASC, null);

        try {
            PageResponseDTO<PerformanceDTO> response = performanceService.findAll(pageRequestDTO);
            performanceData.addAll(response.getContent());
            performanceDatesTableView.refresh();

        } catch (DataAccessException e) {
            LOGGER.error("Could not access performances!");
        }

        final var artistString = performanceData.stream()
            .flatMap(performance -> performance.getArtists().stream())
            .map(artist -> artist.getFirstName() + " " + artist.getLastName())
            .distinct()
            .collect(Collectors.joining("\n"));
        //.collect(Collectors.joining(", "));

        artistNameEvent.setText(artistString);
    }

    private void initializeTableView() {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
        startTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPerformanceStart().format(formatter)));
        endTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPerformanceStart()
            .plusMinutes(cellData.getValue().getDuration().toMinutes()).format(formatter)));
        locationColumn.setCellValueFactory(cellData -> new SimpleStringProperty( cellData.getValue().getLocationAddress().getCountry() + ", " +
            cellData.getValue().getLocationAddress().getCity()));

        startTimeColumn.setComparator((d1, d2) -> {
            LocalDateTime date1 = LocalDateTime.parse(d1, formatter);
            LocalDateTime date2 = LocalDateTime.parse(d2, formatter);
            return date1.compareTo(date2);
        });

        endTimeColumn.setComparator((d1, d2) -> {
            LocalDateTime date1 = LocalDateTime.parse(d1, formatter);
            LocalDateTime date2 = LocalDateTime.parse(d2, formatter);
            return date1.compareTo(date2);
        });

        performanceDatesTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        LOGGER.debug("loading events into the table");
        performanceDatesTableView.setItems(performanceData);

        performanceDatesTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                chosenPerformance = newValue;
            }
        });
    }
}
