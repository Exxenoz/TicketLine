package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.service.AuthenticationInformationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

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
        //LOGGER.debug(authenticationInformationService.getCurrentAuthenticationTokenInfo().get().getRoles() +"");
        if (authenticationInformationService.getCurrentAuthenticationTokenInfo().get().getRoles().contains("ROLE_ADMIN")) {
            createNewsTab.setDisable(false);
        }
    }
}
