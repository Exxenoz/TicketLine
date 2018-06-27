package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.configuration.properties.InvoiceConfigurationProperties;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.InvoiceFileException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.InvoiceRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.InvoiceService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.OSDeterminationService;
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
    private final OSDeterminationService osDeterminationService;

    public SimpleInvoiceService(InvoiceRestClient restClient, InvoiceConfigurationProperties invoiceConfigurationProperties,
                                OSDeterminationService osDeterminationService) {
        this.restClient = restClient;
        this.invoiceConfigurationProperties = invoiceConfigurationProperties;
        this.osDeterminationService = osDeterminationService;
    }

    @Override
    public byte[] createInvoice(String reservationNumber) throws DataAccessException, InvoiceFileException {
        byte[] responseBody = restClient.createInvoice(reservationNumber);
        if(responseBody != null) {
            return responseBody;
        } else {
            throw new InvoiceFileException(BundleManager.getBundle().getString("exception.invoice.fetch"));
        }
    }

    @Override
    public void createAndStoreInvoice(String reservationNumber) throws DataAccessException, InvoiceFileException {
        byte[] pdf =  createInvoice(reservationNumber);
        storeInvoice(reservationNumber, pdf);
    }

    @Override
    public byte[] createCancellationInvoice(String reservationNumber) throws DataAccessException, InvoiceFileException {
       byte[] responseBody = restClient.createCancellationInvoice(reservationNumber);
       if(responseBody != null) {
           return responseBody;
       } else {
           throw new InvoiceFileException(BundleManager.getBundle().getString("exception.invoice.fetch"));
       }
    }

    @Override
    public void createAndStoreCancellationInvoice(String reservationNumber) throws DataAccessException, InvoiceFileException {
        //We can assume that there is already a reservation before
        deleteInvoice(reservationNumber);
        byte[] pdf = createCancellationInvoice(reservationNumber);
        storeInvoice(reservationNumber, pdf);
    }

    @Override
    public void storeInvoice(String reservationNumber, byte[] pdf) throws InvoiceFileException{
        try {
            Files.write(Paths.get(invoiceConfigurationProperties.getLocation() + "/" + reservationNumber + ".pdf"), pdf);
        } catch (IOException io) {
            throw new InvoiceFileException(BundleManager.getExceptionBundle().getString("exception.invoice.file"));
        }
    }

    @Override
    public void openInvoice(String reservationNumber) throws InvoiceFileException {
        File file = new File(invoiceConfigurationProperties.getLocation() + "/" + reservationNumber + ".pdf");

        try {
            if (osDeterminationService.isWindows()) {
                Runtime.getRuntime().exec(new String[]
                    {"rundll32", "url.dll,FileProtocolHandler",
                        file.getAbsolutePath()});

            } else if (osDeterminationService.isMac()) {
                Runtime.getRuntime().exec("open " + file.getPath());
            } else if(osDeterminationService.isUnix()) {
                Runtime.getRuntime().exec("xdg-open " + file.getPath());
            }
        } catch (IOException i) {
            LOGGER.debug("Could not open PFD: {}", i.getMessage());
            throw new InvoiceFileException(BundleManager.getExceptionBundle().getString("exception.invoice.file.open"));
        }
    }

    @Override
    public void deleteInvoice(String reservationNumber) {
        File file = new File(invoiceConfigurationProperties.getLocation() + "/" + reservationNumber + ".pdf");

        if(file.exists()) {
            file.delete();
        }
    }
}
