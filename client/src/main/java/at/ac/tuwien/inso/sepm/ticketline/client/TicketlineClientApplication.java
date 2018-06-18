package at.ac.tuwien.inso.sepm.ticketline.client;

import at.ac.tuwien.inso.sepm.ticketline.client.configuration.properties.JavaFxConfigurationProperties;
import at.ac.tuwien.inso.sepm.ticketline.client.preloader.AppPreloader;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.springfx.SpringFxApplication;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

import static javafx.scene.control.Alert.AlertType.CONFIRMATION;
import static javafx.scene.control.ButtonType.OK;
import static javafx.stage.Modality.APPLICATION_MODAL;

@SpringBootApplication(scanBasePackages = {"at.ac.tuwien.inso.sepm.ticketline.client", "at.ac.tuwien.inso.springfx"})
public class TicketlineClientApplication extends SpringFxApplication {

    private static final String JAVAFX_PRELOADER = "javafx.preloader";

    @Autowired
    @SuppressWarnings("SpringAutowiredFieldsWarningInspection")
    // Suppress warning cause sadly it seems that there is no nice way of doing this without field injection here
    private JavaFxConfigurationProperties javaFxConfigurationProperties;

    @Override
    public void start(Stage stage) {

        if(javaFxConfigurationProperties != null) {
            stage.setTitle(javaFxConfigurationProperties.getTitle());
            javaFxConfigurationProperties.setInitialHeight(800);
            javaFxConfigurationProperties.setInitialWidth(1100);

            stage.setScene(new Scene(
                loadParent("/fxml/mainWindow.fxml"),
                javaFxConfigurationProperties.getInitialWidth(),
                javaFxConfigurationProperties.getInitialHeight()
            ));
        } else {
            stage.setTitle(BundleManager.getBundle().getString("main.stage.title"));
            stage.setMinHeight(1100);
            stage.setMinWidth(1100);

            stage.setScene(new Scene(
                loadParent("/fxml/mainWindow.fxml"),
                javaFxConfigurationProperties.getInitialWidth(),
                javaFxConfigurationProperties.getInitialHeight()
            ));
        }

        stage.getIcons()
            .add(new Image(TicketlineClientApplication.class.getResourceAsStream("/image/ticketlineIcon.png")));
        stage.centerOnScreen();
        stage.setOnCloseRequest(event -> {
            Alert alert = new Alert(CONFIRMATION);
            alert.initModality(APPLICATION_MODAL);
            alert.initOwner(stage);
            alert.setTitle(BundleManager.getBundle().getString("dialog.exit.title"));
            alert.setHeaderText(BundleManager.getBundle().getString("dialog.exit.header"));
            alert.setContentText(BundleManager.getBundle().getString("dialog.exit.content"));
            Optional<ButtonType> result = alert.showAndWait();
            if (!result.isPresent() || !OK.equals(result.get())) {
                event.consume();
            }
        });
        stage.show();
        stage.setAlwaysOnTop(true);
        stage.toFront();
        stage.setAlwaysOnTop(false);
    }

    public static void main(String[] args) {
        System.setProperty(JAVAFX_PRELOADER, System.getProperty(JAVAFX_PRELOADER, AppPreloader.class.getCanonicalName()));
        launch(TicketlineClientApplication.class, args);
    }

}
