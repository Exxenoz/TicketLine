package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.seat;

import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Seat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeatMapper {

    /**
     * Converts a seat entity object to a seat DTO
     *
     * @param seat the object to be converted
     * @return the converted DTO
     */
    SeatDTO seatToSeatDTO(Seat seat);

    /**
     * Converts a seat DTO to a seat entity object
     *
     * @param seatDTO the object to be converted
     * @return the converted entity object
     */
    Seat seatDTOToSeat(SeatDTO seatDTO);
}
