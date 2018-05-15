package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
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
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationFilterTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.sector.SectorCategoryDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class EventTop10Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // ---------- Top Ten Tab -----------

    @FXML
    private ChoiceBox<String> monthChoiceBox;

    @FXML
    public ChoiceBox categoryChoiceBox;

    @FXML
    private BarChart<String, Integer> topTenBarChart;

    @FXML
    private ChoiceBox<String> topTenEventChoiceBox;

    @FXML
    private Button bookTopTenEventButton;

    private final SpringFxmlLoader fxmlLoader;
    private final EventService eventService;
    private final ReservationService reservationService;
    private final SectorCategoryService sectorCategoryService;
    private final EventDetailViewController eventDetailViewController;

    private List<SectorCategoryDTO> sectorCategories;
    private List<EventDTO> currentEvents;


    public EventTop10Controller(
        SpringFxmlLoader fxmlLoader,
        EventService eventService,
        ReservationService reservationService,
        SectorCategoryService sectorCategoryService,
        EventDetailViewController eventDetailViewController
    ) {
        this.fxmlLoader = fxmlLoader;
        this.eventService = eventService;
        this.reservationService = reservationService;
        this.sectorCategoryService = sectorCategoryService;
        this.eventDetailViewController = eventDetailViewController;
        sectorCategories = new ArrayList<>();
        currentEvents = new ArrayList<>();
    }

    @FXML
    private void initialize() {
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
        Integer categorySelectionIndex = categoryChoiceBox.getSelectionModel().getSelectedIndex() > 0 ? categoryChoiceBox.getSelectionModel().getSelectedIndex() - 1 : null;
        Long categoryId = null;
        if (categorySelectionIndex != null) {
            categoryId = sectorCategories.get(categorySelectionIndex).getId();
        }

        LOGGER.info("Show Top 10 Events for month: " + monthChoiceBox.getSelectionModel().getSelectedItem() + " and categoryId: " + categoryId);

        try {
            List<EventDTO> events = eventService.findTop10ByPaidReservationCountByFilter(new EventFilterTopTenDTO(month, categoryId));
            showTopTenEvents(events);
        } catch (DataAccessException e) {
            LOGGER.error("Couldn't fetch top 10 events from server for month: " + month + " " + e.getMessage());
            JavaFXUtils.createErrorDialog(e.getMessage(), monthChoiceBox.getScene().getWindow()).showAndWait();
        }
    }

    private void showTopTenEvents(List<EventDTO> events) {
        topTenBarChart.getData().clear();

        XYChart.Series<String, Integer> barSeries = new XYChart.Series();
        barSeries.setName(monthChoiceBox.getSelectionModel().getSelectedItem());

        for (EventDTO event : events) {
            try {
                Long ticketSaleCount = reservationService.getPaidReservationCountByFilter(new ReservationFilterTopTenDTO(monthChoiceBox.getSelectionModel().getSelectedIndex() + 1, event.getId()));
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
        final var parent = fxmlLoader.<Parent>load("/fxml/events/eventDetailView.fxml");
        int selectedIndex = topTenEventChoiceBox.getSelectionModel().getSelectedIndex() > 0 ? topTenEventChoiceBox.getSelectionModel().getSelectedIndex() : 0;
        Stage stage = new Stage();
        eventDetailViewController.fill(currentEvents.get(selectedIndex), stage);
        stage.setScene(new Scene(parent));
        stage.setTitle("Event Details");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(bookTopTenEventButton.getScene().getWindow());
        stage.showAndWait();
    }
}
