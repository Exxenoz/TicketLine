package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.InvoiceRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.lang.invoke.MethodHandles;
import java.net.URI;

import static org.springframework.http.HttpMethod.POST;

@Component
public class SimpleInvoiceRestClient implements InvoiceRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final static String INVOICE_LOCATION = "/invoice/";

    private final RestClient restClient;
    private final URI invoiceURI;

    public SimpleInvoiceRestClient(RestClient restClient) {
        this.restClient = restClient;
        this.invoiceURI = restClient.getServiceURI(INVOICE_LOCATION);
    }

    @Override
    public void downloadPDF(String reservationNumber) throws DataAccessException {
        try {
            final var response =
                restClient.exchange(
                    new RequestEntity<>(reservationNumber, POST, invoiceURI.resolve(reservationNumber)),
                    new ParameterizedTypeReference<String>() {
                    });
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()));
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
