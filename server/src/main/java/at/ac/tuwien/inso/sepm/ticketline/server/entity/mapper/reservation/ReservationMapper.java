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

    List<ReservationDTO> reservationToReservationDTO(List<Reservation> reservations);

    ReservationDTO reservationToReservationDTO(Reservation reservation);

    List<Reservation> reservationDTOToReservation(List<ReservationDTO> reservations);

    Reservation reservationDTOToReservation(ReservationDTO reservation);

    @Mappings({
        @Mapping(target = "performance.id", source = "performanceID"),
        @Mapping(target = "customer.id", source = "customerID"),
        @Mapping(target = "paid", source = "paid"),
        @Mapping(target = "paidAt", ignore = true),
        @Mapping(target = "id", ignore = true),
    })
    Reservation createReservationDTOToReservation (CreateReservationDTO createReservationDTO);
}