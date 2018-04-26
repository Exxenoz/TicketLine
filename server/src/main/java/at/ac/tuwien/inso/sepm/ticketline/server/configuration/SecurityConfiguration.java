package at.ac.tuwien.inso.sepm.ticketline.server.configuration;

import at.ac.tuwien.inso.sepm.ticketline.server.configuration.properties.H2ConsoleConfigurationProperties;
import at.ac.tuwien.inso.sepm.ticketline.server.security.HeaderTokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Map;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest.to;
import static org.springframework.boot.autoconfigure.security.SecurityProperties.BASIC_AUTH_ORDER;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfiguration {

    private final PasswordEncoder passwordEncoder;

    public SecurityConfiguration(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public static PasswordEncoder configureDefaultPasswordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
                Map<String, Object> errorAttributes = super.getErrorAttributes((WebRequest) requestAttributes, includeStackTrace);
                errorAttributes.remove("exception");
                return errorAttributes;
            }
        };
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, List<AuthenticationProvider> providerList) throws Exception {
        new InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder>()
            .withUser("user").password(passwordEncoder.encode("password")).authorities("USER").and()
            .withUser("admin").password(passwordEncoder.encode("password")).authorities("ADMIN", "USER").and()
            .passwordEncoder(passwordEncoder)
            .configure(auth);
        providerList.forEach(auth::authenticationProvider);
    }

    @Configuration
    @Order(BASIC_AUTH_ORDER)
    private static class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

        private final String h2ConsolePath;
        private final String h2AccessMatcher;

        @Autowired
        private AuthenticationManager authenticationManager;

        public WebSecurityConfiguration(
            H2ConsoleConfigurationProperties h2ConsoleConfigurationProperties
        ) {
            h2ConsolePath = h2ConsoleConfigurationProperties.getPath();
            h2AccessMatcher = h2ConsoleConfigurationProperties.getAccessMatcher();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .csrf().disable()
                .headers().frameOptions().sameOrigin().and()
                .sessionManagement().sessionCreationPolicy(STATELESS).and()
                .exceptionHandling().authenticationEntryPoint((req, res, aE) -> res.sendError(SC_UNAUTHORIZED)).and()
                .authorizeRequests()
                .antMatchers(OPTIONS).permitAll()
                .antMatchers(POST, "/authentication").permitAll()
                .antMatchers(GET,
                    "/v2/api-docs",
                    "/swagger-resources/**",
                    "/webjars/springfox-swagger-ui/**",
                    "/swagger-ui.html")
                .permitAll()
            ;
            if (h2ConsolePath != null && h2AccessMatcher != null) {
                http
                    .authorizeRequests()
                    .antMatchers(h2ConsolePath + "/**").access(h2AccessMatcher);
            }
            http
                .authorizeRequests()
                .anyRequest().fullyAuthenticated()
                .and()
                .addFilterBefore(
                    new HeaderTokenAuthenticationFilter(authenticationManager),
                    UsernamePasswordAuthenticationFilter.class
                );
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                    .addMapping("/**")
                    .allowedOrigins("*");
            }
        };
    }

    @Configuration
    public class ActuatorSecurity extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.requestMatcher(to(InfoEndpoint.class)).authorizeRequests()
                .anyRequest().permitAll();
        }

    }

}
