package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.configuration.properties.RestClientConfigurationProperties;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class RestClient extends RestTemplate {

    private final String baseUrl;

    public RestClient(
        List<ClientHttpRequestInterceptor> interceptors,
        RestClientConfigurationProperties restClientConfigurationProperties
    ) {
        super(new HttpComponentsClientHttpRequestFactory(HttpClients
            .custom()
            .setConnectionManager(new PoolingHttpClientConnectionManager())
            .build()));
        setInterceptors(interceptors);
        String computedBaseUrl = restClientConfigurationProperties.getRemote().getFullUrl();
        if (computedBaseUrl.endsWith("/")) {
            computedBaseUrl = computedBaseUrl.substring(0, computedBaseUrl.length() - 1);
        }
        baseUrl = computedBaseUrl;
    }

    /**
     * Get the full URI of the endpoint.
     *
     * @param serviceLocation the endpoints location
     * @return full URI of the endpoint
     */
    URI getServiceURI(String serviceLocation) {
        if (!serviceLocation.startsWith("/")) {
            return URI.create(baseUrl + "/" + serviceLocation);
        }
        return URI.create(baseUrl + serviceLocation);
    }

}