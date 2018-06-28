package at.ac.tuwien.inso.sepm.ticketline.server.configuration;

import at.ac.tuwien.inso.sepm.ticketline.server.configuration.properties.ApplicationConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.HashSet;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    private final ApplicationConfigurationProperties acp;

    public SwaggerConfiguration(ApplicationConfigurationProperties applicationConfigurationProperties) {
        acp = applicationConfigurationProperties;
    }

    @Bean
    public Docket ticketlineApiDocket() {
        return new Docket(SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(new ApiInfo(
                "Ticketline Server",
                "Interactive API documentation for the Ticketline Server",
                acp.getVersion(),
                null,
                null,
                null,
                null,
                Collections.emptyList()
            ))
            .genericModelSubstitutes(ResponseEntity.class)
            .securitySchemes(newArrayList(new ApiKey("Authorization", "Authorization", "header")))
            .useDefaultResponseMessages(false)
            .globalResponseMessage(GET,
                newArrayList(
                    new ResponseMessageBuilder()
                        .code(OK.value())
                        .message("Success")
                        .build(),
                    new ResponseMessageBuilder()
                        .code(UNAUTHORIZED.value())
                        .message("Unauthorized request, login first")
                        .build()))
            .globalResponseMessage(POST,
                newArrayList(
                    new ResponseMessageBuilder()
                        .code(OK.value())
                        .message("Success")
                        .build(),
                    new ResponseMessageBuilder()
                        .code(UNAUTHORIZED.value())
                        .message("Unauthorized request, login first")
                        .build()))
            .consumes(new HashSet<>(Collections.singletonList(APPLICATION_JSON_VALUE)))
            .produces(new HashSet<>(Collections.singletonList(APPLICATION_JSON_VALUE)))
            ;
    }
}
