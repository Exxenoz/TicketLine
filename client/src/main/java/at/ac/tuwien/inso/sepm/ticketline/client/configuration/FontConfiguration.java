package at.ac.tuwien.inso.sepm.ticketline.client.configuration;

import org.controlsfx.glyphfont.FontAwesome;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FontConfiguration {

    @Bean
    public FontAwesome fontAwesome() {
        return new FontAwesome();
    }
}
