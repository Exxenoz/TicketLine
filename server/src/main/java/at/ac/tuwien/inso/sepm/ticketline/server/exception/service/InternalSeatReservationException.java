package at.ac.tuwien.inso.sepm.ticketline.server.exception.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Seat;

public class InternalSeatReservationException extends Exception {

    private Seat seat;

    public InternalSeatReservationException(String message) {
        super(message);
    }

    public InternalSeatReservationException(String message, Seat seat) {
        super(message);
        this.seat = seat;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }
}
