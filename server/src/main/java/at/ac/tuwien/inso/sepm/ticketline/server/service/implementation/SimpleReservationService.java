package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.InvalidReservationException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Reservation;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ReservationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SeatRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.lang.invoke.MethodHandles;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class SimpleReservationService implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private PerformanceRepository repo;

    public SimpleReservationService(ReservationRepository reservationRepository, SeatRepository seatRepository) {
        this.reservationRepository = reservationRepository;
        this.seatRepository = seatRepository;
    }

    @Override
    public List<Reservation> findAllByEventId(Long eventId) {
        return reservationRepository.findAllByEventId(eventId);
    }

    @Override
    public Reservation findOneByPaidFalseAndId(Long reservationId) {
        return reservationRepository.findByPaidFalseAndId(reservationId);
    }

    @Override
    public Reservation findOneByPaidFalseAndReservationNumber(String reservationNr) {
        return reservationRepository.findByPaidFalseAndReservationNumber(reservationNr);
    }

    @Override
    public List<Reservation> findAllByPaidFalseAndCustomerNameAndPerformanceName(ReservationSearch reservationSearch) {
        String firstName = reservationSearch.getFirstName();
        String lastName = reservationSearch.getLastName();
        String performanceName = reservationSearch.getPerformanceName();
        return reservationRepository.findAllByPaidFalseAndCustomerNameAndPerformnceName(firstName, lastName, performanceName);
    }

    @Override
    public Long getPaidReservationCountByEventId(Long eventId) {
        return reservationRepository.getPaidReservationCountByEventId(eventId);
    }

    @Override
    public Long getPaidReservationCountByFilter(ReservationFilterTopTen reservationFilterTopTen) {
        LocalDateTime startOfTheMonthDateTime = LocalDateTime.of(LocalDateTime.now().getYear(), reservationFilterTopTen.getMonth(), 1, 0, 0);
        LocalDateTime endOfTheMonthDateTime = LocalDateTime.of(startOfTheMonthDateTime.getYear(), reservationFilterTopTen.getMonth(), startOfTheMonthDateTime.toLocalDate().lengthOfMonth(), 23, 59, 59);
        Timestamp startOfTheMonth = Timestamp.valueOf(startOfTheMonthDateTime);
        Timestamp endOfTheMonth = Timestamp.valueOf(endOfTheMonthDateTime);
        return reservationRepository.getPaidReservationCountByEventIdAndTimeFrame(reservationFilterTopTen.getEventId(), startOfTheMonth, endOfTheMonth);
    }

    @Override
    public Reservation purchaseReservation(Reservation reservation) {
        reservation.setPaid(true);
        reservation.setPaidAt(LocalDateTime.now());
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation createAndPayReservation(Reservation reservation) throws InvalidReservationException {
        createReservation(reservation);
        return purchaseReservation(reservation);
    }

    @Override
    public Page<Reservation> findAll(Pageable pageable) {
        return reservationRepository.findAll(pageable);
    }

    @Override
    public Reservation editReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    private void checkIfAllSeatsAreFree(List<Seat> seatsToCheck) throws InvalidReservationException {
        List<Reservation> allReservations = reservationRepository.findAll();
        for (Reservation reservation : allReservations) {
            for (Seat seat : seatsToCheck) {
                if (reservation.getSeats().contains(seat)) {
                    throw new InvalidReservationException("Seats are already reserved!");
                }
            }
        }
    }

    @Override
    public Reservation createReservation(Reservation reservation) throws InvalidReservationException {
        List<Long> seatIDs = new LinkedList<>();
        for (Seat seat: reservation.getSeats()) {
            seatIDs.add(seat.getId());
        }

        List<Seat> seatsForReservation = seatRepository.findAllById(seatIDs);
        checkIfAllSeatsAreFree(seatsForReservation);

        Performance currentPerformance = repo.findById(reservation.getPerformance().getId()).get();
        reservation.setPaid(false);

        boolean unique = false;

        while (unique == false) {
            try {
                reservation.setReservationNumber(generateReservationNumber());
                unique = true;
            } catch (ConstraintViolationException e) {
                unique = false;
            }
        }

        Reservation createdReservation = reservationRepository.save(reservation);
     /*   String reservationNumber = LocalDate.now().toString() + createdReservation.getId().toString();
        createdReservation.setReservationNumber(reservationNumber); */

        createdReservation.setSeats(seatsForReservation);
        createdReservation.setPerformance(currentPerformance);

        return createdReservation;
    }

    public String generateReservationNumber() {
        String reservationNumber = "";
        final String ALPHA_NUMERIC_STRING = "ABCDEFGHJKLMNPQRSTUVWXYZ0123456789";
        while (reservationNumber.length() <= 6) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            reservationNumber += ALPHA_NUMERIC_STRING.charAt(character);
        }
        return reservationNumber;
    }
}
