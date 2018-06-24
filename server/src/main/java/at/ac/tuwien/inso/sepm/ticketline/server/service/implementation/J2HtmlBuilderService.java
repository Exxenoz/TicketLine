package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.util.PriceUtils;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Reservation;
import at.ac.tuwien.inso.sepm.ticketline.server.service.HtmlBuilderService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ReservationService;
import at.ac.tuwien.inso.sepm.ticketline.server.util.BundleManager;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static j2html.TagCreator.*;

@Service
public class J2HtmlBuilderService implements HtmlBuilderService {

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final ReservationService reservationService;

    public J2HtmlBuilderService(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Override
    public String buildBasicInvoiceHtml(Reservation reservation) {
        String source = body(

            //Company information
            div(
                tbody(
                    tr(
                        th(BundleManager.getBundle().getString("invoice.company")),
                        th(BundleManager.getBundle().getString("invoice.uid")),
                        th(BundleManager.getBundle().getString("invoice.address.header"))
                    ),
                    tr(
                        td(BundleManager.getBundle().getString("invoice.company.name")),
                        td(BundleManager.getBundle().getString("invoice.uid.number")),
                        td(BundleManager.getBundle().getString("invoice.address"))
                    )
                ),
                tbody(
                    tr(
                        th(BundleManager.getBundle().getString("invoice.creation.date"))
                    ),
                    tr(
                        td(DATETIME_FORMATTER.format(LocalDateTime.now()))
                    )
                )
            ),

            //Some polite phrasing and performance information phrasing
            h1(BundleManager.getBundle().getString("invoice.header")),
            div(
                text(
                    BundleManager.getBundle().getString("invoice.thankyou") +
                        BundleManager.getBundle().getString("invoice.performance.info")
                )
            ),

            //Performance information
            div(
                text(BundleManager.getBundle().getString("invoice.performance") + ": " +
                    reservation.getPerformance().getName()),
                text(BundleManager.getBundle().getString("invoice.performance.date") + ": " +
                    DATETIME_FORMATTER.format(reservation.getPerformance().getPerformanceStart())),
                text(BundleManager.getBundle().getString("invoice.artist") + ": "),
                each(reservation.getPerformance().getArtists(), artist ->
                    text(artist.getFirstName() + ", ")
                )
            ),

            //Ticket info phrasing
            div(
                text(BundleManager.getBundle().getString("invoice.tickets.intro") + ":"),
                //Ticket information
                tbody(
                    tr(
                        th(BundleManager.getBundle().getString("invoice.sector")),
                        th(BundleManager.getBundle().getString("invoice.row")),
                        th(BundleManager.getBundle().getString("invoice.seat"))
                    ),
                    each(reservation.getSeats(), seat ->
                        tr(
                            td(Integer.toString(seat.getSector().getHallNumber())),
                            //Make positions human friendly by adding 1
                            td(Integer.toString(seat.getPositionY() + 1)),
                            td(Integer.toString(seat.getPositionX() + 1))
                        )
                    )
                )
            ),

            //Pricing
            tbody(
                tr(
                    th(BundleManager.getBundle().getString("invoice.price.pretax")),
                    td(PriceUtils.priceToRepresentation(reservationService.calculatePreTaxPrice(reservation)))
                ),
                tr(
                    th(BundleManager.getBundle().getString("invoice.price.tax")),
                    td(Double.toString(reservationService.getRegularTaxRate()) + "% " +
                    BundleManager.getBundle().getString("invoice.vat")))
                ,
                tr(
                    th(BundleManager.getBundle().getString("invoice.price.taxed")),
                    td(PriceUtils.priceToRepresentation(reservationService.calculatePrice(reservation))
                        + BundleManager.getBundle().getString("invoice.include.vat"))
                )
            ),

            text(BundleManager.getBundle().getString("invoice.performance.paidat")
                + ":"
                + "\n"
                + DATETIME_FORMATTER.format(reservation.getPaidAt())),

            //Goodbye
            div(
                text(BundleManager.getBundle().getString("invoice.wishes")),
                text(BundleManager.getBundle().getString("invoice.yourteam"))
            )
        ).render();

        return source;
    }

    @Override
    public String buildDetailedInvoiceHtml(Reservation reservation) {
        return null;
    }

    @Override
    public String buildBasicCancellationHtml(Reservation reservation) {
        return null;
    }

    @Override
    public String buildDetailedCancellationHtml(Reservation reservation) {
        return null;
    }
}
