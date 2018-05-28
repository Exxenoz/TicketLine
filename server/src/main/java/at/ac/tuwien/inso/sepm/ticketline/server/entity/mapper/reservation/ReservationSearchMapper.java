package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.reservation;


import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationSearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.ReservationSearch;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationSearchMapper {
    ReservationSearch reservationSearchDTOToReservationSearch(ReservationSearchDTO reservationSearchDTO);

    ReservationSearchDTO reservationSerachToReservationSearchDTO(ReservationSearch reservationSearch);
}
