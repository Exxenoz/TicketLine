package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.service.InvoiceService;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class SimpleInvoiceService implements InvoiceService {

    @Override
    public void downloadPDF(String reservationNumber) {

    }

    @Override
    public void downdloadAndStorePDF(String reservationNumber, File file) {
        downloadPDF(reservationNumber);
        storePDF(file);
    }

    @Override
    public void storePDF(File file) {

    }

    @Override
    public void openPDF(File file) {

    }

    @Override
    public void deletePDF(File file) {

    }
}
