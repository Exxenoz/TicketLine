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
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class AppPreloader extends Preloader {

    public static final String DEFAULT_BUILD_VERSION = "3.0.0";

    private Stage stage;

    @FXML
    private ProgressBar pbLoad;

    @FXML
    private TicketlineInfoController ticketlineInfoController;

    private static final DateTimeFormatter ISO_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private static final ZonedDateTime DEFAULT_BUILD_TIME = ZonedDateTime.now();

    public void start(Stage stage) throws IOException {
        this.stage = stage;
        this.stage.initStyle(StageStyle.TRANSPARENT);
        Font.loadFont(getClass().getResource("/font/CaviarDreams_Bold.ttf").toExternalForm(), 12);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/preloader.fxml"));
        fxmlLoader.setController(this);
        this.stage.setScene(new Scene(fxmlLoader.load()));
        ticketlineInfoController.setInfoText("Loading ...");
        Properties properties = new Properties();
        InputStream buildInfoPropertiesResource = getClass().getResourceAsStream("/git.properties");
        if (buildInfoPropertiesResource != null) {
            properties.load(buildInfoPropertiesResource);
        }
        ticketlineInfoController.setVersion(properties.getProperty("git.build.version", DEFAULT_BUILD_VERSION));
        LocalDateTime localDateTime = ZonedDateTime.parse(
            properties.getProperty("git.build.time", ISO_DATETIME_FORMATTER.format(DEFAULT_BUILD_TIME)),
            ISO_DATETIME_FORMATTER
        ).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
        ticketlineInfoController.setBuildDateTime(localDateTime);
        this.stage.show();
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification pn) {
        if (pn instanceof StateChangeNotification) {
            stage.hide();
        } else if (pn instanceof SpringFxApplication.SpringProgressNotification) {
            double progress = ((SpringFxApplication.SpringProgressNotification) pn).getProgress();
            if (progress <= 0) {
                if (pbLoad.getProgress() != ProgressIndicator.INDETERMINATE_PROGRESS) {
                    pbLoad.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
                }
            } else {
                pbLoad.setProgress(progress);
            }
            String details = ((SpringFxApplication.SpringProgressNotification) pn).getDetails();
            if ((details != null) && !details.isEmpty()) {
                ticketlineInfoController.setInfoText(details);
            } else {
                ticketlineInfoController.setInfoText("Loading...");
            }
        }
    }

}
