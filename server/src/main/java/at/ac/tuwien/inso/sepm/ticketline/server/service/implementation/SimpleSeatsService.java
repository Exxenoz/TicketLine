package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalSeatReservationException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SeatRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.SeatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Service
public class SimpleSeatsService implements SeatsService {


    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    private SeatRepository seatRepository;

    @Override
    public List<Seat> createSeats(List<Seat> seats) {
        LOGGER.info("Create {} Seat(s)", seats.size());
        List<Seat> createdSeats = new ArrayList<>(seats.size());

        for(Seat s: seats) {
            createdSeats.add(seatRepository.save(s));
        }

        return createdSeats;
    }

    @Override
    public Seat findPersistedSeat(Seat seat) throws InternalSeatReservationException{
        LOGGER.info("Get saved Seat {}", seat);
        Seat out = seatRepository.findBySectorAndPositionXAndPositionY(seat.getSector(), seat.getPositionX(), seat.getPositionY());

        if(out != null) {
            return out;
        } else {
            throw new InternalSeatReservationException("Could not find persisted seat for seat parameter: " + seat.toString());
        }
    }

    @Override
    public void deleteAll(List<Seat> seats) {
        LOGGER.info("Delete {} Seat(s)", seats.size());
        seatRepository.deleteAll(seats);
    }

    @Override
    public void deleteSeat(Seat seat) {
        LOGGER.info("Delete Seat {}", seat);
        seatRepository.delete(seat);
    }

    @Override
    public Seat findbyID(Long id) {
        LOGGER.info("find Seat with id={}", id);
        return seatRepository.findById(id).get();
    }
}
