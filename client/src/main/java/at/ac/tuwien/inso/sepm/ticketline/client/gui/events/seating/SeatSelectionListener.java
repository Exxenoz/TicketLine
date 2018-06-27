package at.ac.tuwien.inso.sepm.ticketline.client.gui.events.seating;

import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;

public interface SeatSelectionListener {

    /**
     * Method to be called when a seat was selected by the user
     * @param seatDTO the seat that was selected
     */
    void onSeatSelected(SeatDTO seatDTO);

    /**
     * Method to be called when a seat was de-selected by the user
     * @param seatDTO the seat that was deselected
     */
    void onSeatDeselected(SeatDTO seatDTO);

}
