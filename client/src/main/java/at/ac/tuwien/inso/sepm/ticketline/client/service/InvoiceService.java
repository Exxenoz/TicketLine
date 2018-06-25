package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.InvoiceFileException;

import java.io.File;

public interface InvoiceService {

    /**
     * Downloads a PDF from the server
     * @param reservationNumber the established generated and unique identifier of a reservation
     * @throws DataAccessException in case something went wrong during API call
     */
    byte[] createInvoice(String reservationNumber) throws DataAccessException, InvoiceFileException;

    /**
     * Storing a PDF
     * @param reservationNumber the established generated and unique identifier of a reservation
     * @param pdf the pdf file that will be stored as byte-array
     */
    void storeInvoice(String reservationNumber, byte[] pdf) throws InvoiceFileException;

    /**
     * Combines the actions of downloading and storing a PDF
     * @param reservationNumber the established generated and unique identifier of a reservation
     * @throws DataAccessException in case something went wrong during API call
     */
    void createAndStoreInvoice(String reservationNumber) throws DataAccessException, InvoiceFileException;

    /**
     * Opens an existing PDF
     * @param reservationNumber The reservation number of the PDF-file that will be opened
     */
    void openInvoice(String reservationNumber) throws InvoiceFileException;

    /**
     * Deletes an existing PDF locally on the client
     * @param file the PDF-file that will be deleted
     */
    void deletePDF(File file);
}
