package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationFilterTopTenDTO;

import java.util.List;

public interface ReservationRestClient {

    /**
     * Find all reservations of given event
     * @param event event of performances
     * @return list of reservations
     * @throws DataAccessException in case something went wrong
     */
    List<ReservationDTO> findAllByEvent(EventDTO event) throws DataAccessException;

    /**
     * Find number of paid reservations by given event
     * @param event event of performances
     * @return count of paid reservations
     * @throws DataAccessException in case something went wrong
     */
    Long getPaidReservationCountByEvent(EventDTO event) throws DataAccessException;

    /**
     * Find number of paid reservations by given event and month
     * @param reservationFilterTopTen filter containing month and event id
     * @return count of paid reservations
     * @throws DataAccessException in case something went wrong
     */
    Long getPaidReservationCountByFilter(ReservationFilterTopTenDTO reservationFilterTopTen) throws DataAccessException;
}
