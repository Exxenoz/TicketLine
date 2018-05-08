package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.reservation;

import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationFilterTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.ReservationFilterTopTen;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationFilterTopTenMapper {

    ReservationFilterTopTen reservationFilterTopTenDTOToReservationFilterTopTen(ReservationFilterTopTenDTO reservationFilterTopTenDTO);

    ReservationFilterTopTenDTO reservationFilterTopTenToReservationFilterTopTenDTO(ReservationFilterTopTen one);
}
