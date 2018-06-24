package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.canvas;

import at.ac.tuwien.inso.sepm.ticketline.rest.util.PriceUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class CanvasSectorLegend implements CanvasComponent {
    //Drawing
    public final static double WIDTH = 30;
    public final static double HEIGHT = 30;
    public final static double ESTIMATED_WIDTH = 120;
    public final static double OFFSET_LEFT = 14;
    public final static double REGULAR_MARGIN = 8;
    public final static int LEGEND_ROW_SIZE = 3;
    private final static double LEGEND_OFFSET_TOP = 80;
    private final static double LEGEND_OFFSET_LEFT = 40;
    private final static double ARC_WIDTH = 8;
    private final static double ARC_HEIGHT = 8;
    private final static double VERTICAL_ESTIMATE = 1.5;
    private Paint paint;

    //Positioning
    private double xPos;
    private double yPos;

    //State
    private Long price;

    public CanvasSectorLegend(double xPos, double yPos, Paint sectorPaint, Long price) {
        this.xPos = xPos;
        this.yPos = yPos + LEGEND_OFFSET_TOP;
        this.paint = sectorPaint;
        this.price = price;
    }

    @Override
    public void draw(GraphicsContext gc) {
        //Draw rectangle
        gc.setFill(this.paint);
        gc.setLineWidth(1);
        gc.fillRoundRect(xPos, yPos, WIDTH, HEIGHT, ARC_WIDTH, ARC_HEIGHT);

        //And draw text with it
        gc.setFill(Color.BLACK);
        gc.fillText(PriceUtils.priceToRepresentation(price), xPos + LEGEND_OFFSET_LEFT, yPos + HEIGHT / VERTICAL_ESTIMATE);
    }

    @Override
    public boolean isClicked(double x, double y) {
        return false;
    }


}
