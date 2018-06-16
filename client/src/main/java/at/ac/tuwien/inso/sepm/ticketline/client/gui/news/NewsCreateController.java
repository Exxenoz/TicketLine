package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.NewsValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.client.validator.NewsValidator;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.paint.Color;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;

import static org.controlsfx.glyphfont.FontAwesome.Glyph.NEWSPAPER_ALT;

@Component
public class NewsCreateController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    public Button clearImageButton;

    @FXML
    public Label imageFileLabel;

    @FXML
    public HTMLEditor htmlEditor;

    @FXML
    public TextField titleTextField;

    @FXML
    public TextField summaryTextField;

    @FXML
    public Label titleErrorLabel;

    @FXML
    public Label imageErrorLabel;

    @FXML
    public Label summaryErrorLabel;

    @FXML
    public Label articleErrorLabel;

    @FXML
    public Label createNewsLabel;

    @FXML
    public Label uploadLabel;

    @FXML
    public Button chooseImageFileButton;

    @FXML
    public Label summaryLabel;

    @FXML
    public Label articleLabel;

    @FXML
    public Label requiredFieldLabel;

    @FXML
    public Button saveNewsButton;

    @FXML
    private TabHeaderController tabHeaderController;

    private final MainController mainController;
    private final NewsUnreadController newsUnreadController;
    private final NewsService newsService;

    public NewsCreateController(MainController mainController, NewsUnreadController newsUnreadController, NewsService newsService) {
        this.mainController = mainController;
        this.newsUnreadController = newsUnreadController;
        this.newsService = newsService;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(NEWSPAPER_ALT);
        tabHeaderController.setTitleBinding(BundleManager.getStringBinding("news.header.create"));
        htmlEditor.addEventHandler(InputEvent.ANY, new EventHandler<InputEvent>() {
            @Override
            public void handle(InputEvent event) {
                try {
                    NewsValidator.validateArticle(htmlEditor);
                    articleErrorLabel.textProperty().unbind();
                    articleErrorLabel.setText("");
                } catch (NewsValidationException e) {
                    articleErrorLabel.textProperty().bind(BundleManager.getExceptionStringBinding(e.getExceptionBundleKey()));
                }
            }
        });

        initI18N();
    }

    private void initI18N() {
        createNewsLabel.textProperty().bind(BundleManager.getStringBinding("news.create.title"));
        uploadLabel.textProperty().bind(BundleManager.getStringBinding("news.create.upload"));
        chooseImageFileButton.textProperty().bind(BundleManager.getStringBinding("news.create.choose"));
        summaryLabel.textProperty().bind(BundleManager.getStringBinding("news.create.summary"));
        articleLabel.textProperty().bind(BundleManager.getStringBinding("news.create.article"));
        requiredFieldLabel.textProperty().bind(BundleManager.getStringBinding("news.dialog.create.required_field"));
        saveNewsButton.textProperty().bind(BundleManager.getStringBinding("news.create.save"));
    }

    private void clearInputs() {
        titleErrorLabel.textProperty().unbind();
        imageErrorLabel.textProperty().unbind();
        summaryErrorLabel.textProperty().unbind();
        articleErrorLabel.textProperty().unbind();

        titleErrorLabel.setText("");
        imageErrorLabel.setText("");
        summaryErrorLabel.setText("");
        articleErrorLabel.setText("");

        titleTextField.setText("");
        imageFileLabel.setText("");
        summaryTextField.setText("");
        htmlEditor.setHtmlText("");

        clearImageButton.setDisable(true);
        summaryErrorLabel.setMinHeight(0);
    }

    public void onClickChooseImageFileButton(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(BundleManager.getBundle().getString("news.filechooser.title"));

        //Only Allow .jpg and .png files
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("All Images", "*.jpg", "*jpeg", "*.png")
        );

        File imageFile = fileChooser.showOpenDialog(imageFileLabel.getScene().getWindow());
        if(imageFile != null) {
            imageFileLabel.setText(imageFile.getAbsolutePath());
            imageFileLabel.setTextFill(Color.BLACK);
            clearImageButton.setDisable(false);
            LOGGER.info("User chose image for news article");
        }
    }

    public void onClickSaveNewsClicked(ActionEvent actionEvent) {
        LOGGER.debug("Clicked save news button");

        DetailedNewsDTO detailedNewsDTO = new DetailedNewsDTO();

        boolean valid = true;

        try {
            detailedNewsDTO.setTitle(NewsValidator.validateTitle(titleTextField));
            titleErrorLabel.textProperty().unbind();
            titleErrorLabel.setText("");
        } catch (NewsValidationException e) {
            valid = false;
            LOGGER.debug("News validation failed: " + e.getMessage());
            titleErrorLabel.textProperty().bind(BundleManager.getExceptionStringBinding(e.getExceptionBundleKey()));
        }

        try {
            detailedNewsDTO.setImageData(NewsValidator.validateImage(imageFileLabel.getText()));
            imageErrorLabel.textProperty().unbind();
            imageErrorLabel.setText("");
        } catch (NewsValidationException e) {
            valid = false;
            LOGGER.debug("News validation failed: " + e.getMessage());
            imageErrorLabel.textProperty().bind(BundleManager.getExceptionStringBinding(e.getExceptionBundleKey()));
        }

        try {
            detailedNewsDTO.setSummary(NewsValidator.validateSummary(summaryTextField));
            summaryErrorLabel.textProperty().unbind();
            summaryErrorLabel.setText("");
            summaryErrorLabel.setMinHeight(0);
        } catch (NewsValidationException e) {
            valid = false;
            LOGGER.debug("News validation failed: " + e.getMessage());
            summaryErrorLabel.textProperty().bind(BundleManager.getExceptionStringBinding(e.getExceptionBundleKey()));
            summaryErrorLabel.setMinHeight(16);
        }

        try {
            detailedNewsDTO.setText(NewsValidator.validateArticle(htmlEditor));
            articleErrorLabel.textProperty().unbind();
            articleErrorLabel.setText("");
        } catch (NewsValidationException e) {
            valid = false;
            LOGGER.debug("News validation failed: " + e.getMessage());
            articleErrorLabel.textProperty().bind(BundleManager.getExceptionStringBinding(e.getExceptionBundleKey()));
        }

        if (!valid) {
            return;
        }

        detailedNewsDTO.setPublishedAt(LocalDateTime.now());

        // TODO: add manually instead of reloading all news?
        SimpleNewsDTO simpleNewsDTO = null;
        try {
            simpleNewsDTO = newsService.publish(detailedNewsDTO);
        } catch (DataAccessException e) {
            JavaFXUtils.createErrorDialog(
                BundleManager.getBundle().getString("news.dialog.create.dialog.error.title"),
                BundleManager.getBundle().getString("news.dialog.create.dialog.error.header_text"),
                e.getMessage(),
                titleTextField.getScene().getWindow()
            ).showAndWait();

            return;
        }

        LOGGER.debug("News creation successfully completed!");

        JavaFXUtils.createInformationDialog(
            BundleManager.getBundle().getString("news.dialog.create.dialog.success.title"),
            BundleManager.getBundle().getString("news.dialog.create.dialog.success.header_text"),
            BundleManager.getBundle().getString("news.dialog.create.dialog.success.content_text"),
            titleTextField.getScene().getWindow()
        ).showAndWait();

        newsUnreadController.reloadNews();

        clearInputs();
    }

    public void onClickClearImageButton(ActionEvent actionEvent) {
        clearImageButton.setDisable(true);
        imageFileLabel.setText("");
        imageErrorLabel.textProperty().unbind();
        imageErrorLabel.setText("");
    }
}
