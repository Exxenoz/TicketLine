package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.CreateReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationFilterTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.ReservationFilterTopTen;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.customer.CustomerMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.reservation.ReservationFilterTopTenMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.reservation.ReservationMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ReservationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping(value = "/reservation")
@Api(value = "reservation")
public class ReservationEndpoint {

    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;
    private final ReservationFilterTopTenMapper reservationFilterTopTenMapper;
    private final CustomerMapper customerMapper;

    public ReservationEndpoint(ReservationService reservationService,
                               ReservationMapper reservationMapper,
                               ReservationFilterTopTenMapper reservationFilterTopTenMapper,
                               CustomerMapper customerMapper
    ) {
        this.reservationService = reservationService;
        this.reservationMapper = reservationMapper;
        this.reservationFilterTopTenMapper = reservationFilterTopTenMapper;
        this.customerMapper = customerMapper;
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
        ReservationFilterTopTen reservationFilterTopTen =
            reservationFilterTopTenMapper.reservationFilterTopTenDTOToReservationFilterTopTen(reservationFilterTopTenDTO);
        return reservationService.getPaidReservationCountByFilter(reservationFilterTopTen);
    }

    @PostMapping("/create")
    @ApiOperation("Create a new Reservation for Seats in a Performance")
    @Transactional
    public ReservationDTO createNewReservation(@RequestBody CreateReservationDTO createReservationDTO) {
        final var reservationToCreate = reservationMapper.createReservationDTOToReservation(createReservationDTO);
        final var createdReservation = reservationService.createReservation(reservationToCreate);
        return reservationMapper.reservationToReservationDTO(createdReservation);
    }

    @GetMapping("/find/{reservationId}")
    @ApiOperation("Finds a Reservation which wasn't purchased yet with the given id")
    @Transactional
    public ReservationDTO findOneByPaidFalseById(@PathVariable Long reservationId) {
        final var reservation = reservationService.findOneByPaidFalseById(reservationId);
        return reservationMapper.reservationToReservationDTO(reservation);
    }

    @GetMapping("/findNotPaid")
    @ApiOperation("Finds a Reservation which wasn't purchased yet with the given customer")
    public List<ReservationDTO> findAllByPaidFalseByCustomerName(@RequestBody CustomerDTO customerDTO) {
        var customer = customerMapper.customerDTOToCustomer(customerDTO);
        var reservations = reservationService.findAllByPaidFalseByCustomerName(customer);
        return reservationMapper.reservationToReservationDTO(reservations);
    }

    @PostMapping("/purchase")
    @ApiOperation("Purchases the given Reservation")
    @Transactional
    public ReservationDTO purchaseReservation(@RequestBody ReservationDTO reservationDTO) {
        var reservation = reservationService.purchaseReservation(reservationMapper.reservationDTOToReservation(reservationDTO));
        return reservationMapper.reservationToReservationDTO(reservation);
    }

    @PostMapping("/delete")
    @ApiOperation("Delete given Reservation")
    @Transactional
    public void deleteReservation(@RequestBody ReservationDTO reservationDTO) {
        var reservation = reservationMapper.reservationDTOToReservation(reservationDTO);
        reservationService.deleteReservation(reservation);
    }
}
