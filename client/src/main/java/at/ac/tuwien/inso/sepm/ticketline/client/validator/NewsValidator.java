package at.ac.tuwien.inso.sepm.ticketline.client.validator;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.NewsValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

public class NewsValidator {

    public static final int MIN_CHARS_TITLE = 1;
    public static final int MAX_CHARS_TITLE = 100;
    public static final int MIN_CHARS_TEXT = 1;
    public static final int MAX_CHARS_TEXT = 10000;

    public static String validateTitle(TextField titleTextField) throws NewsValidationException {
        String title = titleTextField.getText();

        if (title == null || title.length() < MIN_CHARS_TITLE || title.length() > MAX_CHARS_TITLE) {
            throw new NewsValidationException(
                BundleManager.getExceptionBundle().getString("exception.validator.news.title_length_invalid")
            );
        }

        return title;
    }

    public static String validateArticle(HTMLEditor articleHTMLEditor) throws NewsValidationException {
        String article = articleHTMLEditor.getHtmlText();

        if (article == null || article.length() < MIN_CHARS_TEXT) {
            throw new NewsValidationException(
                BundleManager.getExceptionBundle().getString("exception.validator.news.article.empty")
            );
        }

        if (article.length() > MAX_CHARS_TEXT) {
            throw new NewsValidationException(
                BundleManager.getExceptionBundle().getString("exception.validator.news.article.too_long")
            );
        }

        return article;
    }
}
