package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.AddressValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.PerformanceSearchValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.PerformanceService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.client.validator.BaseAddressValidator;
import at.ac.tuwien.inso.sepm.ticketline.client.validator.LocationAddressValidator;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventTypeDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.SearchDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.beans.binding.Bindings;
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

import static at.ac.tuwien.inso.sepm.ticketline.client.validator.PerformanceSearchValidator.*;
import static javafx.collections.FXCollections.observableArrayList;
import static org.controlsfx.glyphfont.FontAwesome.Glyph.CALENDAR_ALT;

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
    public Label activeFiltersLabel;
    public Label artistFirstnameLabel;
    public Label artistLastnameLabel;
    public Label eventNameLabel;
    public Label lengthInMinutesLabel;
    public Label seatingLabel;
    public Label beginTimeLabel;
    public Label priceLabel;
    public Label locationNameLabel;
    public Label streetLabel;
    public Label cityLabel;
    public Label postalCodeLabel;
    public Label countryLabel;

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
    private SearchDTO searchDTO;

    private ObservableList<PerformanceDTO> performanceData = observableArrayList();
    private int page = 0;
    private int totalPages = 1;
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
        LOGGER.info("Initialize EventSearchController");
        SpinnerValueFactory<Integer> beginTimeHoursFactory = buildSpinner(23);
        SpinnerValueFactory<Integer> beginTimeMinutesFactory = buildSpinner(59);
        seatingYesButton.setSelected(false);
        seatingNoButton.setSelected(false);

        beginTimeHoursFactory.setValue(0);
        beginTimeMinutesFactory.setValue(0);

        beginTimeHourSpinner.setValueFactory(beginTimeHoursFactory);
        beginTimeMinuteSpinner.setValueFactory(beginTimeMinutesFactory);

        tabHeaderController.setIcon(CALENDAR_ALT);
        tabHeaderController.setTitleBinding(BundleManager.getStringBinding("bookings.table.event"));
        initializeTableView();

        initI18N();
    }

    private void initializeTableView() {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getName()));
        eventColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getEvent().getName()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
        startTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getPerformanceStart().format(formatter)));
        locationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getLocationAddress().getLocationName() + ", " +
            cellData.getValue().getLocationAddress().getCountry() + ", " +
            cellData.getValue().getLocationAddress().getCity()));

        startTimeColumn.setComparator((d1, d2) -> {
            LocalDateTime date1 = LocalDateTime.parse(d1, formatter);
            LocalDateTime date2 = LocalDateTime.parse(d2, formatter);
            return date1.compareTo(date2);
        });

        foundEventsTableView.setItems(performanceData);

    }

    private void initI18N() {
        activeFiltersLabel.textProperty().bind(BundleManager.getStringBinding("events.main.activefilters"));

        clearButton.textProperty().bind(BundleManager.getStringBinding("events.main.button.clear"));

        nameColumn.textProperty().bind(BundleManager.getStringBinding("events.table.name"));
        eventColumn.textProperty().bind(BundleManager.getStringBinding("events.table.event"));
        startTimeColumn.textProperty().bind(BundleManager.getStringBinding("events.table.starttime"));
        locationColumn.textProperty().bind(BundleManager.getStringBinding("events.table.location"));

        artistTitledPane.textProperty().bind(BundleManager.getStringBinding("events.main.search.artist"));
        artistFirstnameLabel.textProperty().bind(BundleManager.getStringBinding("events.search.artistfirstname"));
        artistLastnameLabel.textProperty().bind(BundleManager.getStringBinding("events.search.artistlastname"));

        eventsTitledPane.textProperty().bind(BundleManager.getStringBinding("events.main.search.event"));
        eventNameLabel.textProperty().bind(BundleManager.getStringBinding("events.search.eventname"));
        lengthInMinutesLabel.textProperty().bind(BundleManager.getStringBinding("events.search.length"));
        seatingLabel.textProperty().bind(BundleManager.getStringBinding("events.search.seating"));
        seatingYesButton.textProperty().bind(BundleManager.getStringBinding("events.search.seatingyes"));
        seatingNoButton.textProperty().bind(BundleManager.getStringBinding("events.search.seatingno"));

        timeTitledPane.textProperty().bind(BundleManager.getStringBinding("events.main.search.time"));
        beginTimeLabel.textProperty().bind(BundleManager.getStringBinding("events.search.begintime"));
        priceLabel.textProperty().bind(BundleManager.getStringBinding("events.search.price"));

        cityTitledPane.textProperty().bind(BundleManager.getStringBinding("events.main.search.city"));
        locationNameLabel.textProperty().bind(BundleManager.getStringBinding("events.search.locationname"));
        streetLabel.textProperty().bind(BundleManager.getStringBinding("events.search.street"));
        cityLabel.textProperty().bind(BundleManager.getStringBinding("events.search.city"));
        postalCodeLabel.textProperty().bind(BundleManager.getStringBinding("events.search.postalcode"));
        countryLabel.textProperty().bind(BundleManager.getStringBinding("events.search.contry"));

        bookButton.textProperty().bind(BundleManager.getStringBinding("events.main.button.book"));
        searchButton.textProperty().bind(BundleManager.getStringBinding("events.main.button.search"));
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
                    LOGGER.debug("Getting next Page {}", page);
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
        startTimeColumn.sortTypeProperty().addListener(tableColumnSortChangeListener);
        locationColumn.sortTypeProperty().addListener(tableColumnSortChangeListener);

        loadPerformanceTable(0);
    }

    private void clear() {
        LOGGER.debug("clearing the data");
        performanceData.clear();
        page = 0;
        totalPages = 1;
        ScrollBar scrollBar = getVerticalScrollbar(foundEventsTableView);
        if (scrollBar != null) {
            scrollBar.setValue(0);
        }
    }

    private void loadPerformanceTable(int page) {
        if (page < 0 || page >= totalPages) {
            LOGGER.warn("Could not load Performances table page, because page parameter is invalid!");
            return;
        }
        LOGGER.debug("Loading Performances of Page {}", page);
        PageRequestDTO pageRequestDTO = null;

        if (sortedColumn != null) {
            Sort.Direction sortDirection = (sortedColumn.getSortType() == TableColumn.SortType.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC;
            pageRequestDTO = new PageRequestDTO(page, PERFORMANCES_PER_PAGE, sortDirection, getColumnNameBy(sortedColumn));
        } else {
            pageRequestDTO = new PageRequestDTO(page, PERFORMANCES_PER_PAGE, Sort.Direction.ASC, null);
        }

        PageResponseDTO<PerformanceDTO> responseDTO;
        try {
            if(searchDTO != null){
                responseDTO = performanceService.findAll(searchDTO, pageRequestDTO);
            }else {
                responseDTO = performanceService.findAll(pageRequestDTO);
            }

            for (PerformanceDTO performanceDTO : responseDTO.getContent()) {
                performanceData.remove(performanceDTO); // New created entries must be removed first, so they can be re-added at their sorted location in the next line
                performanceData.add(performanceDTO);
            }

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
        LOGGER.info("User clicked the book button");
        Stage stage = new Stage();
        PerformanceDTO row = foundEventsTableView.getSelectionModel().getSelectedItem();
        if (row != null) {
            performanceDetailViewController.fill(row, stage);

            final var parent = fxmlLoader.<Parent>load("/fxml/events/performanceDetailView.fxml");

            stage.setScene(new Scene(parent));
            stage.setTitle(BundleManager.getBundle().getString("bookings.performance.details.title"));


            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(bookButton.getScene().getWindow());

            stage.showAndWait();
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText(BundleManager.getBundle().getString("events.book.nothingselected"));
            errorAlert.showAndWait();
        }
    }

    String setActiveFiltersAndValidate(Validate toValidate,
                                       TextField currentTextField,
                                       String bundleName,
                                       TitledPane currentTitledPane,
                                       Label errorLabel) {
        String returnString = null;
        Text currentTextChunk = null;

        try {
            if (currentTextField.getText() != null && !currentTextField.getText().equals("")) {
                returnString = currentTextField.getText();
                String limitForActiveFiltersString = currentTextField.getText();
                    if(limitForActiveFiltersString.length() >= 50) {
                    limitForActiveFiltersString = returnString.substring(0, 50);
                    }
                currentTextChunk = new Text();
                currentTextChunk.textProperty().bind(Bindings.concat(BundleManager.getStringBinding(bundleName), " " + limitForActiveFiltersString + " "));
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
                        returnString = LocationAddressValidator.validateLocationName(currentTextField);
                        break;
                    case street:
                        returnString = BaseAddressValidator.validateStreet(currentTextField, false);
                        break;
                    case city:
                        returnString = BaseAddressValidator.validateCity(currentTextField, false);
                        break;
                    case country:
                        returnString = BaseAddressValidator.validateCountry(currentTextField, false);
                        break;
                    case postalcode:
                        returnString = BaseAddressValidator.validatePostalCode(currentTextField, false);
                        break;
                }
            }
            errorLabel.textProperty().unbind();
            errorLabel.setText("");
        } catch (PerformanceSearchValidationException e) {
            LOGGER.error("Error with artist first name value: ", e.getMessage());
            errorLabel.textProperty().bind(BundleManager.getExceptionStringBinding(e.getExceptionBundleKey()));
            int index = textChunks.indexOf(currentTextChunk);
            textChunks.get(index).setFill(Paint.valueOf("red"));
            currentTitledPane.setTextFill(Paint.valueOf("red"));
        } catch (AddressValidationException e) {
            LOGGER.error("Error with artist first name value: ", e.getMessage());
            errorLabel.textProperty().bind(BundleManager.getExceptionStringBinding(e.getExceptionBundleKey()));
            int index = textChunks.indexOf(currentTextChunk);
            textChunks.get(index).setFill(Paint.valueOf("red"));
            currentTitledPane.setTextFill(Paint.valueOf("red"));
        }

        return returnString;
    }

    @FXML
    void searchForPerformancesButton(ActionEvent event) {
        LOGGER.info("User clicked the search button");
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
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
            Text dateText = new Text(labels.getString("events.search.begintime") + " " + beginDateAndTime.format(formatter) + " ");
            textChunks.add(dateText);
        }

        String priceBundle = "events.search.price";
        String priceString = setActiveFiltersAndValidate(Validate.price, priceTextField, priceBundle, timeTitledPane, priceErrorLabel);
        Long price = null;
        if(priceString != null) {
            try {
                priceString = priceString.replace(',', '.');
                price = (long)(Double.valueOf(priceString) * 100);
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

        // Check if an error occurred
        if (!locationnameErrorLabel.getText().isEmpty() ||
            !streetErrorLabel.getText().isEmpty() ||
            !cityErrorLabel.getText().isEmpty() ||
            !postalcodeErrorLabel.getText().isEmpty() ||
            !artistfirstnameErrorLabel.getText().isEmpty() ||
            !artistlastnameErrorLabel.getText().isEmpty() ||
            !eventnameErrorLabel.getText().isEmpty() ||
            !eventdurationErrorLabel.getText().isEmpty() ||
            !starttimeErrorLabel.getText().isEmpty() ||
            !priceErrorLabel.getText().isEmpty() ||
            !countryErrorLabel.getText().isEmpty())
        {
            return;
        }

        searchDTO = new SearchDTO(
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
            PageResponseDTO<PerformanceDTO> response = performanceService.findAll(searchDTO, pageRequestDTO);
            performanceData.clear();
            performanceData.addAll(response.getContent());
            totalPages = response.getTotalPages();
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
        LOGGER.info("User clicked the clear button");
        searchDTO = null;
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

        clearErrorLabels();
    }

    private void clearErrorLabels(){
        LOGGER.debug("Clearing the error labels");
        locationnameErrorLabel.textProperty().unbind();
        streetErrorLabel.textProperty().unbind();
        cityErrorLabel.textProperty().unbind();
        postalcodeErrorLabel.textProperty().unbind();
        artistfirstnameErrorLabel.textProperty().unbind();
        artistlastnameErrorLabel.textProperty().unbind();
        eventnameErrorLabel.textProperty().unbind();
        eventdurationErrorLabel.textProperty().unbind();
        starttimeErrorLabel.textProperty().unbind();
        priceErrorLabel.textProperty().unbind();
        countryErrorLabel.textProperty().unbind();

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
