package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating;

import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;

public interface SeatSelectionListener {

    /**
     *
     * @param seatDTO
     */
    void onSeatSelected(SeatDTO seatDTO);

    /**
     *
     * @param seatDTO
     */
    void onSeatDeselected(SeatDTO seatDTO);

}
