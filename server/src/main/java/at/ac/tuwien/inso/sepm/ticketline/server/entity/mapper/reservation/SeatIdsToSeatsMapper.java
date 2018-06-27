package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.reservation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Seat;
import org.mapstruct.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;


@Component
public class SeatIdsToSeatsMapper {

    @Qualifier
    @Target(METHOD)
    @Retention(SOURCE)
    public @interface SeatIdToSeat {
    }

    /**
     * Creates a seat entity object using the given seat id
     *
     * @param seatId the seat id of the seat
     * @return the created seat
     */
    @SeatIdToSeat
    public Seat seatIDtoSeat(Long seatId) {
        return Seat.SeatBuilder.aSeat().withId(seatId).build();
    }
}