package at.ac.tuwien.inso.sepm.ticketline.client.gui.reservations.seating;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.VBox;

public class SectorController {

    @FXML
    private VBox sectorDetailsVBox;

    @FXML
    private ScrollBar sectorDetailsScrollBar;

    private SeatSelectionListener seatSelectionListener;

    public SectorController(SeatSelectionListener seatSelectionListener) {
        this.seatSelectionListener = seatSelectionListener;
    }
}