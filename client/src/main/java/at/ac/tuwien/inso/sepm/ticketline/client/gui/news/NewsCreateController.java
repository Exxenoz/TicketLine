package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.NewsValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.client.validator.NewsValidator;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
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
    public Label imageFileLabel;

    @FXML
    public HTMLEditor htmlEditor;

    @FXML
    public TextField titleTextField;

    @FXML
    public Label titleErrorLabel;

    @FXML
    public Label articleErrorLabel;

    @FXML
    private TabHeaderController tabHeaderController;

    private final MainController mainController;
    private final NewsService newsService;

    public NewsCreateController(MainController mainController, NewsService newsService) {
        this.mainController = mainController;
        this.newsService = newsService;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(NEWSPAPER_ALT);
        tabHeaderController.setTitle("Create News");
    }

    private void clearInputs() {
        // TODO
    }

    public void onClickChooseImageFileButton(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");

        //Only Allow .jpg and .png files
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("All Images", "*.jpg", "*jpeg", "*.png")
        );

        File imageFile = fileChooser.showOpenDialog(imageFileLabel.getScene().getWindow());
        if(imageFile != null) {
            imageFileLabel.setText(imageFile.getAbsolutePath());
            imageFileLabel.setTextFill(Color.BLACK);
            LOGGER.info("User chose image for news article");
        }
    }

    public void onClickSaveNewsClicked(ActionEvent actionEvent) {
        LOGGER.debug("Clicked save news button");

        DetailedNewsDTO detailedNewsDTO = new DetailedNewsDTO();

        boolean valid = true;

        try {
            detailedNewsDTO.setTitle(NewsValidator.validateTitle(titleTextField));
        } catch (NewsValidationException e) {
            valid = false;
            LOGGER.debug("News validation failed: " + e.getMessage());
            titleErrorLabel.setText(e.getMessage());
        }

        try {
            detailedNewsDTO.setTitle(NewsValidator.validateArticle(htmlEditor));
        } catch (NewsValidationException e) {
            valid = false;
            LOGGER.debug("News validation failed: " + e.getMessage());
            articleErrorLabel.setText(e.getMessage());
        }

        if (!valid) {
            return;
        }

        detailedNewsDTO.setPublishedAt(LocalDateTime.now());
        
        //newsService.publish(detailedNewsDTO);

        LOGGER.debug("News creation successfully completed!");

        JavaFXUtils.createInformationDialog(
            BundleManager.getBundle().getString("news.dialog.create.dialog.success.title"),
            BundleManager.getBundle().getString("news.dialog.create.dialog.success.header_text"),
            BundleManager.getBundle().getString("news.dialog.create.dialog.success.content_text"),
            titleTextField.getScene().getWindow()
        ).showAndWait();

        clearInputs();
    }
}
