package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import javafx.fxml.FXML;
import org.springframework.stereotype.Component;

import static org.controlsfx.glyphfont.FontAwesome.Glyph.NEWSPAPER_ALT;

@Component
public class NewsCreateController {

    @FXML
    private TabHeaderController tabHeaderController;

    private final MainController mainController;
    private final NewsService newsService;

    public NewsCreateController(MainController mainController, NewsService newsService) {
        this.mainController = mainController;
        this.newsService = newsService;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(NEWSPAPER_ALT);
        tabHeaderController.setTitle("Create News");
    }
}
