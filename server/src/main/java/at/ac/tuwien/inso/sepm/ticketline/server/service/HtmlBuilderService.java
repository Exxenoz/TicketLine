package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Reservation;

public interface HtmlBuilderService {

    /**
     * Creates an Html representation for a given reservation
     * @param reservation the reservation we build an Html representation for
     * @return the html source code
     */
    String buildInvoiceHtml(Reservation reservation);
}
