package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationRequest;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationToken;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationTokenInfo;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.endpoint.HttpBadRequestException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.endpoint.HttpForbiddenException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.endpoint.HttpLockedException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.*;
import at.ac.tuwien.inso.sepm.ticketline.server.service.HeaderTokenAuthenticationService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.implementation.SimpleHeaderTokenAuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
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
        AuthenticationToken token = null;
        try {
            token = authenticationService.authenticate(authenticationRequest.getUsername(),
                authenticationRequest.getPassword());
        } catch (InternalUserNotFoundException e) {
            throw new HttpForbiddenException();
        } catch (InternalUserDisabledException e) {
            throw new HttpForbiddenException();
        } catch (InternalPasswordResetException e) {
            throw new HttpLockedException();
        } catch (InternalUserPasswordWrongException e) {
            throw new HttpForbiddenException();
        }
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
    @ApiOperation("Get information about the current user authentication token")
    public AuthenticationTokenInfo tokenInfoCurrent(@ApiIgnore @RequestHeader(AUTHORIZATION) String authorizationHeader) {
        return authenticationService.authenticationTokenInfo(authorizationHeader.substring(TOKEN_PREFIX.length()).trim());
    }
}
