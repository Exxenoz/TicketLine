package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.PerformanceService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.ReservationService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.SectorCategoryService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventFilterTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventTypeDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.SearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.sector.SectorCategoryDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.controlsfx.glyphfont.FontAwesome.Glyph.CALENDAR_ALT;
import static org.controlsfx.glyphfont.FontAwesome.Glyph.NEWSPAPER_ALT;

@Component
public class EventController {

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

    // ---------- Top Ten Tab -----------

    @FXML
    private ChoiceBox<String> monthChoiceBox;

    @FXML
    public ChoiceBox categoryChoiceBox;

    @FXML
    private Button showTopTenButton;

    @FXML
    private BarChart<String, Integer> topTenBarChart;

    @FXML
    private ChoiceBox<String> topTenEventChoiceBox;

    @FXML
    private Button bookTopTenEventButton;

    private EventService eventService;
    private PerformanceService performanceService;
    private ReservationService reservationService;
    private SectorCategoryService sectorCategoryService;

    private ObservableList<PerformanceDTO> performanceData = FXCollections.observableArrayList();

    private List<PerformanceDTO> performances;

    private PerformanceDetailViewController performanceDetailViewController;
    private EventDetailViewController eventDetailViewController;

    private List<SectorCategoryDTO> sectorCategories;
    private List<EventDTO> currentEvents;

    @FXML
    private TabHeaderController tabHeaderController;

    private String activeFilters = "";

    public EventController(EventService eventService, PerformanceService performanceService, ReservationService reservationService, SectorCategoryService sectorCategoryService) {
        this.eventService = eventService;
        this.performanceService = performanceService;
        this.reservationService = reservationService;
        this.sectorCategoryService = sectorCategoryService;
        sectorCategories = new ArrayList<>();
        currentEvents = new ArrayList<>();
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(CALENDAR_ALT);
        tabHeaderController.setTitle("Top 10 Events");

        SpinnerValueFactory<Integer> beginTimeHoursFactory = buildSpinner(23);
        SpinnerValueFactory<Integer> beginTimeMinutesFactory = buildSpinner(59);
        seatingYesButton.setSelected(false);
        seatingNoButton.setSelected(false);

        beginTimeHoursFactory.setValue(0);
        beginTimeMinutesFactory.setValue(0);

        beginTimeHourSpinner.setValueFactory(beginTimeHoursFactory);
        beginTimeMinuteSpinner.setValueFactory(beginTimeMinutesFactory);

        initMonthChoiceBox();
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

    public void loadData() {
        try {
            performances = performanceService.findAll();
            intializeTableView();
        } catch (DataAccessException e) {
            LOGGER.error("Couldn't fetch performances from server!", e);
        }

        updateCategories();
    }

    public void updateCategories() {
        categoryChoiceBox.getItems().clear();
        categoryChoiceBox.getItems().add(BundleManager.getBundle().getString("events.main.all"));

        try {
            sectorCategories = sectorCategoryService.findAllOrderByBasePriceModAsc();
            for(SectorCategoryDTO sectorCategory : sectorCategories) {
                categoryChoiceBox.getItems().add(sectorCategory.getName());
            }
        } catch (DataAccessException e) {
            LOGGER.error("Couldn't get sector categories: " + e.getMessage());
            JavaFXUtils.createErrorDialog(e.getMessage(), categoryChoiceBox.getScene().getWindow()).showAndWait();
        }
        categoryChoiceBox.getSelectionModel().selectFirst();
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

    private void intializeTableView() {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        eventColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvent().getName()));
        startTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPerformanceStart().toString()));
        locationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getLocation()));

        performanceData = FXCollections.observableArrayList(performances);
        foundEventsTableView.setItems(performanceData);
    }

    @FXML
    private void bookPerformance(ActionEvent event) {
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/events/performanceDetailView.fxml"));
        fxmlLoader.setControllerFactory(classToLoad -> classToLoad.isInstance(performanceDetailViewController) ? performanceDetailViewController : null);

        int row = foundEventsTableView.getSelectionModel().getFocusedIndex();
        performanceDetailViewController.fill(performances.get(row));

        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setTitle("Performance Details");

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(bookButton.getScene().getWindow());

            stage.showAndWait();
        } catch (IOException e) {
            LOGGER.error("Detail View Performance window couldn't be opened!");
        }
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
        if(seatingYesButton.isSelected()){
            eventType = EventTypeDTO.SEAT;
            addToCurrentSearchParameters(eventType.toString());
        } else if (seatingNoButton.isSelected()){
            eventType = EventTypeDTO.SECTOR;
            addToCurrentSearchParameters(eventType.toString());
        }
        String durationString = lengthInMinutesTextField.getText();
        Duration duration = null;
        if(!durationString.equals("")){
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
        Double price = null;
        if (!priceString.equals("")) {
            addToCurrentSearchParameters(priceString);
            price = Double.parseDouble(priceString);
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

    private void addToCurrentSearchParameters(String searchParameter){
        if(searchParameter!=null && !searchParameter.equals("")){
            activeFilters += searchParameter + ", ";
        }
    }

    private void updateCurrentSearchParameters(){
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
        } catch (DataAccessException e) {
            LOGGER.error("Couldn't fetch performances from server!", e);
        }

    }

    @FXML
    void showTopTenClicked(ActionEvent event) {
        topTenEventChoiceBox.getItems().clear();

        Integer month = monthChoiceBox.getSelectionModel().getSelectedIndex() > 0 ? monthChoiceBox.getSelectionModel().getSelectedIndex() + 1 : 1;
        Integer categorySelectionIndex = categoryChoiceBox.getSelectionModel().getSelectedIndex() > 0 ? categoryChoiceBox.getSelectionModel().getSelectedIndex() - 1 : null;
        Long categoryId = null;
        if (categorySelectionIndex != null) {
            categoryId = sectorCategories.get(categorySelectionIndex).getId();
        }

        LOGGER.info("Show Top 10 Events for month: " + monthChoiceBox.getSelectionModel().getSelectedItem() + " and categoryId: " + categoryId);

        try {
            List<EventDTO> events = eventService.findTop10ByPaidReservationCountByFilter(new EventFilterTopTenDTO(month, categoryId));
            showTopTenEvents(events, monthChoiceBox.getSelectionModel().getSelectedItem());
        } catch (DataAccessException e) {
            LOGGER.error("Couldn't fetch top 10 events from server for month: " + month + " " + e.getMessage());
            JavaFXUtils.createErrorDialog(e.getMessage(), monthChoiceBox.getScene().getWindow()).showAndWait();
        }
    }

    private void showTopTenEvents(List<EventDTO> events, String month) {
        topTenBarChart.getData().clear();

        XYChart.Series<String, Integer> barSeries = new XYChart.Series();
        barSeries.setName(month);

        for (EventDTO event : events) {
            try {
                Long ticketSaleCount = reservationService.getPaidReservationCountByEvent(event);
                barSeries.getData().add(new XYChart.Data(event.getName(), ticketSaleCount));
                currentEvents.add(event);
                topTenEventChoiceBox.getItems().add(event.getName());
            } catch (DataAccessException e) {
                LOGGER.error("Couldn't fetch reservation of event: " + event + " " + e.getMessage());
                JavaFXUtils.createErrorDialog(e.getMessage(), monthChoiceBox.getScene().getWindow()).showAndWait();
            }
        }

        if (events.size() > 0) {
            topTenBarChart.getData().add(barSeries);
            topTenEventChoiceBox.getSelectionModel().selectFirst();
            bookTopTenEventButton.setDisable(false);
        } else {
            bookTopTenEventButton.setDisable(true);
        }

        registerBarChartListener(barSeries);
    }

    private void registerBarChartListener(XYChart.Series<String, Integer> barSeries) {
        for (final XYChart.Data<String, Integer> data : barSeries.getData()) {
            Node node = data.getNode();

            node.setOnMouseEntered(event -> {
                node.getStyleClass().add("hover");
            });

            node.setOnMouseExited(event -> {
                node.getStyleClass().remove("hover");
                node.getStyleClass().remove("mouse-down");
            });

            node.setOnMousePressed(event -> {
                node.getStyleClass().add("mouse-down");
            });

            node.setOnMouseClicked(event -> {
                topTenEventChoiceBox.getSelectionModel().select(data.getXValue());
                node.getStyleClass().remove("mouse-down");
            });
        }
    }

    @FXML
    void bookTopTenEvent(ActionEvent event) {
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/events/eventDetailView.fxml"));
        fxmlLoader.setControllerFactory(classToLoad -> classToLoad.isInstance(eventDetailViewController) ? eventDetailViewController : null);

        int selectedIndex = topTenEventChoiceBox.getSelectionModel().getSelectedIndex() > 0 ? topTenEventChoiceBox.getSelectionModel().getSelectedIndex() : 0;
        eventDetailViewController.fill(currentEvents.get(selectedIndex));

        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setTitle("Event Details");

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(bookButton.getScene().getWindow());

            stage.showAndWait();
        } catch (IOException e) {
            LOGGER.error("Detail View Events window couldn't be opened!");
        }
    }
}
