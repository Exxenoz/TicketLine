package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.InvoiceRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.InvoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.invoke.MethodHandles;

@Service
public class SimpleInvoiceService implements InvoiceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final InvoiceRestClient restClient;

    public SimpleInvoiceService(InvoiceRestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public void downloadPDF(String reservationNumber) throws DataAccessException {
        restClient.downloadPDF(reservationNumber);
    }

    @Override
    public void downloadAndStorePDF(String reservationNumber, File file) throws DataAccessException {
        downloadPDF(reservationNumber);
        storePDF(file);
    }

    @Override
    public void storePDF(File file) {

    }

    @Override
    public void openPDF(File file) {

    }

    @Override
    public void deletePDF(File file) {

    }
}
