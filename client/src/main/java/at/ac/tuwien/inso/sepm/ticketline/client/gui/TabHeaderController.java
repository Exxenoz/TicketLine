package at.ac.tuwien.inso.sepm.ticketline.client.gui;

import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.controlsfx.glyphfont.FontAwesome;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class TabHeaderController {

    private static final int HEADER_ICON_SIZE = 25;

    @FXML
    private Label lblHeaderIcon;

    @FXML
    private Label lblHeaderTitle;

    private final FontAwesome fontAwesome;

    public TabHeaderController(FontAwesome fontAwesome) {
        this.fontAwesome = fontAwesome;
    }

    public void setIcon(FontAwesome.Glyph glyph) {
        lblHeaderIcon.setGraphic(
            fontAwesome
                .create(glyph)
                .size(HEADER_ICON_SIZE));
    }

    public void setTitle(String title) {
        lblHeaderTitle.setText(title);
    }

    public void setTitleBinding(StringBinding binding) {
        lblHeaderTitle.textProperty().bind(binding);
    }
}
