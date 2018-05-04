package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
        SpinnerValueFactory<Integer> eventLengthFactory = buildSpinner(480);
        SpinnerValueFactory<Integer> beginTimeHoursFactory = buildSpinner(23);
        SpinnerValueFactory<Integer> beginTimeMinutesFactory = buildSpinner(59);

       /* eventLengthFactory.setValue(0);
        beginTimeHoursFactory.setValue(0);
        beginTimeMinutesFactory.setValue(0); */

       eventLengthSpinner.setValueFactory(eventLengthFactory);
       beginTimeHourSpinner.setValueFactory(beginTimeHoursFactory);
       beginTimeMinuteSpinner.setValueFactory(beginTimeMinutesFactory);

    }

    private SpinnerValueFactory<Integer> buildSpinner(int maxValue){
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
        String artistFirstName =  artistFirstNameTextField.getText();
        String artistLastName = artistLastNameTextField.getText();
        String eventName = eventNameTextField.getText();
        String eventDescription = eventDescriptionTextField.getText();
        LocalDate beginDate = beginTimeDatePicker.getValue();
        LocalDateTime beginDateAndTime = null;
        Integer beginTimeHours = null;
        Integer beginTimeMinutes = null;
        if(beginDate != null) {
            beginTimeHours = beginTimeHourSpinner.getValue();
            beginTimeMinutes = beginTimeMinuteSpinner.getValue();
            beginDateAndTime = LocalDateTime.of(beginDate,LocalTime.of(beginTimeHours,beginTimeMinutes));
        }
        String price = priceTextField.getText();
        String locationName = locationNameTextField.getText();
        String street = streetTextField.getText();
        String city = cityTextField.getText();
        String postalCode = postalCodeTextField.getText();



    }

    @FXML
    void showAllPerformances(ActionEvent event) {

    }

    @FXML
    void showTopTenChartForMonth(ActionEvent event) {

    }


}
