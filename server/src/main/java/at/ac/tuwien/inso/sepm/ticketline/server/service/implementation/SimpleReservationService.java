package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Reservation;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.ReservationFilterTopTen;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.InvalidReservationException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ReservationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SeatRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ReservationService;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class SimpleReservationService implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;

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
    public Reservation findOneByPaidFalseById(Long reservationId) {
        return reservationRepository.findByPaidFalseAndId(reservationId);
    }

    @Override
    public List<Reservation> findAllByPaidFalseByCustomerNameAndPerformanceName(String firstName, String lastName,
                                                                                String performanceName) {
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


        Reservation createdReservation = reservationRepository.save(reservation);
        String reservationNumber = LocalDate.now().toString() + createdReservation.getId().toString();
        createdReservation.setReservationNumber(reservationNumber);

        createdReservation.setSeats(seatsForReservation);
        createdReservation.setPerformance(currentPerformance);

        return createdReservation;
    }
}
