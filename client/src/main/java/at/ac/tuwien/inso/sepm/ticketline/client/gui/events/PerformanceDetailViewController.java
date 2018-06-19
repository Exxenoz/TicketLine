package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.booking.HallPlanController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.PerformanceService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.PriceUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventTypeDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Component
public class PerformanceDetailViewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public Label eventName;

    @FXML
    private Label performanceHeader;

    @FXML
    private Label locationName;

    @FXML
    private Label startTime;

    @FXML
    private Label artistNamePerformance;

    @FXML
    private Label performancePrice;

    private PerformanceDTO performance;

    private final SpringFxmlLoader fxmlLoader;
    private final EventDetailViewController eventDetailViewController;
    private final PerformanceService performanceService;
    private final HallPlanController hallPlanController;

    private Stage stage;
    private ReservationDTO reservation;

    public PerformanceDetailViewController(
        SpringFxmlLoader fxmlLoader,
        EventDetailViewController eventDetailViewController,
        PerformanceService performanceService,
        HallPlanController hallPlanController
    ) {
        this.fxmlLoader = fxmlLoader;
        this.eventDetailViewController = eventDetailViewController;
        this.performanceService = performanceService;
        this.hallPlanController = hallPlanController;
    }

    @FXML
    private void initialize() {
        reservation = new ReservationDTO();
        performanceHeader.setText(performance.getName());
        eventName.setText(performance.getEvent().getName());
        locationName.setText(performance.getLocationAddress().getLocationName() + ", "
            + performance.getLocationAddress().getStreet() + ", "
            + performance.getAddress().getPostalCode());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
        startTime.setText(performance.getPerformanceStart().format(formatter));

        String artistList = performance.getArtists()
            .stream()
            .map(artist -> artist.getFirstName() + " " + artist.getLastName())
            .collect(Collectors.joining(", "));
        artistNamePerformance.setText(artistList);
        performancePrice.setText(PriceUtils.priceToRepresentation(performance.getPrice()));
    }

    @FXML
    void continueButton(ActionEvent event) {
        reservation.setPerformance(performance);
        hallPlanController.fill(reservation, stage);

        // Fill with correct sub controller dependent on event category
        if(performance.getEvent().getEventType() == EventTypeDTO.SEAT) {

        } else {

        }

        Parent parent = fxmlLoader.load("/fxml/events/book/hallPlanView.fxml");
        stage.setScene(new Scene(parent));

        // Setting stage name
        if (performance.getEvent().getEventType() == EventTypeDTO.SEAT) {
            stage.setTitle(BundleManager.getBundle().getString("bookings.hallplan.title"));
        } else {
            stage.setTitle(BundleManager.getBundle().getString("bookings.sectorplan.title"));
        }
        stage.centerOnScreen();
    }

    @FXML
    void changeToEventDetailView(ActionEvent event) {
        Parent parent = fxmlLoader.load("/fxml/events/eventDetailView.fxml");
        eventDetailViewController.fill(performance.getEvent(), stage);
        stage.setScene(new Scene(parent));
        stage.setTitle(BundleManager.getBundle().getString("bookings.event.details.title"));
        stage.centerOnScreen();
    }

    @FXML
    void backButton(ActionEvent event) {
        Stage stage = (Stage) performanceHeader.getScene().getWindow();
        stage.close();
    }

    public void fill(PerformanceDTO performance, Stage stage) {
        this.performance = performance;
        this.stage = stage;
    }
}
