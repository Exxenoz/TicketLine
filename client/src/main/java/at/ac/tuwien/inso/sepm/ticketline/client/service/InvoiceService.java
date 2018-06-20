package at.ac.tuwien.inso.sepm.ticketline.client.service;

import java.io.File;

public interface InvoiceService {

    /**
     * Downloads a PDF from the server
     * @param reservationNumber the established generated and unique identifier of a reservation
     */
    void downloadPDF(String reservationNumber);

    /**
     * Stores a downloaded PDF
     * @param file the PDF-file that will be stored
     */
    void storePDF(File file);

    /**
     * Combines the actions of downloading and storing a PDF
     * @param reservationNumber the established generated and unique identifier of a reservation
     * @param file the PDF-file that will be stored after downloading
     */
    void downdloadAndStorePDF(String reservationNumber, File file);

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
