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

    @SeatIdToSeat
    public Seat seatIDtoSeat(Long seatId) {
        return Seat.builder().id(seatId).build();
    }

}