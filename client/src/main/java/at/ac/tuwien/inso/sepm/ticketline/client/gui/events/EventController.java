package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.PerformanceService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventFilterTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventTypeDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class EventController {

    @FXML
    private TabPane eventTabPane;

    @FXML
    private TextField artistFirstNameTextField;

    @FXML
    private TextField artistLastNameTextField;

    @FXML
    private TextField eventNameTextField;

    @FXML
    private TextField eventDescriptionTextField;

    @FXML
    private Spinner<Integer> eventLengthSpinner;

    @FXML
    private RadioButton seatingYesButton;

    @FXML
    private ToggleGroup seatingToggleGroup;

    @FXML
    private RadioButton seatingNoButton;

    @FXML
    private DatePicker beginTimeDatePicker;

    @FXML
    private Spinner<Integer> beginTimeHourSpinner;

    @FXML
    private Spinner<Integer> beginTimeMinuteSpinner;

    @FXML
    private TextField priceTextField;

    @FXML
    private TextField locationNameTextField;

    @FXML
    private TextField streetTextField;

    @FXML
    private TextField cityTextField;

    @FXML
    private TextField postalCodeTextField;

    @FXML
    private TableView<PerformanceDTO> foundEventsTableView;

    @FXML
    private Label activeFiltersListLabel;

    @FXML
    private Button clearButton;

    @FXML
    private Button bookButton;

    @FXML
    private Button searchButton;

    @FXML
    private ChoiceBox<String> monthChoiceBox;

    @FXML
    public ChoiceBox categoryChoiceBox;

    @FXML
    private Button showTopTenButton;

    @FXML
    private BarChart<String, Integer> topTenBarChart;


    @FXML
    private TableColumn<PerformanceDTO, String> nameColumn;

    @FXML
    private TableColumn<PerformanceDTO, String> eventColumn;

    @FXML
    private TableColumn<PerformanceDTO, String> startTimeColumn;

    @FXML
    private TableColumn<PerformanceDTO, String> locationColumn;

    @FXML
    private ChoiceBox<?> topTenEventChoiceBox;

    @FXML
    private Button bookTopTenEventButton;

    private EventService eventService;

    private PerformanceService performanceService;

    private ObservableList<PerformanceDTO> performanceData = FXCollections.observableArrayList();

    private List<PerformanceDTO> performances;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public EventController(EventService eventService, PerformanceService performanceService) {
        this.eventService = eventService;
        this.performanceService = performanceService;
    }


    @FXML
    private void initialize() {
        SpinnerValueFactory<Integer> eventLengthFactory = buildSpinner(480);
        SpinnerValueFactory<Integer> beginTimeHoursFactory = buildSpinner(23);
        SpinnerValueFactory<Integer> beginTimeMinutesFactory = buildSpinner(59);

        eventLengthFactory.setValue(0);
        beginTimeHoursFactory.setValue(0);
        beginTimeMinutesFactory.setValue(0);

        eventLengthSpinner.setValueFactory(eventLengthFactory);
        beginTimeHourSpinner.setValueFactory(beginTimeHoursFactory);
        beginTimeMinuteSpinner.setValueFactory(beginTimeMinutesFactory);

        initMonthChoiceBox();

        try {
            performances = performanceService.findAll();
            intializeTableView();
        } catch (DataAccessException e) {
            LOGGER.error("Couldn't fetch performances from server!", e);
        }
    }

    private void initMonthChoiceBox() {
        monthChoiceBox.getItems().setAll(
            BundleManager.getBundle().getString("events.main.january"),
            BundleManager.getBundle().getString("events.main.february"),
            BundleManager.getBundle().getString("events.main.march"),
            BundleManager.getBundle().getString("events.main.april"),
            BundleManager.getBundle().getString("events.main.may"),
            BundleManager.getBundle().getString("events.main.june"),
            BundleManager.getBundle().getString("events.main.july"),
            BundleManager.getBundle().getString("events.main.august"),
            BundleManager.getBundle().getString("events.main.september"),
            BundleManager.getBundle().getString("events.main.october"),
            BundleManager.getBundle().getString("events.main.november"),
            BundleManager.getBundle().getString("events.main.december"));
        monthChoiceBox.getSelectionModel().selectFirst();
    }

    private SpinnerValueFactory<Integer> buildSpinner(int maxValue) {
        return new SpinnerValueFactory<Integer>() {
            @Override
            public void decrement(int steps) {
                Integer current = this.getValue();
                if (current == 0) {
                    this.setValue(maxValue);
                } else {
                    this.setValue(current - 1);
                }
            }

            @Override
            public void increment(int steps) {
                Integer current = this.getValue();
                if (current == maxValue) {
                    this.setValue(0);
                } else {
                    this.setValue(current + 1);
                }
            }
        };
    }

    @FXML
    void bookPerformance(ActionEvent event) {

    }

    @FXML
    void bookTopTenEvent(ActionEvent event) {

    }

    @FXML
    void searchForPerformances(ActionEvent event) {
        String artistFirstName = artistFirstNameTextField.getText();
        String artistLastName = artistLastNameTextField.getText();
        String eventName = eventNameTextField.getText();
        String eventDescription = eventDescriptionTextField.getText();
        Integer duration = eventLengthSpinner.getValue();
        LocalDate beginDate = beginTimeDatePicker.getValue();
        LocalDateTime beginDateAndTime = null;
        Integer beginTimeHours = null;
        Integer beginTimeMinutes = null;
        if (beginDate != null) {
            beginTimeHours = beginTimeHourSpinner.getValue();
            beginTimeMinutes = beginTimeMinuteSpinner.getValue();
            beginDateAndTime = LocalDateTime.of(beginDate, LocalTime.of(beginTimeHours, beginTimeMinutes));
        }
        String price = priceTextField.getText();
        String locationName = locationNameTextField.getText();
        String street = streetTextField.getText();
        String city = cityTextField.getText();
        String postalCode = postalCodeTextField.getText();

    }

    @FXML
    void showAllPerformances(ActionEvent event) {
        try {
            performances = performanceService.findAll();
        } catch (DataAccessException e) {
            LOGGER.error("Couldn't fetch performances from server!", e);
        }
        intializeTableView();

    }

    private void intializeTableView() {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        eventColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvent().getName()));
        startTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPerformanceStart().toString()));
        locationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getLocation()));

        performanceData = FXCollections.observableArrayList(performances);
        foundEventsTableView.setItems(performanceData);
    }

    @FXML
    void showTopTenClicked(ActionEvent event) {
        Integer month = monthChoiceBox.getSelectionModel().getSelectedIndex() > 0 ? monthChoiceBox.getSelectionModel().getSelectedIndex() + 1 : 1;
        Integer category = categoryChoiceBox.getSelectionModel().getSelectedIndex() >= 0 ? categoryChoiceBox.getSelectionModel().getSelectedIndex() : null;

        try {
            List<EventDTO> events = eventService.findTop10ByPaidReservationCountByMonthAndCategory(new EventFilterTopTenDTO(month, category));
            showTopTenEvents(events);
        } catch (DataAccessException e) {
            LOGGER.error("Couldn't fetch top 10 events from server for month: " + month + " " + e.getMessage());
            JavaFXUtils.createErrorDialog(e.getMessage(), monthChoiceBox.getScene().getWindow()).showAndWait();
        }
    }

    private void showTopTenEvents(List<EventDTO> events) {
        XYChart.Series barSeries = new XYChart.Series();

        for(EventDTO event : events) {
            barSeries.getData().add(new XYChart.Data(event.getName(), 10));
        }

        topTenBarChart.getData().add(barSeries);
    }
}
