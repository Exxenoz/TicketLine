package at.ac.tuwien.inso.sepm.ticketline.client.rest.interceptor;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;

@Component
public class LocaleInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        final var headers = request.getHeaders();
        headers.add(ACCEPT_LANGUAGE, LocaleContextHolder.getLocale().toLanguageTag());
        return execution.execute(request, body);
    }

}
