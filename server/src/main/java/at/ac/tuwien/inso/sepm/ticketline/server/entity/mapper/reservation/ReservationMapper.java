package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.reservation;

import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.CreateReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Reservation;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.reservation.SeatIdsToSeatsMapper.SeatIdToSeat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = SeatIdsToSeatsMapper.class)
public interface ReservationMapper {

    /**
     * Converts a list of reservation entity objects to a list of reservation DTOs
     *
     * @param reservations the list to be converted
     * @return the converted list
     */
    List<ReservationDTO> reservationToReservationDTO(List<Reservation> reservations);

    /**
     * Converts a reservation entity object to a reservation DTO
     *
     * @param reservation the object to be converted
     * @return the converted DTO
     */
    ReservationDTO reservationToReservationDTO(Reservation reservation);

    /**
     * Converts a list of reservation DTOs to a list of reservation entity objects
     *
     * @param reservations the list to be converted
     * @return the converted list
     */
    List<Reservation> reservationDTOToReservation(List<ReservationDTO> reservations);

    /**
     * Converts a reservation DTO to a reservation entity object
     *
     * @param reservation the object to be converted
     * @return the converted entity object
     */
    Reservation reservationDTOToReservation(ReservationDTO reservation);

    /**
     * Converts a create reservation DTO to a reservation entity object
     *
     * @param createReservationDTO the object to be converted
     * @return the converted entity object
     */
    @Mappings({
        @Mapping(target = "performance.id", source = "performanceID"),
        @Mapping(target = "customer.id", source = "customerID"),
        @Mapping(target = "paid", source = "paid"),
        @Mapping(target = "paidAt", ignore = true),
        @Mapping(target = "id", ignore = true),
    })
    Reservation createReservationDTOToReservation (CreateReservationDTO createReservationDTO);
}