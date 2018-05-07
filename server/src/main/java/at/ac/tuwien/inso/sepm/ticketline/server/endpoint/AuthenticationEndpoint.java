package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationRequest;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationToken;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationTokenInfo;
import at.ac.tuwien.inso.sepm.ticketline.server.service.HeaderTokenAuthenticationService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.implementation.SimpleHeaderTokenAuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import static at.ac.tuwien.inso.sepm.ticketline.server.security.AuthenticationConstants.TOKEN_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping(value = "/authentication")
@Api(value = "authentication")
public class AuthenticationEndpoint {

    private final HeaderTokenAuthenticationService authenticationService;

    public AuthenticationEndpoint(SimpleHeaderTokenAuthenticationService simpleHeaderTokenAuthenticationService) {
        authenticationService = simpleHeaderTokenAuthenticationService;
    }

    @PostMapping
    @ApiOperation("Get an authentication token with your username and password")
    public AuthenticationToken authenticate(@RequestBody final AuthenticationRequest authenticationRequest) {

        AuthenticationToken token = authenticationService.authenticate(authenticationRequest.getUsername(),
            authenticationRequest.getPassword());

        return token;
    }

    @GetMapping
    @ApiOperation("Get some valid authentication tokens")
    public AuthenticationToken authenticate(@ApiIgnore @RequestHeader(AUTHORIZATION) String authorizationHeader) {
        return authenticationService.renewAuthentication(authorizationHeader.substring(TOKEN_PREFIX.length()).trim());
    }

    @GetMapping("/info/{token}")
    @ApiOperation("Get information about a specific authentication token")
    public AuthenticationTokenInfo tokenInfoAny(@PathVariable String token) {
        return authenticationService.authenticationTokenInfo(token);
    }

    @GetMapping("/info")
    @ApiOperation("Get information about the current users authentication token")
    public AuthenticationTokenInfo tokenInfoCurrent(@ApiIgnore @RequestHeader(AUTHORIZATION) String authorizationHeader) {
        return authenticationService.authenticationTokenInfo(authorizationHeader.substring(TOKEN_PREFIX.length()).trim());
    }
}
