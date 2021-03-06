package at.ac.tuwien.inso.sepm.ticketline.client.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;

import java.io.PrintWriter;
import java.io.StringWriter;

import static java.lang.Double.MAX_VALUE;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;
import static javafx.scene.layout.Priority.ALWAYS;
import static javafx.stage.Modality.WINDOW_MODAL;

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
        Alert alert = new Alert(ERROR);
        if (parentWindow != null) {
            alert.initOwner(parentWindow);
            alert.initModality(WINDOW_MODAL);
        }
        alert.setTitle(BundleManager.getExceptionBundle().getString("error"));
        alert.setHeaderText(BundleManager.getExceptionBundle().getString("exception.internal"));
        alert.setContentText(throwable.getLocalizedMessage());
        Label label = new Label(BundleManager.getExceptionBundle().getString("exception.stacktrace"));
        TextArea textArea = new TextArea(stacktraceToString(throwable));
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(MAX_VALUE);
        textArea.setMaxHeight(MAX_VALUE);
        GridPane.setVgrow(textArea, ALWAYS);
        GridPane.setHgrow(textArea, ALWAYS);
        GridPane expContent = new GridPane();
        expContent.setMaxWidth(MAX_VALUE);
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
        return createErrorDialog(
            BundleManager.getExceptionBundle().getString("error"),
            BundleManager.getExceptionBundle().getString("exception.internal"),
            contentText,
            parentWindow
        );
    }

    /**
     * Creates a standard error dialog.
     *
     * @param title of the error dialog
     * @param headerText of the error dialog
     * @param contentText  of the error dialog
     * @param parentWindow or null if not modal
     * @return the dialog which shows the error
     */
    public static Alert createErrorDialog(String title, String headerText, String contentText, Window parentWindow) {
        Alert alert = new Alert(ERROR);
        if (parentWindow != null) {
            alert.initOwner(parentWindow);
            alert.initModality(WINDOW_MODAL);
        }
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.setHeight(300);
        alert.setWidth(400);
        return alert;
    }

    /**
     * Creates a standard information dialog.
     *
     * @param title of the information dialog
     * @param headerText of the information dialog
     * @param contentText  of the information dialog
     * @param parentWindow or null if not modal
     * @return the dialog which shows the information
     */
    public static Alert createInformationDialog(String title, String headerText, String contentText, Window parentWindow) {
        Alert alert = new Alert(INFORMATION);
        if (parentWindow != null) {
            alert.initOwner(parentWindow);
            alert.initModality(WINDOW_MODAL);
        }
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.setHeight(300);
        alert.setWidth(400);
        return alert;
    }

    /**
     * Creates a standard information dialog.
     *
     * @param title of the information dialog
     * @param headerText of the information dialog
     * @param contentText  of the information dialog
     * @param parentWindow or null if not modal
     * @return the dialog which shows the information
     */
    public static Alert createCopyTextDialog(String title, String headerText, String contentText, String copyText, Window parentWindow) {
        Alert alert = new Alert(INFORMATION);
        if (parentWindow != null) {
            alert.initOwner(parentWindow);
            alert.initModality(WINDOW_MODAL);
        }
        alert.setTitle(title);
        alert.setHeaderText(headerText);

        Label label = new Label(contentText);
        TextArea textArea = new TextArea(copyText);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        GridPane gridPane = new GridPane();
        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.add(label, 0, 0);
        gridPane.add(textArea, 0, 1);

        alert.getDialogPane().setContent(gridPane);
        alert.setHeight(300);
        alert.setWidth(400);
        return alert;
    }
}
