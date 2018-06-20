package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Reservation;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalInvoiceGenerationException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalNotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.service.HtmlBuilderService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.InvoiceService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ReservationService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class SimpleInvoiceService implements InvoiceService {

    private final ReservationService reservationService;
    private final HtmlBuilderService htmlBuilderService;

    public SimpleInvoiceService(ReservationService reservationService, HtmlBuilderService htmlBuilderService) {
        this.reservationService = reservationService;
        this.htmlBuilderService = htmlBuilderService;
    }

    @Override
    public String generateInvoice(String reservationNumber) throws InternalNotFoundException, InternalInvoiceGenerationException {
        Reservation reservation = reservationService.findOneByReservationNumber(reservationNumber);

        if(reservation == null) {
            throw new InternalNotFoundException();
        }

        //First we want to transform the reservation to HTML
        String source = htmlBuilderService.buildHtmlForReservation(reservation);

        return null;
    }

    @Override
    public String generateCancellationInvoice(String reservationNumber) throws InternalNotFoundException, InternalInvoiceGenerationException {
        return null;
    }

    @Override
    public Resource generateAndServeInvoice(String reservationNumber) throws InternalNotFoundException, InternalInvoiceGenerationException {
        return null;
    }

    @Override
    public Resource serveInvoice(String reservationNumber) throws InternalNotFoundException {
        return null;
    }

    @Override
    public void deletePDF(String reservationNumber) throws InternalNotFoundException {

    }

    @Override
    public void deletePDFByPath(String path) throws InternalNotFoundException {

    }
}
