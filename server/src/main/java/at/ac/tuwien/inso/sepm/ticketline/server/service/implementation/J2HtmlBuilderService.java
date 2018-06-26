package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.util.PriceUtils;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.service.HtmlBuilderService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ReservationService;
import at.ac.tuwien.inso.sepm.ticketline.server.util.BundleManager;
import j2html.tags.ContainerTag;
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
            "        padding-right: 12px;\n" +
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
            "        margin-left: 30%;\n" +
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
            "        padding: 8px;\n" +
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
            "    }";

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
                    buildCompanyInformationAndDateDiv(),

                    //Some polite phrasing and performance information phrasing
                    h1(BundleManager.getBundle().getString("invoice.header")),
                    div(
                        text(
                            BundleManager.getBundle().getString("invoice.thankyou")
                        )
                    ).withClass("block"),

                    //Performance information
                    h3(BundleManager.getBundle().getString("invoice.performance.info") + ":"),
                    buildPerformanceInformationDiv(reservation),

                    //Ticket info phrasing
                    h3(BundleManager.getBundle().getString("invoice.tickets.intro") + ":"),
                    buildTicketInformationDiv(reservation),

                    //Pricing
                    h3(BundleManager.getBundle().getString("invoice.pricing") + ": "),
                    buildPricingInformationDiv(reservation),

                    //Paid date
                    buildPaidDateDiv(reservation),

                    //Goodbye
                    div(
                        text(BundleManager.getBundle().getString("invoice.wishes"))
                    ).withClasses("block", "offset-top", "large-font"),
                    div(
                        text(BundleManager.getBundle().getString("invoice.yourteam"))
                    ).withClasses("block", "large-font")
                )
            ).render();
        LOGGER.debug("Created html: {}", source);
        return source;
    }

    @Override
    public String buildDetailedInvoiceHtml(Reservation reservation) {
        String source =
            //Adding head manually to enable style sheet href
            html(
                head(
                    style(CSS_STYLE)
                ),
                body(
                    //Company information
                    buildCompanyInformationAndDateDiv(),
                    h1(BundleManager.getBundle().getString("invoice.header")),

                    //Customer information
                    h3(BundleManager.getBundle().getString("invoice.dear")),
                    buildCustomerInformationDiv(reservation),

                    //Some polite phrasing and performance information phrasing
                    div(
                        p(
                            BundleManager.getBundle().getString("invoice.thankyou")
                        )
                    ).withClass("block"),

                    //Performance information
                    h3(BundleManager.getBundle().getString("invoice.performance.info") + ":"),
                    buildPerformanceInformationDiv(reservation),

                    //Ticket info phrasing
                    h3(BundleManager.getBundle().getString("invoice.tickets.intro") + ":"),
                    buildTicketInformationDiv(reservation),

                    //Pricing
                    h3(BundleManager.getBundle().getString("invoice.pricing") + ": "),
                    buildPricingInformationDiv(reservation),

                    //Paid date
                    buildPaidDateDiv(reservation),

                    //Goodbye
                    div(
                        text(BundleManager.getBundle().getString("invoice.wishes"))
                    ).withClasses("block", "offset-top", "large-font"),
                    div(
                        text(BundleManager.getBundle().getString("invoice.yourteam"))
                    ).withClasses("block", "large-font")
                )
            ).render();
        LOGGER.debug("Created html: {}", source);
        return source;
    }

    @Override
    public String buildBasicCancellationHtml(Reservation reservation) {
        String source =
            //Adding head manually to enable style sheet href
            html(
                head(
                    style(CSS_STYLE)
                ),
                body(
                    //Company information
                    buildCompanyInformationAndDateDiv(),

                    //Some polite phrasing and performance cancellation text
                    h1(BundleManager.getBundle().getString("invoice.header.cancellation")),
                    div(
                        text(
                            BundleManager.getBundle().getString("invoice.cancellation.text")
                        )
                    ).withClass("block"),

                    //Performance information
                    h3(BundleManager.getBundle().getString("invoice.performance.info") + ":"),
                    buildPerformanceInformationDiv(reservation),

                    //Pricing
                    h3(BundleManager.getBundle().getString("invoice.reimbursed") + ": "),
                    buildCancellationPricingInformationDiv(reservation),

                    //Paid date
                    buildPaidDateDiv(reservation),

                    //Goodbye
                    div(
                        text(BundleManager.getBundle().getString("invoice.thanks"))
                    ).withClasses("block", "offset-top", "large-font"),

                    div(
                        text(BundleManager.getBundle().getString("invoice.yourteam"))
                    ).withClasses("block", "large-font")
                )
            ).render();

        return source;
    }

    @Override
    public String buildDetailedCancellationHtml(Reservation reservation) {
        String source =
            //Adding head manually to enable style sheet href
            html(
                head(
                    style(CSS_STYLE)
                ),
                body(
                    //Company information
                    buildCompanyInformationAndDateDiv(),

                    //Some polite phrasing and performance information phrasing
                    h1(BundleManager.getBundle().getString("invoice.header.cancellation")),

                    //Customer information
                    h3(BundleManager.getBundle().getString("invoice.dears")),
                    buildCustomerInformationDiv(reservation),

                    //Cancellation text
                    div(
                        p(
                            BundleManager.getBundle().getString("invoice.cancellation.text")
                        )
                    ).withClass("block"),

                    //Performance information
                    h3(BundleManager.getBundle().getString("invoice.performance.info") + ":"),
                    buildPerformanceInformationDiv(reservation),

                    //Pricing
                    h3(BundleManager.getBundle().getString("invoice.reimbursed") + ": "),
                    buildCancellationPricingInformationDiv(reservation),

                    //Paid date
                    buildPaidDateDiv(reservation),

                    //Goodbye
                    div(
                        text(BundleManager.getBundle().getString("invoice.thanks"))
                    ).withClasses("block", "offset-top", "large-font"),

                    div(
                        text(BundleManager.getBundle().getString("invoice.yourteam"))
                    ).withClasses("block", "large-font")
                )
            ).render();

        return source;
    }

    /**
     * Builds an html div for the company information
     *
     * @return the built company information div
     */
    private ContainerTag buildCompanyInformationAndDateDiv() {
        return div(
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
                        td(BundleManager.getBundle().getString("invoice.address.value"))
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
        );
    }


    /**
     * Builds an html div for the performance information
     *
     * @param reservation the reservation the the performance information
     * @return the built performance information div
     */
    private ContainerTag buildPerformanceInformationDiv(Reservation reservation) {
        return div(
            table(
                tr(
                    th(BundleManager.getBundle().getString("invoice.reservationnumber")),
                    td(reservation.getReservationNumber())
                ),

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
        ).withClass("block");
    }

    /**
     * Builds an html div for the ticket information
     *
     * @param reservation the reservation the ticket information stems from
     * @return the built ticket information div
     */
    private ContainerTag buildTicketInformationDiv(Reservation reservation) {
        return div(
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
                            + " " + getIndexForSeat(reservation.getSeats(), seat)),
                        td(Integer.toString(seat.getSector().getHallNumber()) + 1),
                        //Make positions human friendly by adding 1
                        td(getSeatYRepresentation(seat, reservation)),
                        td(getSeatXRepresentation(seat, reservation))
                    )
                )
            ).withClass("regular-table")
        ).withClass("bordered-div");
    }

    /**
     * Builds an html div for the pricing
     *
     * @param reservation the reservation the pricing stems from
     * @return the built pricing div
     */
    private ContainerTag buildPricingInformationDiv(Reservation reservation) {
        return div(
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
        ).withClass("bordered-div");
    }

    /**
     * Builds an html div for the cancellation pricing
     *
     * @param reservation the reservation the cancellation pricing stems from
     * @return the built cancellation pricing div
     */
    private ContainerTag buildCancellationPricingInformationDiv(Reservation reservation) {
        if(reservation.getElusivePrice() != null) {
            return div(
                table(
                    tr(
                        th(BundleManager.getBundle().getString("invoice.total")),
                        td(PriceUtils.priceToRepresentation(reservation.getElusivePrice())
                            + " " + BundleManager.getBundle().getString("invoice.include.vat"))
                    )
                ).withClass("regular-table")
            ).withClass("bordered-div");
        } else {
            return div();
        }
    }

    /**
     * Builds an html div for the customer information
     *
     * @param reservation the reservation the customer information stems from
     * @return the built customer information div
     */
    private ContainerTag buildCustomerInformationDiv(Reservation reservation) {
        return div(
            table(
                tbody(
                    tr(
                        th(BundleManager.getBundle().getString("invoice.first.name")),
                        th(BundleManager.getBundle().getString("invoice.last.name")),
                        th(BundleManager.getBundle().getString("invoice.address"))
                    ),
                    tr(
                        td(reservation.getCustomer().getFirstName()),
                        td(reservation.getCustomer().getLastName()),
                        td(buildAddress(reservation.getCustomer()))
                    )
                )
            )
        ).withClass("block");
    }

    /**
     * Builds an html div for the paid date information
     *
     * @param reservation the reservation the paid date information stems from
     * @return the built paid date information div in html
     */
    private ContainerTag buildPaidDateDiv(Reservation reservation) {
        if(reservation.getPaidAt() != null) {
            return div(
                h4(BundleManager.getBundle().getString("invoice.performance.paidat")
                    + ":"
                    + "\n"
                    + DATETIME_FORMATTER.format(reservation.getPaidAt()))
            );
        } else {
            return div();
        }
    }

    /**
     * Determines the index of a seat in a list of seats
     *
     * @param seats the list of seats in which we are looking for a certain seat
     * @param seat  the seat that we are looking for
     * @return the index of the seat
     */
    private int getIndexForSeat(List<Seat> seats, Seat seat) {
        int i = 0;
        for (Seat s : seats) {
            i++;
            if (s.equals(seat)) {
                return i;
            }
        }
        return i;
    }

    /**
     * Builds the artist representation for a performance, especially takes out the last delimiter
     * between several artists.
     *
     * @param artists the list of artists that will be concatenated
     * @return a string representation of all artists
     */
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

    /**
     * Returns a readable representation of the x-position of a seat
     *
     * @param seat        the seat we determine the x-position for
     * @param reservation the reservation this seat belongs to
     * @return the readable x-position
     */
    private String getSeatXRepresentation(Seat seat, Reservation reservation) {
        if (reservation.getPerformance().getEvent().getEventType() == EventType.SECTOR) {
            return BundleManager.getBundle().getString("invoice.free.seating");
        } else {
            return Integer.toString(seat.getPositionX() + 1);
        }
    }

    /**
     * Returns a readable representation fo the y-position of a seat
     *
     * @param seat        the seat we determine the x-position for
     * @param reservation the reservation this seat blongs to
     * @return the readabe y-position
     */
    private String getSeatYRepresentation(Seat seat, Reservation reservation) {
        if (reservation.getPerformance().getEvent().getEventType() == EventType.SECTOR) {
            return BundleManager.getBundle().getString("invoice.free.seating");
        } else {
            return Integer.toString(seat.getPositionY() + 1);
        }
    }

    /**
     * Builds the address representation for a customer
     *
     * @param customer the customer we build a representation for
     * @return the built address representation
     */
    private String buildAddress(Customer customer) {
        String out = "";
        out += customer.getBaseAddress().getStreet() + ", ";
        out += customer.getBaseAddress().getPostalCode() + " ";
        out += customer.getBaseAddress().getCity() + ", ";
        out += customer.getBaseAddress().getCountry();
        return out;
    }
}
