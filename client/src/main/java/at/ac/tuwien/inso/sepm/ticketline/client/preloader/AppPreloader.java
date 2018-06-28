package at.ac.tuwien.inso.sepm.ticketline.client.preloader;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.TicketlineInfoController;
import at.ac.tuwien.inso.springfx.SpringFxApplication;
import javafx.application.Preloader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import static javafx.stage.StageStyle.TRANSPARENT;

public class AppPreloader extends Preloader {

    private static final String DEFAULT_BUILD_VERSION = "3.0.0";

    private Stage stage;

    @FXML
    private ProgressBar pbLoad;

    @FXML
    private TicketlineInfoController ticketlineInfoController;

    private static final DateTimeFormatter ISO_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private static final ZonedDateTime DEFAULT_BUILD_TIME = ZonedDateTime.now();

    public void start(Stage stage) throws IOException {
        this.stage = stage;
        stage.initStyle(TRANSPARENT);
        Font.loadFont(getClass().getResource("/font/CaviarDreams_Bold.ttf").toExternalForm(), 12);
        final var fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/preloader.fxml"));
        fxmlLoader.setController(this);
        stage.setScene(new Scene(fxmlLoader.load()));
        ticketlineInfoController.setInfoText("Loading ...");
        final var properties = new Properties();
        final var buildInfoPropertiesResource = getClass().getResourceAsStream("/git.properties");
        if (buildInfoPropertiesResource != null) {
            properties.load(buildInfoPropertiesResource);
        }
        ticketlineInfoController.setVersion(properties.getProperty("git.build.version", DEFAULT_BUILD_VERSION));
        final var localDateTime = ZonedDateTime.parse(
            properties.getProperty("git.build.time", ISO_DATETIME_FORMATTER.format(DEFAULT_BUILD_TIME)),
            ISO_DATETIME_FORMATTER
        ).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
        ticketlineInfoController.setBuildDateTime(localDateTime);
        stage.show();
        stage.setAlwaysOnTop(true);
        stage.toFront();
        stage.setAlwaysOnTop(false);
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification pn) {
        if (pn instanceof StateChangeNotification) {
            stage.hide();
        } else if (pn instanceof SpringFxApplication.SpringProgressNotification) {
            final var progressInPercent = ((SpringFxApplication.SpringProgressNotification) pn).getProgress();
            if (progressInPercent <= 0) {
                if (pbLoad.getProgress() != ProgressIndicator.INDETERMINATE_PROGRESS) {
                    pbLoad.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
                }
            } else {
                pbLoad.setProgress(progressInPercent);
            }
            final var detailsText = ((SpringFxApplication.SpringProgressNotification) pn).getDetails();
            if ((detailsText != null) && !detailsText.isEmpty()) {
                ticketlineInfoController.setInfoText(detailsText);
            } else {
                ticketlineInfoController.setInfoText("Loading...");
            }
        }
    }
}
