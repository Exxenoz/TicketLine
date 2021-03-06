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
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalReservationException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.endpoint.HttpBadRequestException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.endpoint.HttpConflictException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.endpoint.HttpNotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalCancelationException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalSeatReservationException;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ReservationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
                               ReservationSearchMapper reservationSearchMapper) {

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
    public ReservationDTO createNewReservation(@RequestBody CreateReservationDTO createReservationDTO) throws InternalReservationException {
        final var reservationToCreate = reservationMapper.createReservationDTOToReservation(createReservationDTO);
        try {
            final var createdReservation = reservationService.createReservation(reservationToCreate);
            return reservationMapper.reservationToReservationDTO(createdReservation);
        } catch (InternalSeatReservationException e) {
            e.printStackTrace();
            throw new HttpBadRequestException();
        }
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

    @GetMapping("/find/reservationNr/{reservationNumber}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Finds a Reservation with the given reservationnumber")
    public ReservationDTO findOneByReservationNumber(@PathVariable("reservationNumber") String reservationNr) {
        final var reservation = reservationService.findOneByReservationNumber(reservationNr);
        if (reservation == null) {
            throw new HttpNotFoundException();
        }
        return reservationMapper.reservationToReservationDTO(reservation);
    }

    @PostMapping("/find")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Finds Reservations with the given customername and performancename")
    public PageResponseDTO<ReservationDTO> findAllByCustomerNameAndPerformanceName(
        @RequestBody ReservationSearchDTO reservationSearchDTO) {
        try {
            ReservationSearchValidator.validateReservationSearchDTO(reservationSearchDTO);
            Page<Reservation> reservationPage = reservationService.findAllByCustomerNameAndPerformanceName(
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
        Reservation reservation = null;
        try {
            reservation = reservationService.editReservation(
                reservationMapper.reservationDTOToReservation(reservationDTO)
            );
        } catch (InternalReservationException e) {
            throw new HttpConflictException(e.getMessage());
        }
        return reservationMapper.reservationToReservationDTO(reservation);
    }

    @PostMapping("/createAndPay")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Create and pay new Reservation for Seats in a Performance")
    public ReservationDTO createAndPayReservation(@RequestBody CreateReservationDTO createReservationDTO) throws InternalReservationException {
        final var reservationToCreate = reservationMapper.createReservationDTOToReservation(createReservationDTO);
        try {
            final var createdReservation = reservationService.createAndPayReservation(reservationToCreate);
            return reservationMapper.reservationToReservationDTO(createdReservation);
        } catch (InternalSeatReservationException e) {
            e.printStackTrace();
            throw new HttpBadRequestException();
        }

    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Finds a page of all existing Reservations")
    public PageResponseDTO<ReservationDTO> findAll(Pageable pageable) {
        Page<Reservation> reservationPage = reservationService.findAll(pageable);
        List<ReservationDTO> reservationDTOList = reservationMapper.reservationToReservationDTO(
            reservationPage.getContent()
        );
        return new PageResponseDTO<>(reservationDTOList, reservationPage.getTotalPages());
    }

    @PutMapping("/cancel/id/{id}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Cancel created reservation")
    public ReservationDTO cancelReservation(@PathVariable("id") Long id) {
        Reservation reservation = null;
        try {
            reservation = reservationService.cancelReservation(id);
        } catch (InternalCancelationException e) {
            e.printStackTrace();
        }
        return reservationMapper.reservationToReservationDTO(reservation);
    }

    @GetMapping("/performance/{performanceId}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Find all reservations for a given performance id")
    public List<ReservationDTO> findReservationsForPerformance(@PathVariable("performanceId") long performanceId) {
        List<Reservation> reservations = reservationService.findReservationsForPerformance(performanceId);
        return reservationMapper.reservationToReservationDTO(reservations);
    }
}
