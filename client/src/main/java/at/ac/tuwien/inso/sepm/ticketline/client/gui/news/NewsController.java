package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Separator;
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

    @FXML
    private NewsUnreadController newsUnreadController;

    @FXML
    private NewsUnreadController newsReadController;

    @FXML
    private NewsCreateController newsCreateController;

    @FXML
    private void initialize() { }

    public void loadNews() {
        newsUnreadController.loadNews();
        newsReadController.loadNews();
    }
}
