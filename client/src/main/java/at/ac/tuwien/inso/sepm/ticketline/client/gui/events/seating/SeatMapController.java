package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.canvas.CanvasColorUtil;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.canvas.CanvasLegend;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.canvas.CanvasSeat;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.SeatMapService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.implementation.SimpleSeatMapService;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.sector.SectorDTO;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.List;

@Component
public class SeatMapController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final static double VERTICAL_ESTIMATE = 1.5;
    private final static double OFFSET_LEFT = 2.5;

    private SeatMapService seatMapService;

    @FXML
    private Canvas seatMapCanvas;

    @FXML
    private ScrollPane seatMapScrollPane;

    private GraphicsContext gc;

    private Map<SectorDTO, List<CanvasSeat>> sectorSeatMap;
    private List<CanvasLegend> legendList;

    private SeatSelectionListener seatSelectionListener;
    private PerformanceDTO performance;

    public SeatMapController(SeatMapService seatMapService) {
        this.seatMapService = seatMapService;
    }
    @FXML
    public void initialize() {
        this.gc = seatMapCanvas.getGraphicsContext2D();
        sectorSeatMap = new HashMap<>();
    }

    public void drawSeatMap(PerformanceDTO performance) {
        LOGGER.debug("Performance to draw seatmap for {}", performance.toString());
        for(SectorDTO sector: performance.getHall().getSectors()) {
            Long price = sector.getCategory().getBasePriceMod() * performance.getPrice();

            List<CanvasSeat> canvasSeats = new ArrayList<>(sector.getSeatsPerRow() * sector.getRows());
            for(int i = 0; i < sector.getSeatsPerRow(); i++) {
                for(int j = 0; j < sector.getRows(); j++) {
                    //Draw the row labels
                    gc.fillText("" + (j + 1), OFFSET_LEFT, (CanvasSeat.HEIGHT / VERTICAL_ESTIMATE)
                        + (sector.getStartPositionY() * CanvasSeat.HEIGHT + CanvasSeat.REGULAR_MARGIN * j + j * CanvasSeat.HEIGHT)
                        + (CanvasSeat.OFFSET_TOP));

                    CanvasSeat canvasSeat = new CanvasSeat(i, j,
                        sector.getStartPositionX() * CanvasSeat.WIDTH + CanvasSeat.REGULAR_MARGIN * i + i * CanvasSeat.WIDTH,
                        sector.getStartPositionY() * CanvasSeat.HEIGHT + CanvasSeat.REGULAR_MARGIN * j + j * CanvasSeat.HEIGHT,
                        CanvasColorUtil.priceToPaint(price));
                    canvasSeats.add(canvasSeat);
                }
            }
            sectorSeatMap.put(sector, canvasSeats);
        }

        //Finally draw
        for(Map.Entry<SectorDTO, List<CanvasSeat>> entry: sectorSeatMap.entrySet()) {
            for(CanvasSeat seat: entry.getValue()) {
                seat.draw(this.gc);
            }
        }

        //Draw the legend
        drawLegend(gc);

        seatMapCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            double eventX = event.getX();
            double eventY = event.getY();

            for(Map.Entry<SectorDTO, List<CanvasSeat>> entry: sectorSeatMap.entrySet()) {
                for(CanvasSeat seat: entry.getValue()) {
                    // Check if the seat was clicked
                    if(seat.isClicked(eventX, eventY)) {
                        if(seat.isSelected()) {
                            seat.drawDeselected(gc);
                            seatSelectionListener.onSeatDeselected(new SeatDTO(seat.getPlanX(), seat.getPlanY(),
                                entry.getKey()));
                        } else {
                            seat.drawSelected(gc);
                            seatSelectionListener.onSeatSelected(new SeatDTO(seat.getPlanX(), seat.getPlanY(),
                                entry.getKey()));
                        }
                    }
                }
            }
        });
    }

    public void drawLegend(GraphicsContext gc) {
        List<SectorDTO> sectors = performance.getHall().getSectors();
        legendList = new ArrayList<>(sectors.size());

        double currentY = seatMapService.findMaxSectorY(sectors, false) * CanvasLegend.HEIGHT;
        double currentX = CanvasLegend.OFFSET_LEFT;

        for(int i = 0; i < sectors.size(); i++) {
            SectorDTO s = performance.getHall().getSectors().get(i);
            //Determine price of sector
            Long price = s.getCategory().getBasePriceMod() * performance.getPrice();
            CanvasLegend c = new CanvasLegend(currentX, currentY, CanvasColorUtil.priceToPaint(price), price);
            legendList.add(c);
            c.draw(gc);

            //Create a new legend row for every few items
            if(i % CanvasLegend.LEGEND_ROW_SIZE == 0 && i > 0) {
                currentY += CanvasLegend.HEIGHT + CanvasLegend.REGULAR_MARGIN;
                currentX = CanvasLegend.OFFSET_LEFT;
            } else {
                currentX = CanvasLegend.OFFSET_LEFT + (i % CanvasLegend.LEGEND_ROW_SIZE) * CanvasLegend.ESTIMATED_WIDTH;
            }
        }
    }

    public void fill(PerformanceDTO performance) {
        this.performance = performance;
        resizeCanvas(performance);
        drawSeatMap(performance);
    }

    public void setSeatSelectionListener(SeatSelectionListener seatSelectionListener) {
        this.seatSelectionListener = seatSelectionListener;
    }

    public void resizeCanvas(PerformanceDTO performanceDTO) {
        double estimatedWidth = 0.0f;
        double estimatedHeight = 0.0f;

        estimatedHeight += seatMapService.findMaxSectorY(performanceDTO.getHall().getSectors(), true) * CanvasSeat.HEIGHT;
        //Add extra height for sector legend
        estimatedHeight += ((performanceDTO.getHall().getSectors().size() / CanvasLegend.LEGEND_ROW_SIZE) + 1) *
            (CanvasLegend.HEIGHT + CanvasLegend.REGULAR_MARGIN);

        estimatedWidth += seatMapService.findMaxSectorX(performanceDTO.getHall().getSectors(), true) * CanvasSeat.WIDTH;

        seatMapCanvas.setHeight(estimatedHeight);
        seatMapCanvas.setWidth(estimatedWidth);
    }
}
