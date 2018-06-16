package at.ac.tuwien.inso.sepm.ticketline.client.service;

import java.io.File;

public interface InvoiceService {

    void downloadPDF(String reservationNumber);

    void downdloadAndStorePDF(String reservationNumber, File file);

    void storePDF(File file);

    void openPDF(File file);

    void deletePDF(File file);
}
