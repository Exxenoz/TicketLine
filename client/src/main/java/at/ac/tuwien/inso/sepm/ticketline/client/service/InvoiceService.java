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
    void downloadPDF(String reservationNumber) throws DataAccessException, InvoiceFileException;

    /**
     * Stores a downloaded PDF
     * @param file the PDF-file that will be stored
     */
    void storePDF(File file);

    /**
     * Combines the actions of downloading and storing a PDF
     * @param reservationNumber the established generated and unique identifier of a reservation
     * @param file the PDF-file that will be stored after downloading
     * @throws DataAccessException in case something went wrong during API call
     */
    void downloadAndStorePDF(String reservationNumber, File file) throws DataAccessException, InvoiceFileException;

    /**
     * Opens an existing PDF
     * @param file the PDF-file that will be opened
     */
    void openPDF(File file);

    /**
     * Deletes an existing PDF locally on the client
     * @param file the PDF-file that will be deleted
     */
    void deletePDF(File file);
}
