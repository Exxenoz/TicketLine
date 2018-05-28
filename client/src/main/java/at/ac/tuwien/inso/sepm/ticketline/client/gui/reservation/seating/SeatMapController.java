package at.ac.tuwien.inso.sepm.ticketline.client.gui.reservation.seating;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.reservation.seating.canvas.CanvasLegend;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.sector.SectorDTO;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SeatMapController {

    private final static double SEAT_HEIGHT = 30;
    private final static double SEAT_WIDTH = 30;
    private final static double ARC_DIM = 8;
    private final static double MARGIN = 5;

    @FXML
    private Canvas seatMapCanvas;

    private GraphicsContext gc;

    private Map<SeatDTO, Canvas> seatMap;
    private List<CanvasLegend> legendList;

    private SeatSelectionListener seatSelectionListener;
    private PerformanceDTO performance;

    @FXML
    public void initialize() {
        this.gc = seatMapCanvas.getGraphicsContext2D();
    }

    public void drawSeatMap() {
        gc.fillText("TEST", 0,0 );
    }

    public void drawSeatMap(PerformanceDTO performance) {
        gc.fillText("TEST", 0,0 );
//        for(SectorDTO s: performance.getHall().getSectors()) {
//
//            for(int i = 0; i < s.getRows(); i++) {
//                for(int j = 0; j < s.getSeatsPerRow(); j++) {
//                    gc.setFill(Color.LAWNGREEN);
//                    gc.setStroke(Color.LAWNGREEN);
//                    gc.setLineWidth(2);
//                    gc.fillRoundRect(j * SEAT_WIDTH + j * MARGIN, j * SEAT_HEIGHT + j * MARGIN, SEAT_WIDTH, SEAT_HEIGHT,
//                        ARC_DIM, ARC_DIM);
//                }
//            }
//        }
    }

    public void fill(PerformanceDTO performance) {
        this.performance = performance;
    }

}
