package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Reservation;

public interface HtmlBuilderService {

    /**
     * Creates a basic Html invoice representation for a given reservation.
     *
     * @param reservation the reservation we build a basic invoice Html representation for
     * @return the html source code
     */
    String buildBasicInvoiceHtml(Reservation reservation);

    /**
     * Creates a detailed Html invoice representation for a given reservation
     *
     * @param reservation the reservation we build a detailed invoice Html representation for
     * @return the html source code
     */
    String buildDetailedInvoiceHtml(Reservation reservation);

    /**
     * Creates a basic Html cancellation representation for a given reservation
     *
     * @param reservation the reservation we build a basic cancellation Html representation for
     * @return the html source code
     */
    String buildBasicCancellationHtml(Reservation reservation);

    /**
     * Creates a detailed Html cancellation representation for a given reservation
     *
     * @param reservation the reservation we build a detailed cancellation Html representation for
     * @return the html source code
     */
    String buildDetailedCancellationHtml(Reservation reservation);

}
