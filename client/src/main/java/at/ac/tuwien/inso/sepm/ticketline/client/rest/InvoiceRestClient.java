package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.InvoiceFileException;

import java.io.File;

public interface InvoiceRestClient {

    /**
     * Creates an invoice for a reservation and fetches it
     * @param reservationNumber the reservation number of the reservation we create the invoice for
     * @throws DataAccessException in case anything goes wrong during invoice creation
     */
    byte[] createInvoice(String reservationNumber) throws DataAccessException, InvoiceFileException;

    /**
     * Simply fetches an invoice
     * @param reservationNumber the reservation number of the reservation that we want to fetch
     * @throws DataAccessException in case anything goes wrong during invoice fetching
     */
    void getInvoice(String reservationNumber) throws DataAccessException;

    /**
     * Creates and fetches a cancellaton invoice
     * @param reservationNumber the reservation number of the reservation that we create the cancellation invoice for
     * @throws DataAccessException in case anything goes wrong during cancellation invoice creation
     */
    void createCancellationInvoice(String reservationNumber) throws DataAccessException;

    /**
     * Deletes an invoice
     * @param reservationNumber the reservation number of the reservation that we want to delete
     * @throws DataAccessException in case anything goes wrong during deletion
     */
    void deleteInvoice(String reservationNumber) throws DataAccessException;

}
