package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static javafx.scene.control.ProgressIndicator.INDETERMINATE_PROGRESS;
import static org.controlsfx.glyphfont.FontAwesome.Glyph.NEWSPAPER_ALT;

@Component
public class NewsUnreadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    public ColumnConstraints column1;

    @FXML
    public ColumnConstraints column2;

    @FXML
    public WebView webview;

    @FXML
    private VBox vbNewsElements;

    @FXML
    private TabHeaderController tabHeaderController;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final NewsService newsService;

    private SimpleNewsDTO currentlySelectedNews;
    private List<SimpleNewsDTO> loadedNews;
    ObservableList<Node> vbNewsBoxChildren;

    public NewsUnreadController(MainController mainController, SpringFxmlLoader springFxmlLoader, NewsService newsService) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.newsService = newsService;
        currentlySelectedNews = null;
        loadedNews = new ArrayList<>();
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(NEWSPAPER_ALT);
        tabHeaderController.setTitle("News");
        webview.getEngine().setJavaScriptEnabled(false);
    }

    public void loadNews() {
        vbNewsBoxChildren = vbNewsElements.getChildren();
        vbNewsBoxChildren.clear();
        loadedNews.clear();
        final var task = new Task<List<SimpleNewsDTO>>() {
            @Override
            protected List<SimpleNewsDTO> call() throws DataAccessException {
                return newsService.findAll();
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                int i = 0;
                for (Iterator<SimpleNewsDTO> iterator = getValue().iterator(); iterator.hasNext(); ) {
                    SimpleNewsDTO news = iterator.next();
                    SpringFxmlLoader.Wrapper<NewsElementController> wrapper =
                        springFxmlLoader.loadAndWrap("/fxml/news/newsElement.fxml");
                    wrapper.getLoadedObject().setId("unreadNews" + i);
                    wrapper.getController().initializeData(news);
                    vbNewsBoxChildren.add(wrapper.getLoadedObject());
                    if (iterator.hasNext()) {
                        Separator separator = new Separator();
                        vbNewsBoxChildren.add(separator);
                        i++;
                    }
                }
                loadedNews = getValue();
            }

            @Override
            protected void failed() {
                super.failed();
                JavaFXUtils.createExceptionDialog(getException(),
                    vbNewsElements.getScene().getWindow()).showAndWait();
            }
        };
        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(running ? INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();
    }

    public void toggleDetailView(VBox vbox, int id) {
        for(Node node : vbNewsBoxChildren) {
            node.getStyleClass().remove("vbox-selected");
        }

        SimpleNewsDTO clickedNews = loadedNews.get(id);
        if(clickedNews.equals(currentlySelectedNews)) {
            column1.setPercentWidth(100);
            column2.setPercentWidth(0);
            currentlySelectedNews = null;
        } else {
            column1.setPercentWidth(30);
            column2.setPercentWidth(70);
            currentlySelectedNews = clickedNews;
            vbox.getStyleClass().add("vbox-selected");
            fillDetailView();
        }
    }

    private void fillDetailView() {
        DetailedNewsDTO detailedNewsDTO = null;
        try {
            detailedNewsDTO = newsService.find(currentlySelectedNews.getId());
        } catch (DataAccessException e) {
            // TODO: throw error dialog
        }

        WebEngine webEngine = webview.getEngine();
        String html = detailedNewsDTO.getText();
        if(html.contains("contenteditable=\"true\"")){
            html = html.replace("contenteditable=\"true\"", "contenteditable=\"false\" bgcolor=F4F4F4");
        }

        webEngine.loadContent(html);
    }
}
