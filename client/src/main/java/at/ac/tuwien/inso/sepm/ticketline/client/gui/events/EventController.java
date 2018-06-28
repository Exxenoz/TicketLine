package at.ac.tuwien.inso.sepm.ticketline.client.gui.events;

import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Locale;

@Component
public class EventController {

    private final static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @FXML
    public Tab mainTab1;

    @FXML
    public Tab mainTab2;

    @FXML
    private EventTop10Controller eventTop10Controller;

    @FXML
    private EventSearchController eventSearchController;

    @FXML
    private void initialize() {
        LOGGER.info("Initialize EventController");
        initI18N();
    }

    private void initI18N() {
        mainTab1.textProperty().bind(BundleManager.getStringBinding("events.main.tab1"));
        mainTab2.textProperty().bind(BundleManager.getStringBinding("events.main.tab2"));
    }

    public void loadData() {
        eventSearchController.loadData();
        eventTop10Controller.loadData();
    }

    public void onChangeLanguage(Locale language) {
        eventTop10Controller.onChangeLanguage(language);
    }
}
