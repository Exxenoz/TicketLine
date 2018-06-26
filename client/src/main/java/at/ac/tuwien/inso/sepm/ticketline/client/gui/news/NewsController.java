package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.AuthenticationInformationService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Iterator;
import java.util.List;

import static javafx.scene.control.ProgressIndicator.INDETERMINATE_PROGRESS;
import static org.controlsfx.glyphfont.FontAwesome.Glyph.NEWSPAPER_ALT;

@Component
public class NewsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    private NewsUnreadController newsUnreadController;

    @FXML
    private NewsReadController newsReadController;

    @FXML
    private NewsCreateController newsCreateController;

    @FXML
    public Tab unreadNewsTab;

    @FXML
    public Tab readNewsTab;

    @FXML
    public Tab createNewsTab;

    private AuthenticationInformationService authenticationInformationService;

    public NewsController(AuthenticationInformationService authenticationInformationService) {
        this.authenticationInformationService = authenticationInformationService;
    }

    @FXML
    private void initialize() {
        initI18N();
    }

    private void initI18N() {
        unreadNewsTab.textProperty().bind(BundleManager.getStringBinding("news.main.tab1"));
        readNewsTab.textProperty().bind(BundleManager.getStringBinding("news.main.tab2"));
        createNewsTab.textProperty().bind(BundleManager.getStringBinding("news.main.tab3"));
    }

    public void loadNews() {
        newsUnreadController.loadNews();
        newsReadController.loadNews();
        LOGGER.debug(authenticationInformationService.getCurrentAuthenticationTokenInfo().get().getRoles() +"");
        if (authenticationInformationService.getCurrentAuthenticationTokenInfo().get().getRoles().contains("ROLE_ADMIN")) {
            createNewsTab.setDisable(false);
        }
    }
}
