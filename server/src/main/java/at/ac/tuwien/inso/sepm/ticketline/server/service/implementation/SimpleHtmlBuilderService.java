package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Reservation;
import at.ac.tuwien.inso.sepm.ticketline.server.service.HtmlBuilderService;
import org.springframework.stereotype.Service;
import static j2html.TagCreator.*;

@Service
public class SimpleHtmlBuilderService implements HtmlBuilderService {

    @Override
    public String buildHtmlForReservation(Reservation reservation) {
        //Just for POC
        String source = body(
            h1(reservation.getReservationNumber()),
            tbody(
                each(reservation.getSeats(), seat -> p("Seat: " + seat.getPositionX() + "Row: " + seat.getPositionY())
                )
            )
        ).render();

        return source;
    }
}
