package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Reservation;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ReservationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ReservationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimpleReservationService implements ReservationService {

    private final ReservationRepository reservationRepository;

    public SimpleReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<Reservation> findAllByEventId(Long eventId) {
        return reservationRepository.findAllByEventId(eventId);
    }
}
