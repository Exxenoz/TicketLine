package at.ac.tuwien.inso.sepm.ticketline.server.security;

import at.ac.tuwien.inso.sepm.ticketline.server.service.HeaderTokenAuthenticationService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class HeaderTokenAuthenticationProvider implements AuthenticationProvider {

    private final HeaderTokenAuthenticationService headerTokenAuthenticationService;

    public HeaderTokenAuthenticationProvider(HeaderTokenAuthenticationService headerTokenAuthenticationService) {
        Assert.notNull(headerTokenAuthenticationService, "headerTokenAuthenticationService cannot be null");
        this.headerTokenAuthenticationService = headerTokenAuthenticationService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        final var headerToken = (String) authentication.getCredentials();
        final var user = headerTokenAuthenticationService.authenticate(headerToken);
        final var authenticationResult = new AuthenticationHeaderToken(user, headerToken, user.getAuthorities());
        authenticationResult.setDetails(authentication.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AuthenticationHeaderToken.class.isAssignableFrom(authentication);
    }
}
