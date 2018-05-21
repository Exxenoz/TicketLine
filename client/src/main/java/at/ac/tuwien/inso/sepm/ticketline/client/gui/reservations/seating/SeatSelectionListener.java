package at.ac.tuwien.inso.sepm.ticketline.client.gui.reservations.seating;

import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;

public interface SeatSelectionListener {

    void onSeatSelected(SeatDTO seatDTO);

    void onSeatDeselected(SeatDTO seatDTO);

}
