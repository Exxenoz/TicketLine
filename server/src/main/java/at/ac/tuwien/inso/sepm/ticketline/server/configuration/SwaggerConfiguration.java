package at.ac.tuwien.inso.sepm.ticketline.server.configuration;

import at.ac.tuwien.inso.sepm.ticketline.server.configuration.properties.ApplicationConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.HashSet;

import static com.google.common.collect.Lists.newArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    private final ApplicationConfigurationProperties acp;

    public SwaggerConfiguration(ApplicationConfigurationProperties applicationConfigurationProperties) {
        acp = applicationConfigurationProperties;
    }

    @Bean
    public Docket ticketlineApiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
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
            .globalResponseMessage(RequestMethod.GET,
                newArrayList(
                    new ResponseMessageBuilder()
                        .code(HttpStatus.OK.value())
                        .message("Success")
                        .build(),
                    new ResponseMessageBuilder()
                        .code(HttpStatus.UNAUTHORIZED.value())
                        .message("Unauthorized request, login first")
                        .build()))
            .globalResponseMessage(RequestMethod.POST,
                newArrayList(
                    new ResponseMessageBuilder()
                        .code(HttpStatus.OK.value())
                        .message("Success")
                        .build(),
                    new ResponseMessageBuilder()
                        .code(HttpStatus.UNAUTHORIZED.value())
                        .message("Unauthorized request, login first")
                        .build()))
            .consumes(new HashSet<>(Collections.singletonList(MediaType.APPLICATION_JSON_VALUE)))
            .produces(new HashSet<>(Collections.singletonList(MediaType.APPLICATION_JSON_VALUE)))
            ;
    }

}
