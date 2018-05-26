package at.ac.tuwien.inso.sepm.ticketline.client.gui.reservations.seating;

import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class ReservationsSearchController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final SpringFxmlLoader fxmlLoader;

    public ReservationsSearchController(SpringFxmlLoader fxmlLoader){
        this.fxmlLoader = fxmlLoader;
    }

    public void loadData(){
        //TODO:load all reservations
        //initialize table view
    }

    public void showDetails(){

    }

    public void searchForReservations(){}

}
