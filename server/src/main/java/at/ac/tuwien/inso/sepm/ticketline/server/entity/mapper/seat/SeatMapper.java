package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.seat;

import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Seat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeatMapper {

    SeatDTO seatToSeatDTO(Seat seat);

    Seat seatDTOToSeat(SeatDTO seatDTO);
}
