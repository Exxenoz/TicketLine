package at.ac.tuwien.inso.sepm.ticketline.client.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@Configuration
@ConfigurationProperties("java-fx")
public class JavaFxConfigurationProperties {

    @NotNull
    private Integer initialHeight = 640;

    @NotNull
    private Integer initialWidth = 1060;

    @NotNull
    private String title;

    public Integer getInitialHeight() {
        return initialHeight;
    }

    public void setInitialHeight(Integer initialHeight) {
        this.initialHeight = initialHeight;
    }

    public Integer getInitialWidth() {
        return initialWidth;
    }

    public void setInitialWidth(Integer initialWidth) {
        this.initialWidth = initialWidth;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
