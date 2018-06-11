package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating.canvas;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class CanvasColorUtil {

    public final static Long LOW_RANGE_CATEGORY_PRICE = 2000L;
    public final static Long MID_RANGE_CATEGORY_PRICE = 4000L;
    public final static Long HIGH_RANGE_CATEGORY_PRICE = 6000L;
    public final static Long TOP_RANGE_CATEGORY_PRICE = 80000L;
    /**
     * Color coding the price ranges for the seatmap canvas
     * @param price
     * @return
     */
    public static Paint priceToPaint(Long price) {
        if(price.intValue() >= 0 && price.longValue() <= LOW_RANGE_CATEGORY_PRICE) {
            return Color.LAWNGREEN;
        } else if(price.intValue() > LOW_RANGE_CATEGORY_PRICE && price.intValue() <= MID_RANGE_CATEGORY_PRICE) {
            return Color.DARKGREEN;
        } else if(price.intValue() > MID_RANGE_CATEGORY_PRICE && price.intValue() <= HIGH_RANGE_CATEGORY_PRICE) {
            return Color.ORANGE;
        } else if(price.intValue() > HIGH_RANGE_CATEGORY_PRICE && price.intValue() <= TOP_RANGE_CATEGORY_PRICE) {
            return Color.TOMATO;
        } else if(price.intValue() > TOP_RANGE_CATEGORY_PRICE) {
            return Color.CRIMSON;
        } else {
            return Color.DARKRED;
        }
    }
}
