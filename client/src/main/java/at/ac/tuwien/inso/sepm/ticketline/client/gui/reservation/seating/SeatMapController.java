package at.ac.tuwien.inso.sepm.ticketline.client.gui.reservation.seating;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.reservation.seating.canvas.CanvasLegend;
import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SeatMapController {

    @FXML
    private Canvas seatMapCanvas;

    private GraphicsContext graphicsContext;

    private Map<SeatDTO, Canvas> seatMap;
    private List<CanvasLegend> legendList;

    private SeatSelectionListener seatSelectionListener;

    @FXML
    public void initialize() {
        this.graphicsContext = seatMapCanvas.getGraphicsContext2D();
        drawSeatMap();
    }

    public void fetchSeatMap() {

    }

    public void


    public void drawSeatMap() {
        this.graphicsContext.fillText("test", 10, 10);

        this.graphicsContext.dra
    }

}
