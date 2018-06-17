package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.format.DateTimeFormatter;

import static java.time.format.FormatStyle.LONG;
import static java.time.format.FormatStyle.SHORT;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class NewsElementController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final DateTimeFormatter NEWS_DTF = DateTimeFormatter.ofLocalizedDateTime(LONG, SHORT);

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblText;

    private final NewsUnreadController newsUnreadController;

    public NewsElementController(NewsUnreadController newsUnreadController) {
        this.newsUnreadController = newsUnreadController;
    }

    public void initializeData(SimpleNewsDTO simpleNewsDTO) {
        lblDate.setText(NEWS_DTF.format(simpleNewsDTO.getPublishedAt()));
        lblTitle.setText(simpleNewsDTO.getTitle());
        lblText.setText(simpleNewsDTO.getSummary());
    }

    public void toggleDetailView(MouseEvent mouseEvent) {
        VBox vbox = (VBox) mouseEvent.getSource();

        if(vbox != null) {
            String idString = vbox.getId();
            if(idString.startsWith("unreadNews")) {
                int id = Integer.valueOf(idString.substring(10));
                newsUnreadController.toggleDetailView(vbox, id);
            }
        }
    }
}
