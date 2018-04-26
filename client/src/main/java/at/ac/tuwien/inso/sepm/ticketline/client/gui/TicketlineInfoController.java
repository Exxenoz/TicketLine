package at.ac.tuwien.inso.sepm.ticketline.client.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * Ticketline Info Controller to display some basic information about Ticketline.
 * This controller is used in the preloading process so no CDI is available at runtime.
 */
@Component
@Scope(SCOPE_PROTOTYPE)
public class TicketlineInfoController {

    @FXML
    private Label labLicenseNumber;

    @FXML
    private Label labVersionMajor;

    @FXML
    private Label labVersionMinor;

    @FXML
    private Label labVersionPatch;

    @FXML
    private Label labBuildTime;

    @FXML
    private Label labBuildDate;

    @FXML
    private Label labInfo;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final Pattern SEMANTIC_VERSION_PATTERN = Pattern.compile("^(?<major>\\d+)(\\.(?<minor>\\d+)(\\.(?<patch>\\d+))?)?.*$");

    public void setBuildDateTime(LocalDateTime localDateTime) {
        labBuildDate.setText(DATE_FORMATTER.format(localDateTime));
        labBuildTime.setText(TIME_FORMATTER.format(localDateTime));
    }

    public void setVersion(String version) {
        version = (version != null) ? version : "";
        final var versionMatcher = SEMANTIC_VERSION_PATTERN.matcher(version);
        if (versionMatcher.matches()) {
            labVersionMajor.setText(Optional.ofNullable(versionMatcher.group("major")).orElse(""));
            labVersionMinor.setText(Optional.ofNullable(versionMatcher.group("minor")).orElse("0"));
            labVersionPatch.setText(Optional.ofNullable(versionMatcher.group("patch")).orElse("0"));
        }
    }

    public void setInfoText(String infoText) {
        labInfo.setText(infoText);
    }
}
