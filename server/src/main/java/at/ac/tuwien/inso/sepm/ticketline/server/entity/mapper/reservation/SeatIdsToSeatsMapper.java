package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.reservation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SeatRepository;
import org.mapstruct.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;
import static java.util.stream.Collectors.toList;


@Component
public class SeatIdsToSeatsMapper {

    @Autowired
    private SeatRepository seatRepository;

    @Qualifier
    @Target(METHOD)
    @Retention(SOURCE)
    public @interface SeatIdToSeat {
    }


    @SeatIdToSeat
    public Seat seatIDtoSeat(Long seatId) {
         return seatRepository.findById(seatId).orElse(null);
    }

}