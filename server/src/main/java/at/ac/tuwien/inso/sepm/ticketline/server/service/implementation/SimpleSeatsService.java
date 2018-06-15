package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalSeatReservationException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SeatRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.SeatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SimpleSeatsService implements SeatsService {

    @Autowired
    private SeatRepository seatRepository;

    @Override
    public List<Seat> createSeats(List<Seat> seats) {
        List<Seat> createdSeats = new ArrayList<>(seats.size());

        for(Seat s: seats) {
            createdSeats.add(seatRepository.save(s));
        }

        return createdSeats;
    }

    @Override
    public Seat findPersistedSeat(Seat seat) throws InternalSeatReservationException{
        Seat out = seatRepository.findBySectorAndPositionXAndPositionY(seat.getSector(), seat.getPositionX(), seat.getPositionY());

        if(out != null) {
            return out;
        } else {
            throw new InternalSeatReservationException("Could not find persisted seat for seat parameter: " + seat.toString());
        }
    }
}
