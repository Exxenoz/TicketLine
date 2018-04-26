package at.ac.tuwien.inso.sepm.ticketline.client.configuration.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Properties;

@Configuration
@ConditionalOnMissingBean(GitProperties.class)
public class GitConfigurationProperties {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String PREFIX_TO_STRIP = "git.";

    @Bean
    public GitProperties gitProperties() {
        final var properties = new Properties();
        try {
            PropertiesLoaderUtils.loadProperties(new ClassPathResource("git.properties"))
                .forEach((key, value) -> {
                    if (((String) key).startsWith(PREFIX_TO_STRIP)) {
                        properties.put(((String) key).substring(PREFIX_TO_STRIP.length(), ((String) key).length()), value);
                    } else {
                        properties.put(key, value);
                    }
                });
        } catch (IOException e) {
            LOGGER.warn("Failed to load git.properties, {}", e.getMessage());
        }
        return new GitProperties(properties);
    }
}
