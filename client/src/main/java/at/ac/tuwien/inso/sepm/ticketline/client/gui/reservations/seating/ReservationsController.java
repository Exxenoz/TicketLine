package at.ac.tuwien.inso.sepm.ticketline.client.gui.reservations.seating;

import javafx.fxml.FXML;
import org.springframework.stereotype.Component;


@Component
public class ReservationsController {

    private ReservationsSearchController bookingSearchController;

    public ReservationsController(ReservationsSearchController bookingSearchController) {
        this.bookingSearchController = bookingSearchController;
    }

    @FXML
    private void initialize() {
    }

    public void loadData() {
        bookingSearchController.loadData();
    }
}
