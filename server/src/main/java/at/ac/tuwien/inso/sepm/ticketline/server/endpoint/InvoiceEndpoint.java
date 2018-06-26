package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.server.exception.endpoint.HttpNotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalInvoiceGenerationException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalNotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.service.InvoiceService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ReservationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(value="/invoice")
@Api(value="invoice")
public class InvoiceEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final InvoiceService invoiceService;

    public InvoiceEndpoint(ReservationService reservationService, InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/{reservationNumber}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Creates an invoice for a reservation and serves it")
    public ResponseEntity<Resource> createAndServeInvoice(@PathVariable String reservationNumber) {
        try {
            Resource file = invoiceService.generateAndServeInvoice(reservationNumber);

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        } catch(InternalNotFoundException n) {
            throw new HttpNotFoundException();
        } catch (InternalInvoiceGenerationException g) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{reservationNumber}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Serves an invoice")
    public ResponseEntity<Resource> serveInvoice(@PathVariable String reservationNumber) {
        try {
            Resource file = invoiceService.serveInvoice(reservationNumber);

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        } catch (InternalNotFoundException n) {
            throw new HttpNotFoundException();
        }
    }

    @PutMapping("/{reservationNumber}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Updates the invoice for a cancelled reservation and serves it")
    public ResponseEntity<Resource> updateForCancellationAndServeInvoice(@PathVariable String reservationNumber) {
        try {
            //First we delete the concerning actual PDF
            try {
                invoiceService.deletePDF(reservationNumber);
            } catch (InternalNotFoundException i) {
                LOGGER.debug("Could not find pdf-File for that reservation number");
            }

            //The we generate a new one and serve it
            Resource file = invoiceService.generateAndServeCancellationInvoice(reservationNumber);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        } catch(InternalNotFoundException n) {
            throw new HttpNotFoundException();
        } catch (InternalInvoiceGenerationException g) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{reservationID}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Deletes the invoice for a cancelled reservation")
    public void deleteInvoice(@PathVariable String reservationID) {
        try {
            invoiceService.deletePDF(reservationID);
        } catch (InternalNotFoundException n) {
            throw new HttpNotFoundException();
        }
    }
}
