package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;

public class EventsController {

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
    private Spinner<?> eventLengthSpinner;

    @FXML
    private RadioButton seatingYesButton;

    @FXML
    private ToggleGroup seatingToggleGroup;

    @FXML
    private RadioButton seatingNoButton;

    @FXML
    private DatePicker beginTimeDatePicker;

    @FXML
    private Spinner<?> beginTimeHourSpinner;

    @FXML
    private Spinner<?> beginTimeMinuteSpinner;

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
    private TableView<?> foundEventsTableView;

    @FXML
    private Label activeFiltersListLabel;

    @FXML
    private Button clearButton;

    @FXML
    private Button bookButton;

    @FXML
    private Button searchButton;

    @FXML
    private ChoiceBox<?> monthChoiceBox;

    @FXML
    private Button showTopTenButton;

    @FXML
    private BarChart<?, ?> topTenBarChart;

    @FXML
    private ChoiceBox<?> topTenEventChoiceBox;

    @FXML
    private Button bookTopTenEventButton;


    private void initialize() {

    }

    @FXML
    void bookPerformance(ActionEvent event) {

    }

    @FXML
    void bookTopTenEvent(ActionEvent event) {

    }

    @FXML
    void searchForPerformances(ActionEvent event) {

    }

    @FXML
    void showAllPerformances(ActionEvent event) {

    }

    @FXML
    void showTopTenChartForMonth(ActionEvent event) {

    }


}
