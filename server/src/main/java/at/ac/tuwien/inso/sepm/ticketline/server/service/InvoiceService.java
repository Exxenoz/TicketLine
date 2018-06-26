package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalInvoiceGenerationException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalNotFoundException;
import org.springframework.core.io.Resource;

public interface InvoiceService {

    /**
     * Generates an invoice and stores it locally as file on the server
     * @param reservationNumber the reservation number of the reservation for which we create an invoice
     * @return the path of generated PDF
     * @throws InternalNotFoundException in case the reservation for the given id was not found
     * @throws InternalInvoiceGenerationException in case anything goes wrong during PDF creation
     */
    void generateInvoice(String reservationNumber) throws InternalNotFoundException, InternalInvoiceGenerationException;

    /**
     * Generates a cancellation invoice and returns the path of the file on the server
     * @param reservationNumber the reservation number of the cancelled reservation for which we create an invoice
     * @throws InternalNotFoundException in case the reservation for the given id was not found
     * @throws InternalInvoiceGenerationException in case anything goes wrong during PDF creation
     */
    void generateCancellationInvoice(String reservationNumber) throws InternalNotFoundException, InternalInvoiceGenerationException;

    /**
     * Combines the actions of generating an invoice and serving it
     * @param reservationNumber the reservation number of the reservation for which we create an invoice
     * @return the generated PDF as resource
     * @throws InternalNotFoundException in case the reservation for the given id was not found
     */
    Resource generateAndServeInvoice(String reservationNumber) throws InternalNotFoundException, InternalInvoiceGenerationException;

    /**
     * Combines the actions of generating a cancellation invoice and servin it
     * @param reservationNumber the reservation number of the reservation for which we create a cancellation invoice
     * @return the generated cancellation PDF as invoice
     * @throws InternalNotFoundException in case the reservation for the given id was not found
     * @throws InternalInvoiceGenerationException in case anything goes wrong during PDf creation
     */
    Resource generateAndServeCancellationInvoice(String reservationNumber) throws InternalNotFoundException, InternalInvoiceGenerationException;

    /**
     * Serve an invoice from the server
     * @param reservationNumber the reservation number of the reservation for which we want to serve the invoice
     * @return the invoice that was requested as resource
     * @throws InternalNotFoundException in case the reservation for the given id was not found
     */
    Resource serveInvoice(String reservationNumber) throws InternalNotFoundException;

    /**
     * Deletes an invoice PDF-file
     * @param reservationNumber the reservation id of the reservation for which the invoice has to be deleted
     * @throws InternalNotFoundException in case the reservation for the given id was not found
     */
    void deletePDF(String reservationNumber) throws InternalNotFoundException;

}
