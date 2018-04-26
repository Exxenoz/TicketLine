package at.ac.tuwien.inso.sepm.ticketline.client.rest.interceptor;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class MediaTypeInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        final var headers = request.getHeaders();
        headers.setContentType(APPLICATION_JSON);
        headers.getAccept().add(APPLICATION_JSON);
        return execution.execute(request, body);
    }

}
