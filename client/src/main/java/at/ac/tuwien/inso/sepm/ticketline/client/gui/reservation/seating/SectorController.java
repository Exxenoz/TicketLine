package at.ac.tuwien.inso.sepm.ticketline.client.gui.reservation.seating;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

@Component
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