package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Component
public class EventDetailViewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    private Label eventHeading;

    @FXML
    private Label eventNameEvent;

    @FXML
    private Label artistNameEvent;

    @FXML
    private Label descriptionEvent;

    @FXML
    private Label eventTypeEvent;

    @FXML
    private TableView<PerformanceDTO> performanceDatesTableView;

    @FXML
    private TableColumn<PerformanceDTO, String> endTimeColumn;

    @FXML
    private TableColumn<PerformanceDTO, String> startTimeColumn;

    @FXML
    private TableColumn<PerformanceDTO, String> locationColumn;

    private List<PerformanceDTO> performances;

    private ObservableList<PerformanceDTO> performanceData = FXCollections.observableArrayList();


    @FXML
    private Button bookButtonEvent;

    private PerformanceDetailViewController performanceDetailViewController;

  /*  public EventDetailViewController(PerformanceDetailViewController performanceDetailViewController) {
        this.performanceDetailViewController = performanceDetailViewController;
    } */

    @FXML
    private void changeToPerformanceDetailView(ActionEvent event) {
     /*   final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/events/performanceDetailView.fxml"));
        fxmlLoader.setControllerFactory(classToLoad -> classToLoad.isInstance(performanceDetailViewController) ? performanceDetailViewController : null);

        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setTitle("Event Details");

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(bookButtonEvent.getScene().getWindow());

            stage.showAndWait();
        } catch (IOException e) {
            LOGGER.error("Detail View Event window couldn't be opened!");
        } */

    }

    public void fill(EventDTO event) {
        eventHeading.setText(event.getName());
        eventNameEvent.setText(event.getName());
        artistNameEvent.setText(event.getArtists().toString());
        descriptionEvent.setText(event.getDescription());
        if (event.getEventType().toString().equals("SEATS")) {
            eventTypeEvent.setText("yes");
        } else {
            eventTypeEvent.setText("no");
        }


        //TODO: Get performances for table View

    }

    private void intializeTableView() {
        startTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPerformanceStart().toString()));
        endTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPerformanceEnd().toString()));
        locationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress().getCity()));

        performanceData = FXCollections.observableArrayList(performances);
        performanceDatesTableView.setItems(performanceData);
    }

}
