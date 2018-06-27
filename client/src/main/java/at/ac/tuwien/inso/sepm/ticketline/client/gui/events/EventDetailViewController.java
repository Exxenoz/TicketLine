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
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    private static final int PERFORMANCES_PER_PAGE = 10;

    private final SpringFxmlLoader fxmlLoader;
    private final PerformanceDetailViewController performanceDetailViewController;
    private final EventService eventService;
    private final PerformanceService performanceService;
    private Stage stage;
    private int currentPage;
    private int totalPages;

    private PerformanceDTO chosenPerformance;

    private TableColumn sortedColumn;

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
        if (chosenPerformance == null) {
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
        Stage stage = (Stage) eventHeading.getScene().getWindow();
        stage.close();
    }

    public void fill(EventDTO event, Stage stage) {
        this.stage = stage;
        eventHeading.setText(event.getName());
        eventNameEvent.setText(event.getName());
        descriptionText.setText(event.getDescription());
        eventTypeEvent.setText(event.getEventType() == SEAT ? BundleManager.getBundle().getString("events.eventDetailView.seatingOption.yes") : BundleManager.getBundle().getString("events.eventDetailView.seatingOption.no"));

        //find first page of performances for the given event
        loadData();
    }

    private void loadPerformanceTable(int page) {
        LOGGER.debug("Loading Performances of page {}", page);
        PageRequestDTO pageRequestDTO = null;
        if (sortedColumn != null) {
            Sort.Direction sortDirection =
                (sortedColumn.getSortType() == TableColumn.SortType.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC;
            pageRequestDTO = new PageRequestDTO(page, PERFORMANCES_PER_PAGE, sortDirection, getColumnNameBy(sortedColumn));
        } else {
            pageRequestDTO = new PageRequestDTO(page, PERFORMANCES_PER_PAGE, Sort.Direction.ASC, null);
        }

        try {
            PageResponseDTO<PerformanceDTO> response = performanceService.findAll(pageRequestDTO);
            performanceData.addAll(response.getContent());
            totalPages = response.getTotalPages();
            performanceDatesTableView.refresh();

        } catch (DataAccessException e) {
            LOGGER.warn("Could not access performances!");
        }

        final var artistString = performanceData.stream()
            .flatMap(performance -> performance.getArtists().stream())
            .map(artist -> artist.getFirstName() + " " + artist.getLastName())
            .distinct()
            .collect(Collectors.joining("\n"));
        //.collect(Collectors.joining(", "));

        artistNameEvent.setText(artistString);
    }

    private String getColumnNameBy(TableColumn<PerformanceDTO, ?> sortedColumn) {
        if (sortedColumn == nameColumn) {
            return "name";
        } else if (sortedColumn == locationColumn) {
            return "locationAddress.country";
        } else if (sortedColumn == startTimeColumn) {
            return "performanceStart";
        } else if (sortedColumn == endTimeColumn) {
            return "duration";//needs to be switched with an actual endtime property
        }
        return "id";
    }

    private void loadData() {
        Platform.runLater(() -> {
            final ScrollBar scrollBar = getVerticalScrollbar(performanceDatesTableView);
            if (scrollBar != null) {
                scrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
                    double value = newValue.doubleValue();
                    if ((value >= scrollBar.getMax()) && (currentPage + 1 < totalPages)) {
                        currentPage++;
                        LOGGER.debug("Getting next Page: {}", currentPage);
                        double targetValue = value * performanceData.size();
                        loadPerformanceTable(currentPage);
                        scrollBar.setValue(targetValue / performanceData.size());
                    }
                });

                scrollBar.visibleProperty().addListener((ObservableValue<? extends Boolean> observable,
                                                         Boolean oldValue, Boolean newValue) -> {
                    if (newValue == false) {
                        // Scrollbar is invisible, load next page
                        currentPage++;
                        loadPerformanceTable(currentPage);
                    }
                });
            }
        });

        ChangeListener<TableColumn.SortType> tableColumnSortChangeListener = (observable, oldValue, newValue) -> {
            if (newValue != null) {
                var property = (ObjectProperty<TableColumn.SortType>) observable;
                sortedColumn = (TableColumn) property.getBean();
                for (TableColumn tableColumn : performanceDatesTableView.getColumns()) {
                    if (tableColumn != sortedColumn) {
                        tableColumn.setSortType(null);
                    }
                }

                clear();
                loadPerformanceTable(0);
            }
        };

        for (TableColumn tableColumn : performanceDatesTableView.getColumns()) {
            tableColumn.setSortType(null);
        }
        nameColumn.sortTypeProperty().addListener(tableColumnSortChangeListener);
        startTimeColumn.sortTypeProperty().addListener(tableColumnSortChangeListener);
        endTimeColumn.sortTypeProperty().addListener(tableColumnSortChangeListener);
        locationColumn.sortTypeProperty().addListener(tableColumnSortChangeListener);

        loadPerformanceTable(0);
    }

    private void clear() {
        LOGGER.debug("clearing the data");
        performanceData.clear();
        currentPage = 0;
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
        LOGGER.debug("loading the page into the table");
        performanceDatesTableView.setItems(performanceData);

        performanceDatesTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                chosenPerformance = newValue;
            }
        });
    }

}
