package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.reservation;


import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationSearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.ReservationSearch;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationSearchMapper {

    /**
     * Converts a reservation search DTO to a reservation search entity object
     *
     * @param reservationSearchDTO the object to be converted
     * @return the converted entity object
     */
    ReservationSearch reservationSearchDTOToReservationSearch(ReservationSearchDTO reservationSearchDTO);

    /**
     * Converts a reservation search entity object to a reservation search DTO
     *
     * @param reservationSearch the object to be converted
     * @return the converted DTO
     */
    ReservationSearchDTO reservationSerachToReservationSearchDTO(ReservationSearch reservationSearch);
}
