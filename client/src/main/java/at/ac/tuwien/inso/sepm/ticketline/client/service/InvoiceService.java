package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;

import java.io.File;

public interface InvoiceService {

    void downloadPDF(String reservationNumber);

    void storePDF(File file);

    void openPDF(File file);

    void deletePDF(File file);
}
