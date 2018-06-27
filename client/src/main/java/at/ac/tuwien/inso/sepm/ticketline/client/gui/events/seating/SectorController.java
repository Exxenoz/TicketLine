package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.sector.SectorRow;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.sector.SectorDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.PriceUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SectorController {

    private final static double INITIAL_OFFSET = 10.0;
    private final static double REGULAR_HEIGHT = 100.0;
    private final static double REGULAR_MARGIN = 20.0;

    private final static double SPACING = 100;
    private final static double INSET_TOP = 15;
    private final static double INSET_RIGHT = 30;
    private final static double INSET_BOTTOM = 15;
    private final static double INSET_LEFT = 30;
    private final static double SPINNER_WITH = 100;

    private final static int SPINNER_DEFAULT_VALUE = 0;

    //UI
    @FXML
    private VBox sectorDetailsVBox;

    @FXML
    private ScrollPane sectorScrollPane;

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
    public void initialize() {
    }

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
        for (SectorDTO s : performance.getHall().getSectors()) {
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
        hBox.setPadding(new Insets(INSET_TOP, INSET_RIGHT, INSET_BOTTOM, INSET_LEFT));
        hBox.setSpacing(SPACING);

        Label sectorLabel = createSectorLabel(count);
        addLabelToHBox(hBox, sectorLabel);

        //Create and add price label
        Label priceLabel = createPriceLabel(sectorDTO);
        addLabelToHBox(hBox, priceLabel);

        //Create and add spinner
        Spinner spinner = createSpinner();
        setupSpinner(spinner, sectorDTO);
        addSpinnerToHBox(hBox, spinner);

        addHBoxToVBox(hBox);
        //.. and return the created row!
        return new SectorRow(sectorDTO, sectorLabel, priceLabel, spinner);
    }

    public Label createSectorLabel(int count) {
        Label sectorLabel = new Label();
        sectorLabel.setText("Sector: " + count);
        return sectorLabel;
    }

    public Label createPriceLabel(SectorDTO sectorDTO) {
        Label priceLabel = new Label();
        priceLabel.setText(PriceUtils.priceToRepresentation(sectorDTO.getCategory().getBasePriceMod() * performance.getPrice()));
        return priceLabel;
    }

    public Spinner<Integer> createSpinner() {
        Spinner<Integer> spinner = new Spinner<>();
        spinner.setMinWidth(SPINNER_WITH);
        spinner.setMaxWidth(SPINNER_WITH);
        return spinner;
    }

    public void addLabelToHBox(HBox hBox, Label label) {
        hBox.getChildren().add(label);
    }

    public void addSpinnerToHBox(HBox hBox, Spinner<Integer> spinner) {
        hBox.getChildren().add(spinner);
    }

    public void addHBoxToVBox(HBox hBox) {
        sectorDetailsVBox.getChildren().add(hBox);
    }

    public void setSeatSelectionListener(SeatSelectionListener seatSelectionListener) {
        this.seatSelectionListener = seatSelectionListener;
    }

    public void setupSpinner(Spinner<Integer> spinner, SectorDTO sectorDTO) {
        //Find the maximum spinner value possible for this sector
        int maxValue = sectorDTO.getRows() * sectorDTO.getSeatsPerRow();

        //Then setup spinner accordingly
        SpinnerValueFactory<Integer> valueFactory =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(SPINNER_DEFAULT_VALUE, maxValue, SPINNER_DEFAULT_VALUE);
        spinner.setValueFactory(valueFactory);
        spinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                SeatDTO seatDTO = SeatDTO.Builder.aSeatDTO()
                    .withSector(sectorDTO)
                    .withPositionX(0)
                    .withPositionY(0)
                    .build();
                if(newValue > oldValue) {
                    seatSelectionListener.onSeatSelected(seatDTO);
                } else if (newValue < oldValue) {
                    seatSelectionListener.onSeatDeselected(seatDTO);
                }
            }
        });
    }

    /**
     * Checks for all javafx views if they are correctly initialized
     * to give us information about the controller state
     * @return true if everything is initialized, else false
     */
    public boolean isInitialized() {
        if(sectorDetailsVBox != null && sectorScrollPane != null) {
            return true;
        } else {
            return false;
        }
    }

    public void fillForReservationEdit(ReservationDTO reservationDTO) {
        Map<SectorDTO, Integer> map = getSeatCountMap(reservationDTO);

        for(Map.Entry<SectorDTO, Integer> entry: map.entrySet()) {
            for(SectorRow row: sectorRows) {
                if(row.getSectorDTO().equals(row)) {
                    row.setSpinnerAmount(entry.getValue());
                }
            }
        }
    }

    private Map<SectorDTO, Integer> getSeatCountMap(ReservationDTO reservationDTO) {
        Map<SectorDTO, Integer> sectorCountMap = new HashMap();
        for (SeatDTO s : reservationDTO.getSeats()) {
            SectorDTO sector = s.getSector();
            if (!sectorCountMap.containsKey(sector)) {
                sectorCountMap.put(s.getSector(), 0);
            } else {
                sectorCountMap.put(s.getSector(), sectorCountMap.get(s.getSector()) + 1);
            }
        }
        return sectorCountMap;
    }
}