package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.PerformanceService;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventTypeDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.SearchDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.skin.TableViewSkin;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class EventSearchController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // ---------- Show all and search tab -----------

    @FXML
    private TabPane eventTabPane;

    @FXML
    private TextField artistFirstNameTextField;

    @FXML
    private TextField artistLastNameTextField;

    @FXML
    private TextField eventNameTextField;

    @FXML
    private TextField lengthInMinutesTextField;

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
    private TextField countryTextField;

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
    private TableColumn<PerformanceDTO, String> nameColumn;

    @FXML
    private TableColumn<PerformanceDTO, String> eventColumn;

    @FXML
    private TableColumn<PerformanceDTO, String> startTimeColumn;

    @FXML
    private TableColumn<PerformanceDTO, String> locationColumn;

    private final SpringFxmlLoader fxmlLoader;
    private final PerformanceService performanceService;
    private final PerformanceDetailViewController performanceDetailViewController;

    private List<PerformanceDTO> performances;

    private String activeFilters = "";


    public EventSearchController(SpringFxmlLoader fxmlLoader, PerformanceService performanceService, PerformanceDetailViewController performanceDetailViewController) {
        this.fxmlLoader = fxmlLoader;
        this.performanceService = performanceService;
        this.performanceDetailViewController = performanceDetailViewController;
    }

    @FXML
    private void initialize() {

        SpinnerValueFactory<Integer> beginTimeHoursFactory = buildSpinner(23);
        SpinnerValueFactory<Integer> beginTimeMinutesFactory = buildSpinner(59);
        seatingYesButton.setSelected(false);
        seatingNoButton.setSelected(false);

        beginTimeHoursFactory.setValue(0);
        beginTimeMinutesFactory.setValue(0);

        beginTimeHourSpinner.setValueFactory(beginTimeHoursFactory);
        beginTimeMinuteSpinner.setValueFactory(beginTimeMinutesFactory);
    }

    public void loadData() {
        try {
            performances = performanceService.findAll();
            intializeTableView();
        } catch (DataAccessException e) {
            LOGGER.error("Couldn't fetch performances from server!", e);
        }

    }

    private void intializeTableView() {

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        eventColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvent().getName()));
        startTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPerformanceStart().toString()));
        locationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getCity()));

        ObservableList<PerformanceDTO> performanceData = FXCollections.observableArrayList(performances);
        foundEventsTableView.setItems(performanceData);
    }

    private SpinnerValueFactory<Integer> buildSpinner(int maxValue) {
        return new SpinnerValueFactory<>() {
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
    private void bookPerformance(ActionEvent event) {
        Stage stage = new Stage();
        int row = foundEventsTableView.getSelectionModel().getFocusedIndex();
        performanceDetailViewController.fill(performances.get(row), stage);

        final var parent = fxmlLoader.<Parent>load("/fxml/events/performanceDetailView.fxml");

        stage.setScene(new Scene(parent));
        stage.setTitle("Performance Details");

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(bookButton.getScene().getWindow());

        stage.showAndWait();
    }

    @FXML
    void searchForPerformances(ActionEvent event) {
        String artistFirstName = artistFirstNameTextField.getText();
        addToCurrentSearchParameters(artistFirstName);
        String artistLastName = artistLastNameTextField.getText();
        addToCurrentSearchParameters(artistLastName);

        String eventName = eventNameTextField.getText();
        addToCurrentSearchParameters(eventName);
        EventTypeDTO eventType = null;
        if (seatingYesButton.isSelected()) {
            eventType = EventTypeDTO.SEAT;
            addToCurrentSearchParameters(eventType.toString());
        } else if (seatingNoButton.isSelected()) {
            eventType = EventTypeDTO.SECTOR;
            addToCurrentSearchParameters(eventType.toString());
        }
        String durationString = lengthInMinutesTextField.getText();
        Duration duration = null;
        if (!durationString.equals("")) {
            addToCurrentSearchParameters(durationString + " min");
            duration = Duration.ofMinutes(Integer.parseInt(durationString));
        }

        LocalDate beginDate = beginTimeDatePicker.getValue();
        LocalDateTime beginDateAndTime = null;
        Integer beginTimeHours = null;
        Integer beginTimeMinutes = null;
        if (beginDate != null) {
            beginTimeHours = beginTimeHourSpinner.getValue();
            beginTimeMinutes = beginTimeMinuteSpinner.getValue();
            beginDateAndTime = LocalDateTime.of(beginDate, LocalTime.of(beginTimeHours, beginTimeMinutes));
            addToCurrentSearchParameters(beginDateAndTime.toString());
        }

        String priceString = priceTextField.getText();
        BigDecimal price = null;
        if (!priceString.equals("")) {
            addToCurrentSearchParameters(priceString);
            price = new BigDecimal(priceString);
        }

        String locationName = locationNameTextField.getText();
        addToCurrentSearchParameters(locationName);
        String street = streetTextField.getText();
        addToCurrentSearchParameters(street);
        String city = cityTextField.getText();
        addToCurrentSearchParameters(city);
        String country = countryTextField.getText();
        addToCurrentSearchParameters(country);
        String postalCode = postalCodeTextField.getText();
        addToCurrentSearchParameters(postalCode);


        //String performanceName, String eventName, String artistFirstName,
        //                     String artistLastName, EventTypeDTO eventType, LocalDateTime performanceStart,
        //                     Double price, String locationName, String street, String city, String country,
        //                     String postalCode, Duration duration
        SearchDTO searchParameters = new SearchDTO(null, eventName, artistFirstName, artistLastName, eventType, beginDateAndTime, price, locationName, street, city, country, postalCode, duration);

        try {
            performances = performanceService.search(searchParameters);
            intializeTableView();
            foundEventsTableView.refresh();
            updateCurrentSearchParameters();
        } catch (DataAccessException e) {
            LOGGER.error("Search failed!", e);
        }

    }

    private void addToCurrentSearchParameters(String searchParameter) {
        if (searchParameter != null && !searchParameter.equals("")) {
            activeFilters += searchParameter + ", ";
        }
    }

    private void updateCurrentSearchParameters() {
        activeFiltersListLabel.setText(activeFilters);
    }

    //when clear button is pushed
    @FXML
    private void showAllPerformances(ActionEvent event) {
        try {
            performances = performanceService.findAll();
            intializeTableView();
            foundEventsTableView.refresh();
            activeFilters = "";
            updateCurrentSearchParameters();

            artistFirstNameTextField.setText("");
            artistLastNameTextField.setText("");
            eventNameTextField.setText("");
            seatingYesButton.setSelected(false);
            seatingNoButton.setSelected(false);
            lengthInMinutesTextField.setText("");
            beginTimeDatePicker.setValue(null);
            beginTimeHourSpinner.getValueFactory().setValue(0);
            beginTimeMinuteSpinner.getValueFactory().setValue(0);
            priceTextField.setText("");
            locationNameTextField.setText("");
            streetTextField.setText("");
            cityTextField.setText("");
            countryTextField.setText("");
            postalCodeTextField.setText("");

        } catch (DataAccessException e) {
            LOGGER.error("Couldn't fetch performances from server!", e);
        }

    }


}