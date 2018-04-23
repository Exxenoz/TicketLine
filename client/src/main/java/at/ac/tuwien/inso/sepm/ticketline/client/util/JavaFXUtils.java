package at.ac.tuwien.inso.sepm.ticketline.client.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Window;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * This class provides helper methods for JavaFX.
 */
public class JavaFXUtils {

    /**
     * An empty private constructor to prevent the creation of an JavaFXUtils Instance.
     */
    private JavaFXUtils() {

    }

    /**
     * Creates a dialog for an exception.
     * Based on <a href="http://code.makery.ch/blog/javafx-dialogs-official/">http://code.makery.ch/blog/javafx-dialogs-official/</a>
     *
     * @param throwable the throwable
     * @return the dialog which shows the exception
     */
    public static Alert createExceptionDialog(Throwable throwable, Window parentWindow) {
        Alert alert = new Alert(AlertType.ERROR);
        if (parentWindow != null) {
            alert.initOwner(parentWindow);
            alert.initModality(Modality.WINDOW_MODAL);
        }
        alert.setTitle(BundleManager.getExceptionBundle().getString("error"));
        alert.setHeaderText(BundleManager.getExceptionBundle().getString("exception.unexpected"));
        alert.setContentText(throwable.getLocalizedMessage());
        Label label = new Label(BundleManager.getExceptionBundle().getString("exception.stacktrace"));
        TextArea textArea = new TextArea(stacktraceToString(throwable));
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);
        alert.getDialogPane().setExpandableContent(expContent);
        return alert;
    }

    private static String stacktraceToString(Throwable throwable) {
        StringWriter sw = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    /**
     * Creates a standard error dialog.
     *
     * @param contentText  of the error dialog
     * @param parentWindow or null if not modal
     * @return the dialog which shows the error
     */
    public static Alert createErrorDialog(String contentText, Window parentWindow) {
        Alert alert = new Alert(AlertType.ERROR);
        if (parentWindow != null) {
            alert.initOwner(parentWindow);
            alert.initModality(Modality.WINDOW_MODAL);
        }
        alert.setTitle(BundleManager.getExceptionBundle().getString("error"));
        alert.setHeaderText(BundleManager.getExceptionBundle().getString("exception.unexpected"));
        alert.setContentText(contentText);
        return alert;
    }
}
