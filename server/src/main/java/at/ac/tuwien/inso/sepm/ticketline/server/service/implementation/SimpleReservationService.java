package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Reservation;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.ReservationFilterTopTen;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ReservationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SeatRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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
    public Reservation findOneNotPaidReservationById(Long reservationId) {
        return reservationRepository.getOne(reservationId);
    }

    @Override
    public List<Reservation> findAllNotPaidReservationsByCustomerName(Customer customer) {
        return null;
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
    public void purchaseReservation(Reservation reservation) {

    }

    @Override
    public Reservation createReservation(Reservation reservation) {
        List<Long> seatIDs = new LinkedList<>();
        for (Seat seat: reservation.getSeats()) {
            seatIDs.add(seat.getId());
        }

        List<Seat> seatsForReservation = seatRepository.findAllById(seatIDs);

        Reservation createdReservation = reservationRepository.save(reservation);
        createdReservation.setSeats(seatsForReservation);

        return createdReservation;
    }
}
