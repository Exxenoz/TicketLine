package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.canvas.CanvasLegend;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.canvas.CanvasSeat;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.sector.SectorDTO;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.*;

@Component
public class SeatMapController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    private Canvas seatMapCanvas;
    private GraphicsContext gc;

    private Map<SectorDTO, List<CanvasSeat>> sectorSeatMap;
    private List<CanvasLegend> legendList;

    private SeatSelectionListener seatSelectionListener;
    private PerformanceDTO performance;

    @FXML
    public void initialize() {
        this.gc = seatMapCanvas.getGraphicsContext2D();
        sectorSeatMap = new HashMap<>();
    }


    public void drawSeatMap(PerformanceDTO performance) {
        LOGGER.debug("Performance to draw seatmap for {}", performance.toString());
        for(SectorDTO sector: performance.getHall().getSectors()) {
            List<CanvasSeat> canvasSeats = new ArrayList<>(sector.getSeatsPerRow() * sector.getRows());
            for(int i = 0; i < sector.getSeatsPerRow(); i++) {
                for(int j = 0; j < sector.getRows(); j++) {

                    CanvasSeat canvasSeat = new CanvasSeat(
                        sector.getStartPositionX() * CanvasSeat.WIDTH + CanvasSeat.REGULAR_MARGIN * i + i * CanvasSeat.WIDTH,
                        sector.getStartPositionY() * CanvasSeat.HEIGHT + CanvasSeat.REGULAR_MARGIN * j + j * CanvasSeat.HEIGHT);
                    canvasSeats.add(canvasSeat);
                }
            }
            sectorSeatMap.put(sector, canvasSeats);
        }

        //finally draw
        for(Map.Entry<SectorDTO, List<CanvasSeat>> entry: sectorSeatMap.entrySet()) {
            for(CanvasSeat seat: entry.getValue()) {
                seat.draw(this.gc);
            }
        }
    }

    public void fill(PerformanceDTO performance) {
        this.performance = performance;
        drawSeatMap(performance);
    }

    public void setSeatSelectionListener(SeatSelectionListener seatSelectionListener) {
        this.seatSelectionListener = seatSelectionListener;
    }

}
