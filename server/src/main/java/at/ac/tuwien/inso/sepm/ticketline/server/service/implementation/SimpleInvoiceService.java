package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.configuration.properties.InvoiceConfigurationProperties;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Reservation;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.ExternalRendererException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalInvoiceGenerationException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalNotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.service.HtmlBuilderService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.InvoiceService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ReservationService;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class SimpleInvoiceService implements InvoiceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ReservationService reservationService;
    private final HtmlBuilderService htmlBuilderService;

    private final Path invoiceLocation;

    public SimpleInvoiceService(ReservationService reservationService, HtmlBuilderService htmlBuilderService,
                                InvoiceConfigurationProperties invoiceConfigurationProperties) {
        this.reservationService = reservationService;
        this.htmlBuilderService = htmlBuilderService;
        this.invoiceLocation = Paths.get(invoiceConfigurationProperties.getLocation());
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
    public void generateInvoice(String reservationNumber) throws InternalNotFoundException, InternalInvoiceGenerationException {
        Reservation reservation = reservationService.findOneByReservationNumber(reservationNumber);

        if (reservation == null) {
            throw new InternalNotFoundException("Could not find reservation for reservation number: " + reservationNumber);
        }
        //First we want to transform the reservation to HTML
        String source = htmlBuilderService.buildBasicInvoiceHtml(reservation);

        //Then we render it to pdf and store it as file, we use the reservation number as invoice name
        try {
            runRendererBuilder(source, reservation.getReservationNumber());
        } catch (ExternalRendererException | IOException e) {
            throw new InternalInvoiceGenerationException();
        }
    }

    @Override
    public String generateCancellationInvoice(String reservationNumber) throws InternalNotFoundException, InternalInvoiceGenerationException {
        return null;
    }

    @Override
    public Resource generateAndServeInvoice(String reservationNumber) throws InternalNotFoundException, InternalInvoiceGenerationException {
        generateInvoice(reservationNumber);
        return serveInvoice(reservationNumber);
    }

    @Override
    public Resource serveInvoice(String reservationNumber) throws InternalNotFoundException {
        //Then get the file to put it into response
        try {
            Path file = load(reservationNumber + ".pdf");
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new InternalNotFoundException("Could not find generated invoice file.");
            }
        } catch (MalformedURLException m) {
            throw new InternalNotFoundException("URL for PDF itself could not be resolved.");
        }
    }

    @Override
    public void deletePDF(String reservationNumber) throws InternalNotFoundException {

    }

    @Override
    public void deletePDFByPath(String path) throws InternalNotFoundException {

    }

    private void runRendererBuilder(String richText, String name) throws IOException, ExternalRendererException {
        try {
            OutputStream outputStream = new FileOutputStream(invoiceLocation.toString() + "/" + name + ".pdf");
            PdfRendererBuilder pdfRendererBuilder = new PdfRendererBuilder();
            pdfRendererBuilder.withHtmlContent(richText, "");
            pdfRendererBuilder.toStream(outputStream);
            pdfRendererBuilder.run();
        } catch (Exception e) {
            throw new ExternalRendererException("Rendering did not complete.");
        }
    }

    private Path load(String name) {
        return invoiceLocation.resolve(name);
    }
}
