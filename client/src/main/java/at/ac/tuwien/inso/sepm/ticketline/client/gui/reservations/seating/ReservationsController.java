package at.ac.tuwien.inso.sepm.ticketline.client.gui.reservations.seating;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.reservations.seating.ReservationsSearchController;

public class ReservationsController {

    private ReservationsSearchController bookingSearchController;

    private void initialize(){}

    public void loadData(){
        bookingSearchController.loadData();
    }
}
