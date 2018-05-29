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
        if(price.intValue() >= 0 && price.intValue() <= 20) {
            return Color.LAWNGREEN;
        } else if(price.intValue() > 20 && price.intValue() <= 40) {
            return Color.DARKGREEN;
        } else if(price.intValue() > 40 && price.intValue() <= 60) {
            return Color.ORANGE;
        } else if(price.intValue() > 60 && price.intValue() <= 80) {
            return Color.TOMATO;
        } else if(price.intValue() > 80) {
            return Color.CRIMSON;
        } else {
            return Color.DARKRED;
        }
    }
}
