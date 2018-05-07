package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.reservation.ReservationMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ReservationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/reservation")
@Api(value = "reservation")
public class ReservationEndpoint {

    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;

    public ReservationEndpoint(ReservationService reservationService, ReservationMapper reservationMapper) {
        this.reservationService = reservationService;
        this.reservationMapper = reservationMapper;
    }

    @GetMapping("/event/{eventId}")
    @ApiOperation("Get list of reservation entries by event id")
    public List<ReservationDTO> findAllByEventId(@PathVariable Long eventId) {
        return reservationMapper.reservationToReservationDTO(reservationService.findAllByEventId(eventId));
    }
}
