package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Sector;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalHallValidationException;

import java.util.List;

public interface HallPlanService {

    /**
     * Checks whether or not the given sectors are coherent and valid against a chosen hall
     * @param sectors the list of sectors that will be checked
     * @param hall the hall the list of sectors is being checked against
     * @throws InternalHallValidationException in case the sectors are not coherent with the hall
     */
    void checkSectorsAgainstHall(List<Sector> sectors, Hall hall) throws InternalHallValidationException;

    /**
     * Checks whether or not the given seats are coherent and valid against a chosen sectors. The id of the seat
     * will not be used during this check, since it may not be set.
     * @param seats the list of seats that will be checked
     * @param sectors the list of sectors that the list of seats is being checked against
     * @throws InternalHallValidationException
     */
    void checkSeatsAgainstSectors(List<Seat> seats, List<Sector> sectors) throws InternalHallValidationException;

}
