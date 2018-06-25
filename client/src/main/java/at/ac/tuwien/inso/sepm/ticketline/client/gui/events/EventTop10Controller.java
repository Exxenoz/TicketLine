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
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventRequestTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventResponseTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.sector.SectorCategoryDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.controlsfx.glyphfont.FontAwesome.Glyph.CALENDAR_ALT;

@Component
public class EventTop10Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    public CategoryAxis barAxisX;

    @FXML
    public NumberAxis barAxisY;

    // ---------- Top Ten Tab -----------

    @FXML
    public Label yearLabel;

    @FXML
    public Label monthLabel;

    @FXML
    public Label categoryLabel;

    @FXML
    public ChoiceBox yearChoiceBox;

    @FXML
    private ChoiceBox<String> monthChoiceBox;

    @FXML
    public ChoiceBox categoryChoiceBox;

    @FXML
    public Button showTopTenButton;

    @FXML
    private BarChart<String, Long> topTenBarChart;

    @FXML
    private ChoiceBox<String> topTenEventChoiceBox;

    @FXML
    public Label eventLabel;

    @FXML
    private Button bookTopTenEventButton;

    @FXML
    private TabHeaderController tabHeaderController;

    private final SpringFxmlLoader fxmlLoader;
    private final EventService eventService;
    private final ReservationService reservationService;
    private final SectorCategoryService sectorCategoryService;
    private final EventDetailViewController eventDetailViewController;

    private List<SectorCategoryDTO> sectorCategories;
    private List<EventDTO> currentEvents;
    private PerformanceService performanceService;


    public EventTop10Controller(
        SpringFxmlLoader fxmlLoader,
        EventService eventService,
        ReservationService reservationService,
        SectorCategoryService sectorCategoryService,
        EventDetailViewController eventDetailViewController,
        PerformanceService performanceService) {
        this.fxmlLoader = fxmlLoader;
        this.eventService = eventService;
        this.reservationService = reservationService;
        this.sectorCategoryService = sectorCategoryService;
        this.eventDetailViewController = eventDetailViewController;
        this.performanceService = performanceService;
        sectorCategories = new ArrayList<>();
        currentEvents = new ArrayList<>();
    }

    @FXML
    private void initialize() {
        initYearChoiceBox();
        initMonthChoiceBox();

        //Initialize tab header
        tabHeaderController.setIcon(CALENDAR_ALT);
        tabHeaderController.setTitleBinding(BundleManager.getStringBinding("events.topten.title"));

        initI18N();
    }

    private void initYearChoiceBox() {
        List<Integer> yearList = new ArrayList<>();
        for(int i = LocalDateTime.now().getYear(); i >= 1980; i--) {
            yearList.add(i);
        }
        yearChoiceBox.getItems().setAll(yearList);
        yearChoiceBox.getSelectionModel().selectFirst();
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

    private void initI18N() {
        yearLabel.textProperty().bind(BundleManager.getStringBinding("events.main.year"));
        monthLabel.textProperty().bind(BundleManager.getStringBinding("events.main.month"));
        categoryLabel.textProperty().bind(BundleManager.getStringBinding("events.main.categories"));

        showTopTenButton.textProperty().bind(BundleManager.getStringBinding("events.main.button.show"));

        barAxisX.labelProperty().bind(BundleManager.getStringBinding("events.main.events"));
        barAxisY.labelProperty().bind(BundleManager.getStringBinding("events.main.ticket_sales"));

        eventLabel.textProperty().bind(BundleManager.getStringBinding("events.main.event"));
        bookTopTenEventButton.textProperty().bind(BundleManager.getStringBinding("events.main.button.book"));
    }

    public void onChangeLanguage(Locale language) {
        int selectedIndex = monthChoiceBox.getSelectionModel().getSelectedIndex();
        initMonthChoiceBox();
        monthChoiceBox.getSelectionModel().select(selectedIndex);

        if (categoryChoiceBox.getItems().size() > 0) {
            selectedIndex = categoryChoiceBox.getSelectionModel().getSelectedIndex();
            categoryChoiceBox.getItems().set(0, BundleManager.getBundle().getString("events.main.all"));
            categoryChoiceBox.getSelectionModel().select(selectedIndex);
        }
    }

    public void loadData() {
        updateCategories();
    }

    private void updateCategories() {
        categoryChoiceBox.getItems().clear();
        categoryChoiceBox.getItems().add(BundleManager.getBundle().getString("events.main.all"));

        try {
            sectorCategories = sectorCategoryService.findAllOrderByBasePriceModAsc();
            for (SectorCategoryDTO sectorCategory : sectorCategories) {
                categoryChoiceBox.getItems().add(sectorCategory.getName());
            }
        } catch (DataAccessException e) {
            LOGGER.error("Couldn't get sector categories: " + e.getMessage());
            JavaFXUtils.createErrorDialog(e.getMessage(), categoryChoiceBox.getScene().getWindow()).showAndWait();
        }
        categoryChoiceBox.getSelectionModel().selectFirst();
    }

    @FXML
    void showTopTenClicked(ActionEvent event) {
        topTenEventChoiceBox.getItems().clear();

        Integer month = monthChoiceBox.getSelectionModel().getSelectedIndex() > 0 ? monthChoiceBox.getSelectionModel().getSelectedIndex() + 1 : 1;
        Integer year = (Integer) yearChoiceBox.getSelectionModel().getSelectedItem();
        Integer categorySelectionIndex = categoryChoiceBox.getSelectionModel().getSelectedIndex() > 0 ? categoryChoiceBox.getSelectionModel().getSelectedIndex() - 1 : null;
        Long categoryId = null;
        if (categorySelectionIndex != null) {
            categoryId = sectorCategories.get(categorySelectionIndex).getId();
        }

        LOGGER.info("Show Top 10 Events for month: " + monthChoiceBox.getSelectionModel().getSelectedItem() + " and categoryId: " + categoryId);

        try {
            List<EventResponseTopTenDTO> events = eventService.findTopTenByMonthAndCategory(new EventRequestTopTenDTO(month, year, categoryId));
            showTopTenEvents(events, true);
        } catch (DataAccessException e) {
            LOGGER.error("Couldn't fetch top 10 events from server for month: " + month + " " + e.getMessage());
            JavaFXUtils.createErrorDialog(e.getMessage(), monthChoiceBox.getScene().getWindow()).showAndWait();
        }
    }

    private void showTopTenEvents(List<EventResponseTopTenDTO> response, boolean repeat) {
        currentEvents.clear();
        topTenBarChart.getData().clear();
        barAxisX.getCategories().clear();

        XYChart.Series<String, Long> barSeries = new XYChart.Series();
        barSeries.setName(monthChoiceBox.getSelectionModel().getSelectedItem());

        ArrayList<String> categories = new ArrayList<>();

        int categoryOccurrences[] = new int[response.size()];

        int i = 0;
        for (EventResponseTopTenDTO eventResponse : response) {
            if (!categories.contains(eventResponse.getEvent().getName())) {
                categories.add(eventResponse.getEvent().getName());
            } else {
                // add number of occurrence to event name
                categoryOccurrences[i]++;
                categories.add(eventResponse.getEvent().getName() + "_" + (categoryOccurrences[i] + 1));
            }
            i++;
        }

        barAxisX.setCategories(FXCollections.observableArrayList(categories));

        i = 0;
        for (EventResponseTopTenDTO eventResponse : response) {
            EventDTO eventDTO = eventResponse.getEvent();
            if(categoryOccurrences[i] <= 0) {
                barSeries.getData().add(new XYChart.Data(eventDTO.getName(), eventResponse.getSales()));
            } else {
                barSeries.getData().add(new XYChart.Data(eventDTO.getName() + "_" + (categoryOccurrences[i] + 1), eventResponse.getSales()));
            }
            currentEvents.add(eventDTO);
            topTenEventChoiceBox.getItems().add(eventDTO.getName());
            i++;
        }

        if (response.size() > 0) {
            topTenBarChart.getData().add(barSeries);
            topTenEventChoiceBox.getSelectionModel().selectFirst();
            bookTopTenEventButton.setDisable(false);
        } else {
            bookTopTenEventButton.setDisable(true);
        }

        registerBarChartListener(barSeries);

        if (repeat) {
            showTopTenEvents(response, false);
        }
    }

    private void registerBarChartListener(XYChart.Series<String, Long> barSeries) {
        for (final XYChart.Data<String, Long> data : barSeries.getData()) {
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
        int selectedIndex = topTenEventChoiceBox.getSelectionModel().getSelectedIndex() > 0 ? topTenEventChoiceBox.getSelectionModel().getSelectedIndex() : 0;
        if (currentEvents.size() > 0) {
            Stage stage = new Stage();

            final var parent = fxmlLoader.<Parent>load("/fxml/events/eventDetailView.fxml");
            eventDetailViewController.fill(currentEvents.get(selectedIndex), stage);

            stage.setScene(new Scene(parent));
            stage.setTitle(BundleManager.getBundle().getString("bookings.event.details.title"));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(topTenEventChoiceBox.getScene().getWindow());
            stage.showAndWait();
        }
    }
}
