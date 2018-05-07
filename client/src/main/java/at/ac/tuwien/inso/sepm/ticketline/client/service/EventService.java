package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;

import java.util.List;

public interface EventService {

    /**
     * Find top 10 events by ticket sale for a given month
     * @param month month of ticket sale
     * @param category category to filter
     * @return list of top 10 events
     * @throws DataAccessException in case something went wrong
     */
    List<EventDTO> findTop10ByPaidReservationCountByMonth(int month, Integer category) throws DataAccessException;
}
