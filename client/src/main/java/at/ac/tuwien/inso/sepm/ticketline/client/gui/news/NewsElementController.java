package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

import static java.time.format.FormatStyle.LONG;
import static java.time.format.FormatStyle.SHORT;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class NewsElementController {

    private static final DateTimeFormatter NEWS_DTF = DateTimeFormatter.ofLocalizedDateTime(LONG, SHORT);

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblText;

    public void initializeData(SimpleNewsDTO simpleNewsDTO) {
        lblDate.setText(NEWS_DTF.format(simpleNewsDTO.getPublishedAt()));
        lblTitle.setText(simpleNewsDTO.getTitle());
        lblText.setText(simpleNewsDTO.getSummary());
    }

}
