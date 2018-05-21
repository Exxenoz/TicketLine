package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import javafx.fxml.FXML;
import org.springframework.stereotype.Component;

import static org.controlsfx.glyphfont.FontAwesome.Glyph.CALENDAR_ALT;

@Component
public class EventController {

    @FXML
    private EventTop10Controller eventTop10Controller;

    @FXML
    private EventSearchController eventSearchController;

    @FXML
    private void initialize() { }

    public void loadData() {
        eventSearchController.loadData();
        eventTop10Controller.loadData();
    }
}
