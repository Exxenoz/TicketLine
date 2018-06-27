package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.canvas;

import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class CanvasSeat implements CanvasComponent {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    //Drawing
    public final static double WIDTH = 30.0;
    public final static double HEIGHT = 30;
    public final static double OFFSET_LEFT = 14;
    public final static double OFFSET_TOP = 20;
    public final static double REGULAR_MARGIN = 2;
    private final static double ARC_WIDTH = 8;
    private final static double ARC_HEIGHT = 8;
    private final static double LINE_MARGIN = 2;

    public final static Color SELECTED_COLOR = Color.ROYALBLUE;
    public final static Color ALREADY_RESERVED_COLOR = Color.GREY;
    private Paint paint;

    private SeatDTO seatDTO;
    private double xPos;
    private double yPos;

    //Seat state
    private boolean selected;
    private boolean alreadyReserved;

    public CanvasSeat(int planX, int planY, double xPos, double yPos, Paint paint, boolean alreadyReserved) {

        this.seatDTO = SeatDTO.Builder.aSeatDTO()
            .withPositionX(planX)
            .withPositionY(planY)
            .build();

        this.xPos = xPos + OFFSET_LEFT;
        this.yPos = yPos + OFFSET_TOP;
        this.paint = paint;
        this.alreadyReserved = alreadyReserved;
    }

    @Override
    public boolean isClicked(double x, double y) {
        return (x > xPos && x < xPos + WIDTH) && (y > yPos && y < yPos + HEIGHT);
    }

    @Override
    public void draw(GraphicsContext gc) {
        if(isAlreadyReserved()) {
            //Draw seat background as usual
           drawAlreadyReservedState(gc);
        } else {
            gc.setFill(this.paint);
            gc.setLineWidth(1);
            gc.fillRoundRect(xPos, yPos, WIDTH, HEIGHT, ARC_WIDTH, ARC_HEIGHT);
        }
    }

    public void drawSelectedState(GraphicsContext gc) {
        LOGGER.debug("Draw selected seat with {}", toString());
        gc.setFill(Color.ROYALBLUE);
        gc.fillRoundRect(xPos, yPos, WIDTH, HEIGHT, ARC_WIDTH, ARC_HEIGHT);
    }

    public void drawDeselectedState(GraphicsContext gc) {
        LOGGER.debug("Draw deselected seat with {}", toString());
        gc.setFill(this.paint);
        gc.fillRoundRect(xPos, yPos, WIDTH, HEIGHT, ARC_WIDTH, ARC_HEIGHT);
    }

    public void drawAlreadyReservedState(GraphicsContext gc) {
        LOGGER.debug("Draw already reserved seat with {}", toString());
        gc.setFill(ALREADY_RESERVED_COLOR);
        gc.setLineWidth(1);
        gc.fillRoundRect(xPos, yPos, WIDTH, HEIGHT, ARC_WIDTH, ARC_HEIGHT);

        //Draw a crossed line to symbolize reservation state
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeLine(xPos + LINE_MARGIN, yPos + LINE_MARGIN, xPos + WIDTH - LINE_MARGIN,
            yPos + HEIGHT - LINE_MARGIN);
        gc.strokeLine(xPos + LINE_MARGIN, yPos + HEIGHT - LINE_MARGIN, xPos + WIDTH - LINE_MARGIN,
            yPos + LINE_MARGIN);
    }


    public double getxPos() {
        return xPos;
    }

    public void setxPos(double xPos) {
        this.xPos = xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public void setyPos(double yPos) {
        this.yPos = yPos;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getPlanX() {
        return seatDTO.getPositionX();
    }

    public void setPlanX(int planX) {
        seatDTO.setPositionX(planX);
    }

    public int getPlanY() {
        return seatDTO.getPositionY();
    }

    public void setPlanY(int planY) {
        seatDTO.setPositionY(planY);
    }

    public boolean isAlreadyReserved() {
        return alreadyReserved;
    }

    public void setAlreadyReserved(boolean alreadyReserved) {
        this.alreadyReserved = alreadyReserved;
    }

    public void setSeatID(Long seatID) {
        seatDTO.setId(seatID);
    }

    public Long getSeatID() {
        return seatDTO.getId();
    }

    @Override
    public String toString() {
        return "CanvasSeat{" +
            "paint=" + paint +
            ", seatDTO=" + seatDTO +
            ", xPos=" + xPos +
            ", yPos=" + yPos +
            ", selected=" + selected +
            ", alreadyReserved=" + alreadyReserved +
            '}';
    }
}
