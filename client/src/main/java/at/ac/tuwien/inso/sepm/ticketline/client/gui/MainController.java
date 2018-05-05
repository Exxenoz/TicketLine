package at.ac.tuwien.inso.sepm.ticketline.client.gui;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.events.EventController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.news.NewsController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.AuthenticationInformationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.glyphfont.FontAwesome;
import org.springframework.stereotype.Component;

import static javafx.application.Platform.runLater;
import static javafx.stage.Modality.APPLICATION_MODAL;
import static javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST;
import static org.controlsfx.glyphfont.FontAwesome.Glyph.CALENDAR_ALT;
import static org.controlsfx.glyphfont.FontAwesome.Glyph.NEWSPAPER_ALT;

@Component
public class MainController {

    private static final int TAB_ICON_FONT_SIZE = 20;

    @FXML
    private StackPane spMainContent;

    @FXML
    private ProgressBar pbLoadingProgress;

    @FXML
    private TabPane tpContent;

    @FXML
    private MenuBar mbMain;

    private Node login;

    private final SpringFxmlLoader springFxmlLoader;
    private final FontAwesome fontAwesome;
    private NewsController newsController;
    private EventController eventController;

    public MainController(
        SpringFxmlLoader springFxmlLoader,
        FontAwesome fontAwesome,
        AuthenticationInformationService authenticationInformationService
    ) {
        this.springFxmlLoader = springFxmlLoader;
        this.fontAwesome = fontAwesome;
        authenticationInformationService.addAuthenticationChangeListener(
            authenticationTokenInfo -> setAuthenticated(null != authenticationTokenInfo));
    }

    @FXML
    private void initialize() {
        runLater(() -> mbMain.setUseSystemMenuBar(true));
        pbLoadingProgress.setProgress(0);
        login = springFxmlLoader.load("/fxml/authenticationComponent.fxml");
        spMainContent.getChildren().add(login);
        initNewsTabPane();
        initEventsTabPane();
    }

    @FXML
    private void exitApplication(ActionEvent actionEvent) {
        final var stage = (Stage) spMainContent.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WINDOW_CLOSE_REQUEST));
    }

    @FXML
    private void aboutApplication(ActionEvent actionEvent) {
        final var stage = (Stage) spMainContent.getScene().getWindow();
        final var dialog = new Stage();
        dialog.setResizable(false);
        dialog.initModality(APPLICATION_MODAL);
        dialog.initOwner(stage);
        dialog.setScene(new Scene(springFxmlLoader.load("/fxml/aboutDialog.fxml")));
        dialog.setTitle(BundleManager.getBundle().getString("dialog.about.title"));
        dialog.showAndWait();
    }

    private void initNewsTabPane() {
        SpringFxmlLoader.Wrapper<NewsController> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/news/newsComponent.fxml");
        newsController = wrapper.getController();
        final var newsTab = new Tab(null, wrapper.getLoadedObject());
        final var newsGlyph = fontAwesome.create(NEWSPAPER_ALT);
        newsGlyph.setFontSize(TAB_ICON_FONT_SIZE);
        newsGlyph.setColor(Color.WHITE);
        newsTab.setGraphic(newsGlyph);
        tpContent.getTabs().add(newsTab);

    }

    private void initEventsTabPane() {
        SpringFxmlLoader.Wrapper<EventController> wrapperEvents =
            springFxmlLoader.loadAndWrap("/fxml/events/eventMain.fxml");
        eventController = wrapperEvents.getController();
        final var eventsTab = new Tab(null, wrapperEvents.getLoadedObject());
        final var eventsGlyph = fontAwesome.create(CALENDAR_ALT);
        eventsGlyph.setFontSize(TAB_ICON_FONT_SIZE);
        eventsGlyph.setColor(Color.WHITE);
        eventsTab.setGraphic(eventsGlyph);
        tpContent.getTabs().add(eventsTab);
    }

    private void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            spMainContent.getChildren().remove(login);
            newsController.loadNews();
        } else {
            if (!spMainContent.getChildren().contains(login)) {
                spMainContent.getChildren().add(login);
            }
        }
    }

    public void setProgressbarProgress(double progress) {
        pbLoadingProgress.setProgress(progress);
    }
}
