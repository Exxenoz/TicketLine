package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.canvas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CanvasSeat implements CanvasComponent {

    private double xPos;
    private double yPos;

    public final static double WIDTH = 30;
    public final static double HEIGHT = 30;

    public final static double OFFSET_TOP = 20;
    public final static double REGULAR_MARGIN = 2;
    public final static double EXTRA_MARGIN = 4;

    private final static double ARC_WIDTH = 8;
    private final static double ARC_HEIGHT = 8;

    public CanvasSeat(double xPos, double yPos) {
        this.xPos = xPos;
        this.yPos = yPos + OFFSET_TOP;
    }

    @Override
    public void onClicked() {

    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.DARKGREEN);
        gc.setStroke(Color.DARKGREEN);
        gc.setLineWidth(1);
        gc.fillRoundRect(xPos, yPos, WIDTH, HEIGHT, ARC_WIDTH, ARC_HEIGHT);
    }

    @Override
    public void isInInterval(double topLeft, double lowerRight) {

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
}
