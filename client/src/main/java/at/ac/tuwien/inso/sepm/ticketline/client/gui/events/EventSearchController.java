package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.AddressValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.PerformanceSearchValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.PerformanceService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventTypeDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.SearchDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;
import static org.controlsfx.glyphfont.FontAwesome.Glyph.CALENDAR_ALT;

import static at.ac.tuwien.inso.sepm.ticketline.client.validator.PerformanceSearchValidator.*;
import static at.ac.tuwien.inso.sepm.ticketline.client.validator.LocationAddressValidator.*;

@Component
public class EventSearchController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    public Label locationnameErrorLabel;
    public Label streetErrorLabel;
    public Label cityErrorLabel;
    public Label postalcodeErrorLabel;
    public Label artistfirstnameErrorLabel;
    public Label artistlastnameErrorLabel;
    public Label eventnameErrorLabel;
    public Label eventdurationErrorLabel;
    public Label starttimeErrorLabel;
    public Label priceErrorLabel;
    public Label countryErrorLabel;
    public FlowPane flowpane;
    public TitledPane artistTitledPane;
    public TitledPane eventsTitledPane;
    public TitledPane timeTitledPane;
    public TitledPane cityTitledPane;

    // ---------- Show all and search tab -----------

    @FXML
    private TabHeaderController tabHeaderController;
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
    private static final int PERFORMANCES_PER_PAGE = 50;

    private ObservableList<PerformanceDTO> performanceData = observableArrayList();
    private int page = 0;
    private int totalPages = 0;
    private ArrayList<Text> textChunks = new ArrayList<Text>();
    private TableColumn sortedColumn;
    private enum Validate { artistFristName, artistLastName, eventName, duration, price, locationName, city, street, country, postalcode }

    public EventSearchController(SpringFxmlLoader fxmlLoader, PerformanceService performanceService, PerformanceDetailViewController performanceDetailViewController) {
        this.fxmlLoader = fxmlLoader;
        this.performanceService = performanceService;
        this.performanceDetailViewController = performanceDetailViewController;
    }


    //+++++++++++INITIALIZE+++++++++++++++
    public void initialize() {
        SpinnerValueFactory<Integer> beginTimeHoursFactory = buildSpinner(23);
        SpinnerValueFactory<Integer> beginTimeMinutesFactory = buildSpinner(59);
        seatingYesButton.setSelected(false);
        seatingNoButton.setSelected(false);

        beginTimeHoursFactory.setValue(0);
        beginTimeMinutesFactory.setValue(0);

        beginTimeHourSpinner.setValueFactory(beginTimeHoursFactory);
        beginTimeMinuteSpinner.setValueFactory(beginTimeMinutesFactory);

        tabHeaderController.setIcon(CALENDAR_ALT);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("bookings.table.event"));
        initializeTableView();


    }

    private void initializeTableView() {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getName()));
        eventColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getEvent().getName()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
        startTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getPerformanceStart().format(formatter)));
        locationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocationAddress().getCountry() + ", " +
            cellData.getValue().getLocationAddress().getCity()));

        startTimeColumn.setComparator((d1, d2) -> {
            LocalDateTime date1 = LocalDateTime.parse(d1, formatter);
            LocalDateTime date2 = LocalDateTime.parse(d2, formatter);
            return date1.compareTo(date2);
        });

        foundEventsTableView.setItems(performanceData);

    }

    //+++++++++++++++++LOAD DATA+++++++++++++++
    public void loadData() {
        final ScrollBar scrollBar = getVerticalScrollbar(foundEventsTableView);
        if (scrollBar != null) {
            scrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
                double value = newValue.doubleValue();
                if ((value == scrollBar.getMax()) && (page + 1 < totalPages)) {
                    page++;
                    double targetValue = value * performanceData.size();
                    LOGGER.debug("VAL: " + (targetValue / performanceData.size()));
                    loadPerformanceTable(page);
                    scrollBar.setValue(targetValue / performanceData.size());
                }
            });

            scrollBar.visibleProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (newValue == false) {
                    // Scrollbar is invisible, load next page
                    page++;
                    loadPerformanceTable(page);
                }
            });
        }

        ChangeListener<TableColumn.SortType> tableColumnSortChangeListener = (observable, oldValue, newValue) -> {
            if (newValue != null) {
                var property = (ObjectProperty<TableColumn.SortType>) observable;
                sortedColumn = (TableColumn) property.getBean();
                for (TableColumn tableColumn : foundEventsTableView.getColumns()) {
                    if (tableColumn != sortedColumn) {
                        tableColumn.setSortType(null);
                    }
                }
                clear();
                loadPerformanceTable(0);
            }
        };

        for (TableColumn tableColumn : foundEventsTableView.getColumns()) {
            tableColumn.setSortType(null);
        }
        nameColumn.sortTypeProperty().addListener(tableColumnSortChangeListener);
        eventColumn.sortTypeProperty().addListener(tableColumnSortChangeListener);
        startTimeColumn.sortTypeProperty().addListener(tableColumnSortChangeListener);
        locationColumn.sortTypeProperty().addListener(tableColumnSortChangeListener);

        loadPerformanceTable(0);
    }

    private void clear() {
        LOGGER.debug("clearing the data");
        performanceData.clear();
        page = 0;
        totalPages = 0;
        ScrollBar scrollBar = getVerticalScrollbar(foundEventsTableView);
        if (scrollBar != null) {
            scrollBar.setValue(0);
        }
    }

    private void loadPerformanceTable(int page) {
        PageRequestDTO pageRequestDTO = null;

        if (sortedColumn != null) {
            Sort.Direction sortDirection = (sortedColumn.getSortType() == TableColumn.SortType.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC;
            pageRequestDTO = new PageRequestDTO(page, PERFORMANCES_PER_PAGE, sortDirection, getColumnNameBy(sortedColumn));
        } else {
            pageRequestDTO = new PageRequestDTO(page, PERFORMANCES_PER_PAGE, Sort.Direction.ASC, null);
        }

        try {
            PageResponseDTO<PerformanceDTO> responseDTO = performanceService.findAll(pageRequestDTO);
            performanceData.addAll(responseDTO.getContent());
            totalPages = responseDTO.getTotalPages();
            foundEventsTableView.refresh();
        } catch (DataAccessException e) {
            LOGGER.error("Couldn't fetch performance from server!");
        }
    }


    private ScrollBar getVerticalScrollbar(TableView<?> table){
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

    private String getColumnNameBy(TableColumn<PerformanceDTO, ?> sortedColumn) {
        if (sortedColumn == nameColumn) {
            return "name";
        } else if (sortedColumn == eventColumn) {
            return "event.name";
        } else if (sortedColumn == startTimeColumn) {
            return "performanceStart";
        } else if (sortedColumn == locationColumn) {
            return "locationAddress.country";
        }
        return "id";
    }

    //++++++++++++++BUTTONS+++++++++++++++
    @FXML
    private void bookPerformanceButton(ActionEvent event) {
        Stage stage = new Stage();
        int row = foundEventsTableView.getSelectionModel().getFocusedIndex();
        performanceDetailViewController.fill(performanceData.get(row), stage);

        final var parent = fxmlLoader.<Parent>load("/fxml/events/performanceDetailView.fxml");

        stage.setScene(new Scene(parent));
        stage.setTitle(BundleManager.getBundle().getString("bookings.performance.details.title"));


        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(bookButton.getScene().getWindow());

        stage.showAndWait();
    }

    String setActiveFiltersAndValidate(Validate toValidate,
                                       TextField currentTextField,
                                       String bundleName,
                                       TitledPane currentTitledPane,
                                       Label errorLabel)
        throws PerformanceSearchValidationException, AddressValidationException {
        ResourceBundle labels = BundleManager.getBundle();
        String returnString = null;
        Text currentTextChunk = null;

        try {
            if (currentTextField.getText() != null && !currentTextField.getText().equals("")) {
                returnString = currentTextField.getText();
                String limitForActiveFiltersString = returnString.substring(0,50);
                currentTextChunk = new Text(labels.getString(bundleName) + " " + limitForActiveFiltersString + " ");
                textChunks.add(currentTextChunk);

                switch (toValidate){
                    case artistFristName:
                        returnString = validateArtistFirstName(currentTextField);
                        break;
                    case artistLastName:
                        returnString = validateArtistLastName(currentTextField);
                        break;
                    case eventName:
                        returnString = validateEventName(currentTextField);
                        break;
                    case duration:
                        returnString = validateDuration(currentTextField);
                        break;
                    case price:
                        returnString = validatePrice(currentTextField);
                        break;
                    case locationName:
                        returnString = validateLocationName(currentTextField);
                        break;
                    case street:
                        returnString = validateStreet(currentTextField);
                        break;
                    case city:
                        returnString = validateCity(currentTextField);
                        break;
                    case country:
                        returnString = validateCountry(currentTextField);
                        break;
                    case postalcode:
                        returnString = validatePostalCode(currentTextField);
                        break;
                }
            }
        } catch (PerformanceSearchValidationException | AddressValidationException e) {
            LOGGER.error("Error with artist first name value: ", e.getMessage());
            errorLabel.setText(e.getMessage());
            int index = textChunks.indexOf(currentTextChunk);
            textChunks.get(index).setFill(Paint.valueOf("red"));
            currentTitledPane.setTextFill(Paint.valueOf("red"));
        }

        return returnString;
    }

    @FXML
    void searchForPerformancesButton(ActionEvent event) throws PerformanceSearchValidationException, AddressValidationException {
        clearErrorLabels();
        textChunks = new ArrayList<>();
        updateCurrentFlowPane();
        ResourceBundle labels = BundleManager.getBundle();

        //+++++++++++++++++++++++ARTIST++++++++++++++++++++

        String artistFirstNameBundle = "events.search.artistfirstname";
        String artistFirstName = setActiveFiltersAndValidate(Validate.artistFristName, artistFirstNameTextField, artistFirstNameBundle, artistTitledPane, artistfirstnameErrorLabel);

        String artistLastNameBundle = "events.search.artistlastname";
        String artistLastName = setActiveFiltersAndValidate(Validate.artistLastName, artistLastNameTextField, artistLastNameBundle, artistTitledPane, artistlastnameErrorLabel);

        //+++++++++++++++++++++++EVENT++++++++++++++++++++

        String eventNameBundle = "events.search.eventname";
        String eventName = setActiveFiltersAndValidate(Validate.eventName, eventNameTextField, eventNameBundle, eventsTitledPane, eventnameErrorLabel);

        EventTypeDTO eventType = null;
        if (seatingYesButton.isSelected()) {
            eventType = EventTypeDTO.SEAT;
            Text eventTypeText = new Text(labels.getString("events.search.seating") + " " + eventType.toString());
            textChunks.add(eventTypeText);
        } else if (seatingNoButton.isSelected()) {
            eventType = EventTypeDTO.SECTOR;
            Text eventTypeText = new Text(labels.getString("events.search.seating") + " " + eventType.toString());
            textChunks.add(eventTypeText);
        }

        String durationBundle = "events.search.length";
        String durationStringValue = setActiveFiltersAndValidate(Validate.duration, lengthInMinutesTextField, durationBundle, eventsTitledPane, eventdurationErrorLabel);
        Duration duration = null;
        if(durationStringValue != null) {
            try {
                duration = Duration.ofMinutes(Integer.parseInt(durationStringValue));
            }catch (NumberFormatException e){
                //ignore;
            }
        }

        //+++++++++++++++++++++++TIME++++++++++++++++++++

        LocalDate beginDate = beginTimeDatePicker.getValue();
        LocalDateTime beginDateAndTime = null;
        Integer beginTimeHours = null;
        Integer beginTimeMinutes = null;
        if (beginDate != null) {
            beginTimeHours = beginTimeHourSpinner.getValue();
            beginTimeMinutes = beginTimeMinuteSpinner.getValue();
            beginDateAndTime = LocalDateTime.of(beginDate, LocalTime.of(beginTimeHours, beginTimeMinutes));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
            Text dateText = new Text(labels.getString("events.search.begintime") + " " + beginDateAndTime.format(formatter) + " ");
            textChunks.add(dateText);
        }

        String priceBundle = "events.search.price";
        String priceString = setActiveFiltersAndValidate(Validate.price, priceTextField, priceBundle, timeTitledPane, priceErrorLabel);
        Long price = null;
        if(priceString != null) {
            try {
                price = Long.valueOf(priceString);
            }catch (NumberFormatException e){
                //ignore;
            }
        }

        //+++++++++++++++++++++++LOCATION++++++++++++++++++++

        String locationBundle = "events.search.locationname";
        String locationName = setActiveFiltersAndValidate(Validate.locationName, locationNameTextField, locationBundle, cityTitledPane, locationnameErrorLabel);

        String streetBundle = "events.search.street";
        String street = setActiveFiltersAndValidate(Validate.street, streetTextField, streetBundle, cityTitledPane, streetErrorLabel);

        String cityBundle = "events.search.city";
        String city = setActiveFiltersAndValidate(Validate.city, cityTextField, cityBundle, cityTitledPane, cityErrorLabel);

        String countryBundle = "events.search.contry";
        String country = setActiveFiltersAndValidate(Validate.country, countryTextField, countryBundle, cityTitledPane, countryErrorLabel);

        String postalCodeBundle = "events.search.postalcode";
        String postalCode = setActiveFiltersAndValidate(Validate.postalcode, postalCodeTextField, postalCodeBundle, cityTitledPane, postalcodeErrorLabel);





        SearchDTO searchParameters = new SearchDTO(
            null, eventName,
            artistFirstName, artistLastName,
            eventType, beginDateAndTime,
            price, locationName,
            street, city,
            country, postalCode,
            duration);

        try {
            PageRequestDTO pageRequestDTO = new PageRequestDTO();
            pageRequestDTO.setPage(0);
            pageRequestDTO.setSize(PERFORMANCES_PER_PAGE);
            PageResponseDTO<PerformanceDTO> response = performanceService.findAll(searchParameters, pageRequestDTO);
            performanceData.clear();
            performanceData.addAll(response.getContent());
            totalPages = response.getTotalPages();
            initializeTableView();
            foundEventsTableView.refresh();
            updateCurrentFlowPane();
        } catch (DataAccessException e) {
            LOGGER.error("Search failed!", e);
            JavaFXUtils.createErrorDialog(e.getMessage(),
                priceTextField.getScene().getWindow()).showAndWait();
        }

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
    private void clearAndReloadButton(ActionEvent event) {
        clear();
        loadPerformanceTable(0);
        foundEventsTableView.refresh();
        textChunks.clear();
        updateCurrentFlowPane();

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

        locationnameErrorLabel.setText("");
        streetErrorLabel.setText("");
        cityErrorLabel.setText("");
        postalcodeErrorLabel.setText("");
        artistfirstnameErrorLabel.setText("");
        artistlastnameErrorLabel.setText("");
        eventnameErrorLabel.setText("");
        eventdurationErrorLabel.setText("");
        starttimeErrorLabel.setText("");
        priceErrorLabel.setText("");
        countryErrorLabel.setText("");

        eventsTitledPane.setTextFill(Paint.valueOf("black"));
        artistTitledPane.setTextFill(Paint.valueOf("black"));
        timeTitledPane.setTextFill(Paint.valueOf("black"));
        cityTitledPane.setTextFill(Paint.valueOf("black"));
    }

    private void clearErrorLabels(){
        locationnameErrorLabel.setText("");
        streetErrorLabel.setText("");
        cityErrorLabel.setText("");
        postalcodeErrorLabel.setText("");
        artistfirstnameErrorLabel.setText("");
        artistlastnameErrorLabel.setText("");
        eventnameErrorLabel.setText("");
        eventdurationErrorLabel.setText("");
        starttimeErrorLabel.setText("");
        priceErrorLabel.setText("");
        countryErrorLabel.setText("");

        eventsTitledPane.setTextFill(Paint.valueOf("black"));
        artistTitledPane.setTextFill(Paint.valueOf("black"));
        timeTitledPane.setTextFill(Paint.valueOf("black"));
        cityTitledPane.setTextFill(Paint.valueOf("black"));

    }

    private void updateCurrentFlowPane(){
        flowpane.getChildren().clear();
        flowpane.getChildren().addAll(textChunks);
    }


}
