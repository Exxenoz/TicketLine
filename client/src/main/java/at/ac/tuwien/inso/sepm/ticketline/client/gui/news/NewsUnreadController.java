package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.ByteArrayInputStream;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static javafx.scene.control.ProgressIndicator.INDETERMINATE_PROGRESS;
import static org.controlsfx.glyphfont.FontAwesome.Glyph.NEWSPAPER_ALT;

@Component
public class NewsUnreadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final int NEWS_PER_PAGE = 50;
    public static final int FIRST_NEWS_LIST_PAGE = 0;

    @FXML
    public ColumnConstraints column1;

    @FXML
    public ColumnConstraints column2;

    @FXML
    public Label titleLabel;

    @FXML
    public WebView webView;

    @FXML
    public ImageView imageView;

    @FXML
    public ScrollPane detailScrollPane;

    @FXML
    public ScrollPane simpleNewsScrollPane;

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
    private int page = 0;
    private int totalPages = 1;

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
        tabHeaderController.setTitle(BundleManager.getBundle().getString("news.header.unread"));
        webView.getEngine().setJavaScriptEnabled(false);
    }

    public void loadNews() {
        vbNewsBoxChildren = vbNewsElements.getChildren();
        vbNewsBoxChildren.clear();

        final ScrollBar scrollBar = getVerticalScrollbar();
        if (scrollBar != null) {
            scrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
                double scrollValue = newValue.doubleValue();
                if (scrollValue == scrollBar.getMax() && (page + 1) < totalPages) {
                    double targetValue = scrollValue * loadedNews.size();
                    loadNewsList(page + 1);
                    scrollBar.setValue(targetValue / loadedNews.size());
                }
            });

            scrollBar.visibleProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (newValue == false) {
                    // Scrollbar is invisible, load pages until scrollbar is shown again
                    loadNewsList(page + 1);
                }
            });
        }

        loadNewsList(FIRST_NEWS_LIST_PAGE);
    }

    public void reloadNews() {
        vbNewsBoxChildren = vbNewsElements.getChildren();
        vbNewsBoxChildren.clear();
        loadedNews.clear();
        loadNewsList(FIRST_NEWS_LIST_PAGE);
    }

    private boolean loadNewsList(int page) {
        if (page < 0 || page >= totalPages) {
            LOGGER.error("Could not load news table page, because page parameter is invalid: " + page);
            return false;
        }

        PageRequestDTO pageRequestDTO = null;
        pageRequestDTO = new PageRequestDTO(page, NEWS_PER_PAGE, Sort.Direction.ASC, null);

        this.page = page;

        try {
            PageResponseDTO<SimpleNewsDTO> response = newsService.findAllUnread(pageRequestDTO);
            this.totalPages = response.getTotalPages() > 0 ? response.getTotalPages() : 1;

            for (int i = 0; i < response.getContent().size(); i++) {
                SimpleNewsDTO news = response.getContent().get(i);
                SpringFxmlLoader.Wrapper<NewsElementController> wrapper =
                    springFxmlLoader.loadAndWrap("/fxml/news/newsElement.fxml");
                wrapper.getLoadedObject().setId("unreadNews" + loadedNews.size() + i);
                wrapper.getController().initializeData(news);
                vbNewsBoxChildren.add(wrapper.getLoadedObject());
                if (i + 1 < response.getContent().size()) {
                    Separator separator = new Separator();
                    vbNewsBoxChildren.add(separator);
                }
            }
            loadedNews.addAll(response.getContent());
        } catch (DataAccessException e) {
            JavaFXUtils.createErrorDialog(e.getMessage(),
                vbNewsElements.getScene().getWindow()).showAndWait();
        }

        return true;
    }

    private ScrollBar getVerticalScrollbar() {
        for (Node n : simpleNewsScrollPane.lookupAll(".scroll-bar")) {
            if (n instanceof ScrollBar) {
                ScrollBar bar = (ScrollBar) n;
                if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                    return bar;
                }
            }
        }

        return null;
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
            detailScrollPane.setVisible(false);
        } else {
            column1.setPercentWidth(30);
            column2.setPercentWidth(70);
            currentlySelectedNews = clickedNews;
            vbox.getStyleClass().add("vbox-selected");
            fillDetailView();
            detailScrollPane.setVisible(true);
        }
    }

    private void fillDetailView() {
        DetailedNewsDTO detailedNewsDTO = null;
        try {
            detailedNewsDTO = newsService.find(currentlySelectedNews.getId());
        } catch (DataAccessException e) {
            LOGGER.debug(e.getMessage());
        }

        titleLabel.setText(detailedNewsDTO.getTitle());

        WebEngine webEngine = webView.getEngine();
        String html = detailedNewsDTO.getText();
        if(html.contains("contenteditable=\"true\"")){
            html = html.replace("contenteditable=\"true\"", "contenteditable=\"false\" bgcolor=F4F4F4> <div id='container'");
        }

        html.replace("</body>", "</div></body>");
        webEngine.loadContent(html);

        if(detailedNewsDTO.getImageData() != null && detailedNewsDTO.getImageData().length > 0) {
            Image image = new Image(new ByteArrayInputStream(detailedNewsDTO.getImageData()));
            imageView.setImage(image);
        } else {
            imageView.setImage(null);
        }

        resizeWebView();

        webView.widthProperty().addListener((ChangeListener<Object>) (observable, oldValue, newValue) -> resizeWebView());

        webView.heightProperty().addListener((ChangeListener<Object>) (observable, oldValue, newValue) -> resizeWebView());

        webView.getScene().getWindow().heightProperty().addListener((ChangeListener<Object>) (observable, oldValue, newValue) -> {
            resizeWebView();
        });

        webView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> arg0, Worker.State oldState, Worker.State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    resizeWebView();
                }
            }
        });
    }

    private void resizeWebView() {
        Platform.runLater(() -> {
            try {
                double height = Double.parseDouble(webView.getEngine().executeScript("document.getElementById('container').clientHeight").toString());
                height += 50;
                webView.setMinHeight(height);
                webView.setPrefHeight(height);
            } catch (Exception e) {

            }
        });
    }
}
