package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.util.PriceUtils;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.EventType;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Reservation;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.service.HtmlBuilderService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ReservationService;
import at.ac.tuwien.inso.sepm.ticketline.server.util.BundleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import static j2html.TagCreator.*;

@Service
public class J2HtmlBuilderService implements HtmlBuilderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final String CSS_STYLE =
        "* {\n" +
            "        font-family: Arial, \"Helvetica Neue\", Helvetica, sans-serif !important;\n" +
            "    }\n" +
            "\n" +
            "    table {\n" +
            "        background-color: cornflowerblue;\n" +
            "        border: 1px solid #ddd;\n" +
            "        margin-left: 8px;\n" +
            "        margin-right: 8px;\n" +
            "    }\n" +
            "\n" +
            "    th {\n" +
            "        text-align: left;\n" +
            "    }\n" +
            "\n" +
            "    td, th {\n" +
            "        width: auto\n" +
            "    }\n" +
            "\n" +
            "    .bordered-div {\n" +
            "        border-top: 2px solid lightgray;\n" +
            "        margin-top: 16px;\n" +
            "        margin-bottom: 16px;\n" +
            "        padding: 10px;\n" +
            "    }\n" +
            "\n" +
            "    .company-info {\n" +
            "        background-color: lightgrey;\n" +
            "        display: block;\n" +
            "        margin-left: 40%;\n" +
            "        margin-right: auto;\n" +
            "        width: auto;\n" +
            "        font-size: 12px;\n" +
            "    }\n" +
            "\n" +
            "    .regular-table {\n" +
            "        clear: left;\n" +
            "        display: block;\n" +
            "        margin-left: 0%;\n" +
            "        margin-right: auto;\n" +
            "        width: 60%;\n" +
            "    }\n" +
            "\n" +
            "    .regular-table tr td {\n" +
            "       padding: 8px;\n" +
            "        width: 40%;\n" +
            "    }\n" +
            "\n" +
            "    .regular-table tr th {\n" +
            "        padding: 8px;\n" +
            "        width: 40%;\n" +
            "    }\n" +
            "\n" +
            "    .block {\n" +
            "        display: block;\n" +
            "    }\n" +
            "\n" +
            "    .offset-top {\n" +
            "        margin-top: 20px;\n" +
            "    }\n" +
            "\n" +
            "    .large-font {\n" +
            "        font-size: 16px;\n" +
            "    }\n";

    private final ReservationService reservationService;

    public J2HtmlBuilderService(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Override
    public String buildBasicInvoiceHtml(Reservation reservation) {
        String source =
            //Adding head manually to enable style sheet href
            html(
                head(
                    style(CSS_STYLE)
                ),
                body(
                    //Company information
                    div(
                        table(
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
                        ).withClass("company-info")
                    ),

                    //Some polite phrasing and performance information phrasing
                    h1(BundleManager.getBundle().getString("invoice.header")),
                    div(
                        text(
                            BundleManager.getBundle().getString("invoice.thankyou")
                        )
                    ).withClass("block"),

                    //Performance information
                    h3(BundleManager.getBundle().getString("invoice.performance.info") + ":"),
                    div(
                        table(
                            tr(
                                th(BundleManager.getBundle().getString("invoice.performance")),
                                td(reservation.getPerformance().getName())
                            ),
                            tr(
                                th(BundleManager.getBundle().getString("invoice.performance.date")),
                                td(DATETIME_FORMATTER.format(reservation.getPerformance().getPerformanceStart()))
                            ),
                            tr(
                                th(BundleManager.getBundle().getString("invoice.artist")),
                                td(buildArtistsRepresentation(reservation.getPerformance().getArtists()))
                            )
                        ).withClass("regular-table")
                    ).withClass("block")
                ),

                //Ticket info phrasing
                div(
                    h3(BundleManager.getBundle().getString("invoice.tickets.intro") + ":"),
                    //Ticket information
                    table(
                        tr(
                            th(),
                            th(BundleManager.getBundle().getString("invoice.sector")),
                            th(BundleManager.getBundle().getString("invoice.row")),
                            th(BundleManager.getBundle().getString("invoice.seat"))
                        ),
                        each(reservation.getSeats(), seat ->
                            tr(
                                td(BundleManager.getBundle().getString("invoice.ticket")
                                    + " " + getCountForSeat(reservation.getSeats(), seat)),
                                td(Integer.toString(seat.getSector().getHallNumber())),
                                //Make positions human friendly by adding 1
                                td(getSeatYRepresentation(seat, reservation)),
                                td(getSeatXRepresentation(seat, reservation))
                            )
                        )
                    ).withClass("regular-table")
                ).withClass("bordered-div"),

                //Pricing
                div(
                    table(
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
                                + " " + BundleManager.getBundle().getString("invoice.include.vat"))
                        )
                    ).withClass("regular-table")
                ).withClass("bordered-div"),
                div(
                    h4(BundleManager.getBundle().getString("invoice.performance.paidat")
                        + ":"
                        + "\n"
                        + DATETIME_FORMATTER.format(reservation.getPaidAt()))
                ),

                //Goodbye
                div(
                    text(BundleManager.getBundle().getString("invoice.wishes"))
                ).withClasses("block", "offset-top", "large-font"),
                div(
                    text(BundleManager.getBundle().getString("invoice.yourteam"))
                ).withClasses("block", "large-font")
            ).render();
        LOGGER.debug("Created html: {}", source);
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

    private int getCountForSeat(List<Seat> seats, Seat seat) {
        int i = 0;
        for (Seat s : seats) {
            i++;
            if (s.equals(seat)) {
                return i;
            }
        }
        return i;
    }

    private String buildArtistsRepresentation(Set<Artist> artists) {
        String out = "";
        int i = 0;
        for (Artist a : artists) {
            out += a.getFirstName();

            if (i < artists.size() - 1) {
                out += ", ";
            }
            i++;
        }

        return out;
    }

    private String getSeatXRepresentation(Seat seat, Reservation reservation) {
        if(reservation.getPerformance().getEvent().getEventType() == EventType.SECTOR) {
            return BundleManager.getBundle().getString("invoice.free.seating");
        } else {
            return Integer.toString(seat.getPositionX() + 1);
        }
    }

    private String getSeatYRepresentation(Seat seat, Reservation reservation) {
        if(reservation.getPerformance().getEvent().getEventType() == EventType.SECTOR) {
            return BundleManager.getBundle().getString("invoice.free.seating");
        } else {
            return Integer.toString(seat.getPositionY() + 1);
        }
    }
}
