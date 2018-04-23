package at.ac.tuwien.inso.sepm.ticketline.server.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Validated
@Configuration
@ConfigurationProperties("spring.h2.console")
public class H2ConsoleConfigurationProperties {

    private String path = "/h2-console";
    private String accessMatcher = "denyAll";

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAccessMatcher() {
        return accessMatcher;
    }

    public void setAccessMatcher(String accessMatcher) {
        this.accessMatcher = accessMatcher;
    }
}
