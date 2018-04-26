package at.ac.tuwien.inso.sepm.ticketline.client.configuration;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.TimeZone;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

@Configuration
public class JacksonConfiguration {


    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder()
            .serializationInclusion(NON_NULL)
            .featuresToDisable(WRITE_DATES_AS_TIMESTAMPS)
            .indentOutput(true)
            .findModulesViaServiceLoader(true)
            .modules(new JavaTimeModule())
            .timeZone(TimeZone.getDefault())
            ;
    }
}
