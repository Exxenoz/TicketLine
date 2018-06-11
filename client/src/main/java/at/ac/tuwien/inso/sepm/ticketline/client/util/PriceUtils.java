package at.ac.tuwien.inso.sepm.ticketline.client.util;

import java.text.NumberFormat;
import java.util.Locale;

public class PriceUtils {

    private final static NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());

    public static String priceToRepresentation(Long price) {
        return nf.format(price/100.0);
    }
}
