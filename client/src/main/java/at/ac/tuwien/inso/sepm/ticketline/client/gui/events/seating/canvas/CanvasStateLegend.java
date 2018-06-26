package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.canvas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class CanvasStateLegend implements CanvasComponent {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    //Drawing
    public final static double WIDTH = 30;
    public final static double HEIGHT = 30;
    private final static double LEGEND_OFFSET_TOP = 80;
    private final static double LEGEND_OFFSET_LEFT = 40;
    private final static double ARC_WIDTH = 8;
    private final static double ARC_HEIGHT = 8;
    private final static double VERTICAL_ESTIMATE = 1.5;
    private final static double LINE_MARGIN = 2;
    private Paint paint;

    //Positioning
    private double xPos;
    private double yPos;

    //State
    private String text;
    private boolean crossed;

    public CanvasStateLegend(double xPos, double yPos, Color paint, String text, boolean crossed) {
        this.xPos = xPos;
        this.yPos = yPos + LEGEND_OFFSET_TOP;
        this.paint = paint;
        this.text = text;
        this.crossed = crossed;
    }

    @Override
    public void draw(GraphicsContext gc) {
        LOGGER.debug("Draw state legend");
        gc.setFill(this.paint);
        gc.setLineWidth(1);
        gc.fillRoundRect(xPos, yPos, WIDTH, HEIGHT, ARC_WIDTH, ARC_HEIGHT);

        if(crossed) {
            //Draw a crossed line to symbolize reservation state
            gc.setFill(Color.BLACK);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(2);
            gc.strokeLine(xPos + LINE_MARGIN, yPos + LINE_MARGIN, xPos + WIDTH - LINE_MARGIN,
                yPos + HEIGHT - LINE_MARGIN);
            gc.strokeLine(xPos + LINE_MARGIN, yPos + HEIGHT - LINE_MARGIN, xPos + WIDTH - LINE_MARGIN,
                yPos + LINE_MARGIN);
        }

        //And draw text with it
        gc.setFill(Color.BLACK);
        gc.fillText(text, xPos + LEGEND_OFFSET_LEFT, yPos + HEIGHT / VERTICAL_ESTIMATE);
    }

    @Override
    public boolean isClicked(double x, double y) {
        return false;
    }
}
