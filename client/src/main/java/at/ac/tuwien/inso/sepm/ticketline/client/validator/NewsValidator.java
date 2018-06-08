package at.ac.tuwien.inso.sepm.ticketline.client.validator;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.NewsValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class NewsValidator {

    public static final int MIN_CHARS_TITLE = 1;
    public static final int MAX_CHARS_TITLE = 100;
    public static final int MIN_CHARS_SUMMARY = 1;
    public static final int MAX_CHARS_SUMMARY = 50;
    public static final String EMPTY_ARTICLE_REGEX = ".*<body contenteditable=\"true\">.*>\\s*[a-zA-Z0-9]+\\s*<.*<\\/body>.*";
    public static final int MAX_CHARS_TEXT = 10000;
    public static final int MIN_IMG_WIDTH = 300;
    public static final int MIN_IMG_HEIGHT = 300;
    public static final int MAX_SIZE_IMAGE_DATA = 8000000;

    public static String validateTitle(TextField titleTextField) throws NewsValidationException {
        String title = titleTextField.getText();

        if (title == null || title.length() < MIN_CHARS_TITLE || title.length() > MAX_CHARS_TITLE) {
            throw new NewsValidationException(
                BundleManager.getExceptionBundle().getString("exception.validator.news.title_length_invalid")
            );
        }

        return title;
    }

    public static byte[] validateImage(String path) throws NewsValidationException {
        if (path.isEmpty()) {
            // Image is optional
            return null;
        }

        validateFileName(path);

        File file = new File(path);
        if (!file.exists()) {
            throw new NewsValidationException(
                BundleManager.getExceptionBundle().getString("exception.validator.news.image_not_found")
            );
        }

        if (file.length() > MAX_SIZE_IMAGE_DATA) {
            throw new NewsValidationException(
                BundleManager.getExceptionBundle().getString("exception.validator.news.image_too_big")
            );
        }

        BufferedImage img;
        try {
            img = ImageIO.read(file);
        } catch (IOException e) {
            throw new NewsValidationException(
                BundleManager.getExceptionBundle().getString("exception.validator.news.image.could_not_read")
            );
        }

        if (img.getWidth() < MIN_IMG_WIDTH || img.getHeight() < MIN_IMG_HEIGHT) {
            throw new NewsValidationException(
                BundleManager.getExceptionBundle().getString("exception.validator.news.image_too_small")
            );
        }

        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new NewsValidationException(
                BundleManager.getExceptionBundle().getString("exception.validator.news.image.could_not_read")
            );
        }
    }

    public static void validateFileName(String fileName) throws NewsValidationException {
        if(fileName != null && !fileName.isEmpty()) {
            int extensionIndex = fileName.lastIndexOf(".");
            if(extensionIndex < 0) {
                throw new NewsValidationException(
                    BundleManager.getExceptionBundle().getString("exception.validator.news.invalid_filetype")
                );
            }

            String extension = fileName.substring(extensionIndex);
            if (extension == null || extension.isEmpty()) {
                throw new NewsValidationException(
                    BundleManager.getExceptionBundle().getString("exception.validator.news.invalid_filetype")
                );
            }

            if (!extension.equals(".png") && !extension.equals(".jpg") &&
                !extension.equals(".jpeg")) {
                throw new NewsValidationException(
                    BundleManager.getExceptionBundle().getString("exception.validator.news.invalid_filetype")
                );
            }
        } else {
            throw new NewsValidationException(
                BundleManager.getExceptionBundle().getString("exception.validator.news.image_not_found")
            );
        }
    }

    public static String validateSummary(TextField summaryTextField) throws NewsValidationException {
        String summary = summaryTextField.getText();

        if (summary == null || summary.length() < MIN_CHARS_SUMMARY || summary.length() > MAX_CHARS_SUMMARY) {
            throw new NewsValidationException(
                BundleManager.getExceptionBundle().getString("exception.validator.news.summary_length_invalid")
            );
        }

        return summary;
    }

    public static String validateArticle(HTMLEditor articleHTMLEditor) throws NewsValidationException {
        String article = articleHTMLEditor.getHtmlText();

        if (article == null || !article.matches(EMPTY_ARTICLE_REGEX)) {
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
