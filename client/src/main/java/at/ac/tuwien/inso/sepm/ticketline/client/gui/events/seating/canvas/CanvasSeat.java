package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.canvas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CanvasSeat implements CanvasComponent {

    private int planX;
    private int planY;

    private double xPos;
    private double yPos;

    public final static double WIDTH = 30;
    public final static double HEIGHT = 30;

    public final static double OFFSET_LEFT = 14;
    public final static double OFFSET_TOP = 20;
    public final static double REGULAR_MARGIN = 2;
    public final static double EXTRA_MARGIN = 4;

    private final static double ARC_WIDTH = 8;
    private final static double ARC_HEIGHT = 8;

    private boolean selected;

    public CanvasSeat(int planX, int planY, double xPos, double yPos) {
        this.xPos = xPos + OFFSET_LEFT;
        this.yPos = yPos + OFFSET_TOP;
    }

    @Override
    public boolean isClicked(double x, double y) {
        if((x > xPos && x < xPos + WIDTH) && (y > yPos && y < yPos + HEIGHT)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.DARKGREEN);
        gc.setStroke(Color.DARKGREEN);
        gc.setLineWidth(1);
        gc.fillRoundRect(xPos, yPos, WIDTH, HEIGHT, ARC_WIDTH, ARC_HEIGHT);
    }

    public void drawSelected(GraphicsContext gc) {
        selected = true;
        gc.setFill(Color.CHARTREUSE);
        gc.fillRoundRect(xPos, yPos, WIDTH, HEIGHT, ARC_WIDTH, ARC_HEIGHT);
    }

    public void drawDeselected(GraphicsContext gc) {
        selected = false;
        gc.setFill(Color.DARKGREEN);
        gc.fillRoundRect(xPos, yPos, WIDTH, HEIGHT, ARC_WIDTH, ARC_HEIGHT);
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
        return planX;
    }

    public void setPlanX(int planX) {
        this.planX = planX;
    }

    public int getPlanY() {
        return planY;
    }

    public void setPlanY(int planY) {
        this.planY = planY;
    }
}
