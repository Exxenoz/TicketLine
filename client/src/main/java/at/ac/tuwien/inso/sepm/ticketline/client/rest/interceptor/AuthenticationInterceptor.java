package at.ac.tuwien.inso.sepm.ticketline.client.rest.interceptor;

import at.ac.tuwien.inso.sepm.ticketline.client.service.AuthenticationInformationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationInterceptor implements ClientHttpRequestInterceptor {

    private final AuthenticationInformationService authenticationInformationService;

    public AuthenticationInterceptor(AuthenticationInformationService authenticationInformationService) {
        this.authenticationInformationService = authenticationInformationService;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        authenticationInformationService.getCurrentAuthenticationToken().ifPresent(
            authenticationToken -> headers.add("Authorization", "Bearer " + authenticationToken));
        return execution.execute(request, body);
    }
}
