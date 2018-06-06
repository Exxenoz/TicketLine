package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.exception.ReservationSearchValidationException;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.CreateReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationSearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.validator.ReservationSearchValidator;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Reservation;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.customer.CustomerMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.reservation.ReservationMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.reservation.ReservationSearchMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.InvalidReservationException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.endpoint.HttpBadRequestException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.endpoint.HttpNotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ReservationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/reservation")
@Api(value = "reservation")
public class ReservationEndpoint {

    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;
    private final CustomerMapper customerMapper;
    private final ReservationSearchMapper reservationSearchMapper;

    public ReservationEndpoint(ReservationService reservationService,
                               ReservationMapper reservationMapper,
                               CustomerMapper customerMapper,
                               ReservationSearchMapper reservationSearchMapper
    ) {
        this.reservationService = reservationService;
        this.reservationMapper = reservationMapper;
        this.customerMapper = customerMapper;
        this.reservationSearchMapper = reservationSearchMapper;
    }

    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Get list of reservation entries by event id")
    public List<ReservationDTO> findAllByEventId(@PathVariable Long eventId) {
        return reservationMapper.reservationToReservationDTO(reservationService.findAllByEventId(eventId));
    }

    @GetMapping("/event/{eventId}/count")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Get count of paid reservation entries by event id")
    public Long getPaidReservationCountByEventId(@PathVariable Long eventId) {
        return reservationService.getPaidReservationCountByEventId(eventId);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Create a new Reservation for Seats in a Performance")
    public ReservationDTO createNewReservation(@RequestBody CreateReservationDTO createReservationDTO) throws InvalidReservationException {
        final var reservationToCreate = reservationMapper.createReservationDTOToReservation(createReservationDTO);
        final var createdReservation = reservationService.createReservation(reservationToCreate);
        return reservationMapper.reservationToReservationDTO(createdReservation);
    }

    @GetMapping("/findNotPaid/{reservationId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Finds a Reservation which wasn't purchased yet with the given id")
    public ReservationDTO findOneByPaidFalseById(@PathVariable Long reservationId) {
        final var reservation = reservationService.findOneByPaidFalseAndId(reservationId);
        if (reservation == null) {
            throw new HttpNotFoundException();
        }
        return reservationMapper.reservationToReservationDTO(reservation);
    }

    @GetMapping("/findNotPaid/reservationNr/{reservationNumber}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Finds a Reservation which wasn't purchased yet with the given id")
    public ReservationDTO findOneByPaidFalseAndReservationNumber(@PathVariable("reservationNumber") String reservationNr) {
        final var reservation = reservationService.findOneByPaidFalseAndReservationNumber(reservationNr);
        if (reservation == null) {
            throw new HttpNotFoundException();
        }
        return reservationMapper.reservationToReservationDTO(reservation);
    }

    @PostMapping("/findNotPaid")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Finds Reservations which wasn't purchased yet with the given customername and performancename")
    public PageResponseDTO<ReservationDTO> findAllByPaidFalseByCustomerNameAndPerformanceName(
        @RequestBody ReservationSearchDTO reservationSearchDTO) {
        try {
            ReservationSearchValidator.validateReservationSearchDTO(reservationSearchDTO);
            Page<Reservation> reservationPage = reservationService.findAllByPaidFalseAndCustomerNameAndPerformanceName(
                reservationSearchMapper.reservationSearchDTOToReservationSearch(reservationSearchDTO),
                reservationSearchDTO.getPageable()
            );

            List<ReservationDTO> reservationDTOList = reservationMapper.reservationToReservationDTO(reservationPage.getContent());
            if (reservationDTOList.isEmpty()) {
                throw new HttpNotFoundException();
            } else {
                return new PageResponseDTO<>(reservationDTOList, reservationPage.getTotalPages());
            }
        } catch (ReservationSearchValidationException e) {
            throw new HttpBadRequestException();
        }
    }

    @PostMapping("/purchase")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Purchases the given Reservation")
    public ReservationDTO purchaseReservation(@RequestBody ReservationDTO reservationDTO) {
        var reservation = reservationService.purchaseReservation(
            reservationMapper.reservationDTOToReservation(reservationDTO)
        );
        return reservationMapper.reservationToReservationDTO(reservation);
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Edit an existing Reservation")
    public ReservationDTO editReservation(@RequestBody ReservationDTO reservationDTO) {
        var reservation = reservationService.editReservation(
            reservationMapper.reservationDTOToReservation(reservationDTO)
        );
        return reservationMapper.reservationToReservationDTO(reservation);
    }

    @PostMapping("/createAndPay")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Create and pay new Reservation for Seats in a Performance")
    public ReservationDTO createAndPayReservation(@RequestBody CreateReservationDTO createReservationDTO) throws InvalidReservationException {
        final var reservationToCreate = reservationMapper.createReservationDTOToReservation(createReservationDTO);
        final var createdReservation = reservationService.createAndPayReservation(reservationToCreate);
        return reservationMapper.reservationToReservationDTO(createdReservation);
    }

    @GetMapping("/{page}/{size}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Finds a page of all exisiting Reservations")
    public PageResponseDTO<ReservationDTO> findAll(@PathVariable("page") int page, @PathVariable("size") int size) {
        Page<Reservation> reservationPage = reservationService.findAll(PageRequest.of(page, size));
        List<ReservationDTO> reservationDTOList = reservationMapper.reservationToReservationDTO(
            reservationPage.getContent()
        );
        return new PageResponseDTO<>(reservationDTOList, reservationPage.getTotalPages());
    }
}
