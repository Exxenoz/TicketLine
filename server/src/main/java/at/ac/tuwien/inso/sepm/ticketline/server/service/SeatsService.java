package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalSeatReservationException;

import java.util.List;

/**
 * A simple service to create seats. Validation and business logic
 * has to happen outside of this service. This service assumes that
 * every seat operation was already checked.
 */
public interface SeatsService {

    /**
     * A simple creation method without further logic.
     *
     * @param seats a list of seats that was not yet fully initialized and/or is not yet
     *              represented in our persistence layer.
     */
    List<Seat> createSeats(List<Seat> seats);

    /**
     * A simple method to look up a fully specified seat in our peristence layer
     *
     * @param seat the draft of the seat we are looking for
     * @return the fully specified seat for the matching draft.
     */
    Seat findPersistedSeat(Seat seat) throws InternalSeatReservationException;

    /**
     * Removes all persisted seats equal to the seats in the given list
     *
     * @param seats the seats to be removed
     */
    void deleteAll(List<Seat> seats);

    /**
     * deletes the parameter seat
     *
     * @param seat
     */
    void deleteSeat(Seat seat);

    /**
     * finds Seat with ID
     *
     * @param id
     * @return
     */
    Seat findbyID(Long id);
}
