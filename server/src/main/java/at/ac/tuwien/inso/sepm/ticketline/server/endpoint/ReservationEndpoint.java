package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationFilterTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.ReservationFilterTopTen;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.reservation.ReservationFilterTopTenMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.reservation.ReservationMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ReservationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/reservation")
@Api(value = "reservation")
public class ReservationEndpoint {

    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;
    private final ReservationFilterTopTenMapper reservationFilterTopTenMapper;

    public ReservationEndpoint(ReservationService reservationService, ReservationMapper reservationMapper, ReservationFilterTopTenMapper reservationFilterTopTenMapper) {
        this.reservationService = reservationService;
        this.reservationMapper = reservationMapper;
        this.reservationFilterTopTenMapper = reservationFilterTopTenMapper;
    }

    @GetMapping("/event/{eventId}")
    @ApiOperation("Get list of reservation entries by event id")
    public List<ReservationDTO> findAllByEventId(@PathVariable Long eventId) {
        return reservationMapper.reservationToReservationDTO(reservationService.findAllByEventId(eventId));
    }

    @GetMapping("/event/{eventId}/count")
    @ApiOperation("Get count of paid reservation entries by event id")
    public Long getPaidReservationCountByEventId(@PathVariable Long eventId) {
        return reservationService.getPaidReservationCountByEventId(eventId);
    }

    @PostMapping("/top_ten")
    @ApiOperation("Get count of paid reservation entries by top ten filter")
    public Long getPaidReservationCountByFilter(@RequestBody final ReservationFilterTopTenDTO reservationFilterTopTenDTO) {
        ReservationFilterTopTen reservationFilterTopTen = reservationFilterTopTenMapper.reservationFilterTopTenDTOToReservationFilterTopTen(reservationFilterTopTenDTO);
        return reservationService.getPaidReservationCountByFilter(reservationFilterTopTen);
    }
}
