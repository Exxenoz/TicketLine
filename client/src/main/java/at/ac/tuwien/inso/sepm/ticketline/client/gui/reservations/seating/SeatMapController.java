package at.ac.tuwien.inso.sepm.ticketline.client.gui.reservations.seating;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;

public class SeatMapController {

    @FXML
    private Canvas seatMapCanvas;

    private SeatSelectionListener seatSelectionListener;

    public SeatMapController(SeatSelectionListener seatSelectionListener) {
        this.seatSelectionListener = seatSelectionListener;
    }

    @FXML
    public void initialize() {

    }

    public void getSeatMap() {

    }

    public void drawSeatMap() {

    }

}
