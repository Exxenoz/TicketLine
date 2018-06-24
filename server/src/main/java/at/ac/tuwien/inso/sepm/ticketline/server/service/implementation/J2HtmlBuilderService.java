package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.util.PriceUtils;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Reservation;
import at.ac.tuwien.inso.sepm.ticketline.server.service.HtmlBuilderService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ReservationService;
import at.ac.tuwien.inso.sepm.ticketline.server.util.BundleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static j2html.TagCreator.*;

@Service
public class J2HtmlBuilderService implements HtmlBuilderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final String CSS_STYLE =
        "*\n" +
            "{\n" +
            "    font-family: Arial, \"Helvetica Neue\", Helvetica, sans-serif !important;\n" +
            "}\n" +
            "\n" +
            "table, th, td {\n" +
            "    background-color: cornflowerblue;\n" +
            "}\n" +
            "\n" +
            ".company-info {\n" +
            "    display: block;\n" +
            "    margin-left: 60%;\n" +
            "    margin-right: auto;\n" +
            "    width: 100%;\n" +
            "    font-size: 12px;\n" +
            "}\n";

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
                    ),

                    //Performance information
                    h3(BundleManager.getBundle().getString("invoice.performance.info")),
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
                                td(each(reservation.getPerformance().getArtists(), artist ->
                                    text(artist.getFirstName() + ", ")
                                ))
                            )
                        )
                    )
                ),

                //Ticket info phrasing
                div(
                    h3(BundleManager.getBundle().getString("invoice.tickets.intro") + ":"),
                    //Ticket information
                    table(
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
                                + BundleManager.getBundle().getString("invoice.include.vat"))
                        )
                    )
                ),
                div(
                    text(BundleManager.getBundle().getString("invoice.performance.paidat")
                        + ":"
                        + "\n"
                        + DATETIME_FORMATTER.format(reservation.getPaidAt()))
                ),
                //Goodbye
                div(
                    text(BundleManager.getBundle().getString("invoice.wishes")),
                    text(BundleManager.getBundle().getString("invoice.yourteam"))
                )
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
}
