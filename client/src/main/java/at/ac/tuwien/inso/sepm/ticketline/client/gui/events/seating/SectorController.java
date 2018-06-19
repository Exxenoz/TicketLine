package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.sector.SectorRow;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.sector.SectorDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SectorController {

    private final static double INITIAL_OFFSET = 10.0;
    private final static double REGULAR_HEIGHT = 100.0;
    private final static double REGULAR_MARGIN = 20.0;
    private final static double IN_BETWEEN_MARGIN = 40.0;
    //UI
    @FXML
    private VBox sectorDetailsVBox;

    //State
    PerformanceDTO performance;
    List<ReservationDTO> reservations;
    List<SectorRow> sectorRows;

    @FXML
    private ScrollBar sectorDetailsScrollBar;

    private SeatSelectionListener seatSelectionListener;

    public SectorController(SeatSelectionListener seatSelectionListener) {
        this.seatSelectionListener = seatSelectionListener;
    }

    @FXML
    public void initialize() {}

    public void fill(PerformanceDTO performance, List<ReservationDTO> reservationDTOS) {
        this.performance = performance;
        this.reservations = reservationDTOS;

        addSectors(this.performance);
    }

    public void addSectors(PerformanceDTO performance) {
        List<SectorDTO> sectors = performance.getHall().getSectors();
        this.sectorRows = new ArrayList<>();

        double height = INITIAL_OFFSET;
        int counter = 0;
        for(SectorDTO s: performance.getHall().getSectors()) {
            counter++;
            sectorRows.add(createNewSectorRow(s, height, REGULAR_MARGIN, counter));
            height += REGULAR_HEIGHT;
        }
    }

    public SectorRow createNewSectorRow(SectorDTO sectorDTO, double height, double margin, int count) {
        //Create and add sector label
        HBox hBox = new HBox();
        hBox.setLayoutX(margin);
        hBox.setLayoutY(height);

        Label sectorLabel = createSectorLabel(sectorDTO, height, margin, count);
        addLabel(sectorLabel);

        //Create and add price label
        margin += IN_BETWEEN_MARGIN;
        Label priceLabel = createPriceLabel(sectorDTO, height, margin);
        addLabel(priceLabel);

        //Create and add spinner
        margin += IN_BETWEEN_MARGIN;
        Spinner spinner = createSpinner(height, margin);
        addSpinner(spinner);

        //.. and return the created row!
        return new SectorRow(sectorDTO, sectorLabel, priceLabel, spinner);
    }

    public Label createSectorLabel(SectorDTO sectorDTO, double height, double margin, int count) {
        Label sectorLabel = new Label();
        sectorLabel.setLayoutX(margin);
        sectorLabel.setLayoutY(height);
        sectorLabel.setText("Sector: " + count);
        return sectorLabel;
    }

    public Label createPriceLabel(SectorDTO sectorDTO, double height, double margin) {
        Label priceLabel = new Label();
        priceLabel.setLayoutX(margin);
        priceLabel.setLayoutY(height);
        priceLabel.setText(Long.toString(sectorDTO.getCategory().getBasePriceMod() * performance.getPrice()));
        return priceLabel;
    }

    public Spinner<Integer> createSpinner(double height, double margin) {
        Spinner<Integer> spinner = new Spinner<>();
        spinner.setLayoutX(margin);
        spinner.setLayoutY(height);
        return spinner;
    }

    public void addLabel(Label label) {
        sectorDetailsVBox.getChildren().add(label);
    }

    public void addSpinner(Spinner spinner) {
        sectorDetailsVBox.getChildren().add(spinner);
    }

    public void setSeatSelectionListener(SeatSelectionListener seatSelectionListener) {
        this.seatSelectionListener = seatSelectionListener;
    }
}