package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.canvas;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.math.BigDecimal;

public class CanvasColorUtil {

    /**
     * Color coding the price ranges for the seatmap canvas
     * @param price
     * @return
     */
    public static Paint priceToPaint(BigDecimal price) {
        if(price.intValue() >= 0 && price.intValue() <= 10) {
            return Color.LAWNGREEN;
        } else if(price.intValue() > 10 && price.intValue() <= 30) {
            return Color.DARKGREEN;
        } else if(price.intValue() > 30 && price.intValue() <= 70) {
            return Color.BLUEVIOLET;
        } else if(price.intValue() > 70) {
            return Color.BLUEVIOLET;
        } else {
            return Color.DARKCYAN;
        }
    }
}
