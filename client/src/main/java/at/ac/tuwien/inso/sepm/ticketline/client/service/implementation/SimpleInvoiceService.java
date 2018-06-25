package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.configuration.properties.InvoiceConfigurationProperties;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.InvoiceFileException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.InvoiceRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.InvoiceService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.DesktopApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class SimpleInvoiceService implements InvoiceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final InvoiceRestClient restClient;
    private final InvoiceConfigurationProperties invoiceConfigurationProperties;

    public SimpleInvoiceService(InvoiceRestClient restClient, InvoiceConfigurationProperties invoiceConfigurationProperties) {
        this.restClient = restClient;
        this.invoiceConfigurationProperties = invoiceConfigurationProperties;
    }

    @Override
    public byte[] downloadPDF(String reservationNumber) throws DataAccessException, InvoiceFileException {
        return restClient.createInvoice(reservationNumber);
    }

    @Override
    public void downloadAndStorePDF(String reservationNumber) throws DataAccessException, InvoiceFileException {
        byte[] pdf =  downloadPDF(reservationNumber);
        storePDF(reservationNumber, pdf);
    }

    @Override
    public void storePDF(String reservationNumber, byte[] pdf) throws InvoiceFileException{
        try {
            Files.write(Paths.get(invoiceConfigurationProperties.getLocation() + "/" + reservationNumber + ".pdf"), pdf);
        } catch (IOException io) {
            throw new InvoiceFileException(BundleManager.getExceptionBundle().getString("exception.invoice.file"));
        }
    }

    @Override
    public void openPDF(String reservationNumber) throws InvoiceFileException {
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File(invoiceConfigurationProperties.getLocation() + "/" + reservationNumber + ".pdf");
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                LOGGER.warn("Couldn't open PDF-File");
                throw new InvoiceFileException(BundleManager.getExceptionBundle().getString("exception.invoice.file.open"));
            }
        } else {
            File myFile = new File(invoiceConfigurationProperties.getLocation() + "/" + reservationNumber + ".pdf");
            DesktopApi.open(myFile);
        }
    }

    @Override
    public void deletePDF(File file) {

    }
}
