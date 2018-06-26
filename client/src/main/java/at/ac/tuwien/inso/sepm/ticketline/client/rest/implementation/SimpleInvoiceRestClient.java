package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.configuration.properties.InvoiceConfigurationProperties;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.InvoiceFileException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.InvoiceRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.http.HttpMethod.*;

@Component
public class SimpleInvoiceRestClient implements InvoiceRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final static String INVOICE_LOCATION = "/invoice/";

    private final RestClient restClient;
    private final URI invoiceURI;

    private final InvoiceConfigurationProperties invoiceConfigurationProperties;

    private final Path invoiceLocation;

    public SimpleInvoiceRestClient(RestClient restClient, InvoiceConfigurationProperties invoiceConfigurationProperties) {
        this.restClient = restClient;
        this.invoiceURI = restClient.getServiceURI(INVOICE_LOCATION);
        this.invoiceConfigurationProperties = invoiceConfigurationProperties;
        invoiceLocation = Paths.get(invoiceConfigurationProperties.getLocation());
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(invoiceLocation);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("Could not create directory for invoice PDF storing!");
        }
    }

    @Override
    public byte[] createInvoice(String reservationNumber) throws DataAccessException  {
        try {
            ResponseEntity<byte[]> response =
                restClient.exchange(
                    new RequestEntity<>(reservationNumber, POST, invoiceURI.resolve(reservationNumber)),
                    byte[].class);

            if(response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                return null;
            }
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()));
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public byte[] getInvoice(String reservationNumber) throws DataAccessException {
        try {
            ResponseEntity<byte[]> response =
                restClient.exchange(
                    new RequestEntity<>(reservationNumber, GET, invoiceURI.resolve(reservationNumber)),
                    byte[].class);

            if(response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                return null;
            }
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()));
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public byte[] createCancellationInvoice(String reservationNumber) throws DataAccessException {
        try {
            ResponseEntity<byte[]> response =
                restClient.exchange(
                    new RequestEntity<>(reservationNumber, PUT, invoiceURI.resolve(reservationNumber)),
                    byte[].class);

            if(response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                return null;
            }
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()));
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteInvoice(String reservationNumber) throws DataAccessException {
//        try {
//            ResponseEntity<String> response =
//                restClient.exchange(
//                    new RequestEntity<>(reservationNumber, DELETE, invoiceURI.resolve(reservationNumber), String);
//
//        } catch (HttpStatusCodeException e) {
//            throw new DataAccessException(restClient.getMessageFromHttpStatusCode(e.getStatusCode()));
//        } catch (RestClientException e) {
//            throw new DataAccessException(e.getMessage(), e);
//        }
    }
}
