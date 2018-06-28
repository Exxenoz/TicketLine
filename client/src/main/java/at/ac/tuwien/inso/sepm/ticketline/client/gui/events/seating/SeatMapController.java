package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.canvas.CanvasColorUtil;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.canvas.CanvasSeat;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.canvas.CanvasSectorLegend;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.canvas.CanvasStateLegend;
import at.ac.tuwien.inso.sepm.ticketline.client.service.SeatMapService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.sector.SectorDTO;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SeatMapController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final static double VERTICAL_ESTIMATE = 1.5;
    private final static double OFFSET_LEFT = 2.5;
    private final static int STATE_LEGEND_SIZE = 2;

    private SeatMapService seatMapService;

    @FXML
    private Canvas seatMapCanvas;
    @FXML
    private ScrollPane seatMapScrollPane;

    private GraphicsContext gc;
    private Map<SectorDTO, List<CanvasSeat>> sectorSeatMap;
    private List<CanvasSectorLegend> sectorLegendList;
    private SeatSelectionListener seatSelectionListener;

    private PerformanceDTO performance;
    private List<ReservationDTO> reservations;

    public SeatMapController(SeatMapService seatMapService) {
        this.seatMapService = seatMapService;
    }

    @FXML
    public void initialize() {
        LOGGER.info("Initialize SeatMapController");
        this.gc = seatMapCanvas.getGraphicsContext2D();
        sectorSeatMap = new HashMap<>();
    }

    public void drawSeatMap(PerformanceDTO performance, List<ReservationDTO> reservationDTOS) {
        LOGGER.debug("Performance to draw seatmap for {}", performance.toString());
        for (SectorDTO sector : performance.getHall().getSectors()) {
            Long price = sector.getCategory().getBasePriceMod() * performance.getPrice();

            LOGGER.debug("Draw the sector {}", sector);
            //First draw all sectors and seats of this hall and check if these seats are in any reservation
            List<CanvasSeat> canvasSeats = new ArrayList<>(sector.getSeatsPerRow() * sector.getRows());
            for (int i = 0; i < sector.getSeatsPerRow(); i++) {
                for (int j = 0; j < sector.getRows(); j++) {
                    //Draw the row labels
                    gc.fillText("" + (j + 1), OFFSET_LEFT, (CanvasSeat.HEIGHT / VERTICAL_ESTIMATE)
                        + (sector.getStartPositionY() * CanvasSeat.HEIGHT + CanvasSeat.REGULAR_MARGIN * j + j * CanvasSeat.HEIGHT)
                        + (CanvasSeat.OFFSET_TOP));

                    boolean isReserved = false;
                    //Check if this seat might be in a reservation
                    for (ReservationDTO r : reservationDTOS) {
                        for (SeatDTO s : r.getSeats()) {
                            if (s.getSector().getId().equals(sector.getId())
                                && s.getPositionX() == i
                                && s.getPositionY() == j) {
                                LOGGER.debug("The Seat {} is  reserved", s);
                                isReserved = true;
                            }
                        }
                    }
                    CanvasSeat canvasSeat = new CanvasSeat(i, j,
                        sector.getStartPositionX() * CanvasSeat.WIDTH + CanvasSeat.REGULAR_MARGIN * i + i * CanvasSeat.WIDTH,
                        sector.getStartPositionY() * CanvasSeat.HEIGHT + CanvasSeat.REGULAR_MARGIN * j + j * CanvasSeat.HEIGHT,
                        CanvasColorUtil.priceToPaint(price), isReserved);
                    canvasSeats.add(canvasSeat);
                }
            }
            sectorSeatMap.put(sector, canvasSeats);
        }

        //Finally draw
        LOGGER.debug("Draw Seats");
        for (Map.Entry<SectorDTO, List<CanvasSeat>> entry : sectorSeatMap.entrySet()) {
            for (CanvasSeat seat : entry.getValue()) {
                seat.draw(this.gc);
            }
        }

        //Draw the legend
        drawLegend(gc);

        //Add mouse click event handler
        LOGGER.debug("Adding MouseClickEventListener");
        seatMapCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            double eventX = event.getX();
            double eventY = event.getY();

            for (Map.Entry<SectorDTO, List<CanvasSeat>> entry : sectorSeatMap.entrySet()) {
                for (CanvasSeat seat : entry.getValue()) {
                    // Check if the seat was clicked
                    if (seat.isClicked(eventX, eventY)) {
                        if (!seat.isAlreadyReserved()) {

                            //Setup the corresponding seat with full data
                            SeatDTO seatDTO;
                            if (seat.getSeatID() != null) {
                                seatDTO = SeatDTO.Builder.aSeatDTO()
                                    .withId(seat.getSeatID())
                                    .withPositionX(seat.getPlanX())
                                    .withPositionY(seat.getPlanY())
                                    .withSector(entry.getKey())
                                    .build();
                            } else {
                                seatDTO = SeatDTO.Builder.aSeatDTO()
                                    .withPositionX(seat.getPlanX())
                                    .withPositionY(seat.getPlanY())
                                    .withSector(entry.getKey())
                                    .build();
                            }
                            if (seat.isSelected()) {
                                seat.setSelected(false);
                                seat.drawDeselectedState(gc);
                                seatSelectionListener.onSeatDeselected(seatDTO);
                            } else {
                                seat.setSelected(true);
                                seat.drawSelectedState(gc);
                                //Notify listener about selected seat
                                seatSelectionListener.onSeatSelected(seatDTO);
                            }
                        }
                    }
                }
            }
        });
        LOGGER.debug("The SeatPlan was drawn successfully");
    }

    public void drawLegend(GraphicsContext gc) {
        LOGGER.debug("Draw the legend");
        List<SectorDTO> sectors = performance.getHall().getSectors();
        sectorLegendList = new ArrayList<>(sectors.size());

        double currentY = seatMapService.findMaxSectorY(sectors, false) * CanvasSectorLegend.HEIGHT;
        double currentX = CanvasSectorLegend.OFFSET_LEFT;

        for (int i = 0; i < sectors.size() + STATE_LEGEND_SIZE; i++) {
            if (i < sectors.size()) {
                SectorDTO s = performance.getHall().getSectors().get(i);
                //Determine price of sector
                Long price = s.getCategory().getBasePriceMod() * performance.getPrice();
                CanvasSectorLegend c = new CanvasSectorLegend(currentX, currentY, CanvasColorUtil.priceToPaint(price), price);
                sectorLegendList.add(c);
                c.draw(gc);
            } else {
                if (i < sectors.size() + STATE_LEGEND_SIZE) {
                    if (i == sectors.size()) {
                        //Adding legend for reserved state
                        CanvasStateLegend canvasStateLegend = new CanvasStateLegend(currentX, currentY,
                            CanvasSeat.ALREADY_RESERVED_COLOR,
                            BundleManager.getBundle().getString("events.seating.canvas.state.reserved"), true);
                        canvasStateLegend.draw(gc);
                    } else if (i == sectors.size() + 1) {
                        //Add legend for selected state
                        CanvasStateLegend canvasStateLegend = new CanvasStateLegend(currentX, currentY,
                            CanvasSeat.SELECTED_COLOR,
                            BundleManager.getBundle().getString("events.seating.canvas.state.selected"), false);
                        canvasStateLegend.draw(gc);
                    }
                }
            }

            //Create a new legend row for every few items
            if (i % CanvasSectorLegend.LEGEND_ROW_SIZE == CanvasSectorLegend.LEGEND_ROW_SIZE - 1) {
                currentY += CanvasSectorLegend.HEIGHT + CanvasSectorLegend.REGULAR_MARGIN;
                currentX = CanvasSectorLegend.OFFSET_LEFT;
            } else {
                currentX = CanvasSectorLegend.OFFSET_LEFT + ((i + 1) % CanvasSectorLegend.LEGEND_ROW_SIZE) * CanvasSectorLegend.ESTIMATED_WIDTH;
            }
        }
        LOGGER.debug("Legend was drawn successfully");
    }

    public void fill(PerformanceDTO performance, List<ReservationDTO> reservationDTOS) {
        this.performance = performance;
        this.reservations = reservationDTOS;

        resizeCanvas(performance);
        drawSeatMap(performance, reservationDTOS);
    }

    public void fillForReservationEdit(ReservationDTO reservationDTO) {
        //We have to find the corresponding reservation in the seatmap, and make those seats editable
        for (SeatDTO s : reservationDTO.getSeats()) {
            LOGGER.debug("Seat {} is reserved, draw as such", s);
            for (Map.Entry<SectorDTO, List<CanvasSeat>> entry : sectorSeatMap.entrySet()) {
                if (s.getSector().getId() == entry.getKey().getId()) {
                    for (CanvasSeat cs : entry.getValue()) {
                        if (cs.getPlanX() == s.getPositionX()
                            && (cs.getPlanY() == s.getPositionY())) {
                            cs.setAlreadyReserved(false);
                            cs.setSelected(true);
                            cs.drawSelectedState(this.gc);

                            //Set the known id of the reserved seat too
                            cs.setSeatID(s.getId());
                        }
                    }
                }
            }
        }
    }

    public void setSeatSelectionListener(SeatSelectionListener seatSelectionListener) {
        this.seatSelectionListener = seatSelectionListener;
    }

    public void resizeCanvas(PerformanceDTO performanceDTO) {
        double estimatedWidth = 0.0f;
        double estimatedHeight = 0.0f;

        estimatedHeight += seatMapService.findMaxSectorY(performanceDTO.getHall().getSectors(), true) * CanvasSeat.HEIGHT;
        //Add extra height for sector legend
        estimatedHeight += ((performanceDTO.getHall().getSectors().size() / CanvasSectorLegend.LEGEND_ROW_SIZE) + 1) *
            (CanvasSectorLegend.HEIGHT + CanvasSectorLegend.REGULAR_MARGIN);

        estimatedWidth += seatMapService.findMaxSectorX(performanceDTO.getHall().getSectors(), true) * CanvasSeat.WIDTH;

        seatMapCanvas.setHeight(estimatedHeight);
        seatMapCanvas.setWidth(estimatedWidth);
    }

    /**
     * Checks for all javafx views if they are correctly initialized
     * to give us information about the controller state
     * @return true if everything is initialized, else false
     */
    public boolean isInitialized() {
        return seatMapCanvas != null && seatMapScrollPane != null;
    }
}
