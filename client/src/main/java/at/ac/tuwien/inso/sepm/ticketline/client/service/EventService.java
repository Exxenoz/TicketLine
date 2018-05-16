package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventFilterTopTenDTO;

import java.util.List;

public interface EventService {

    /**
     * Find top 10 events by ticket sale for a given month
     * @param eventFilterTopTen filter by month and category
     * @return list of top 10 events
     * @throws DataAccessException in case something went wrong
     */
    List<EventDTO> findTop10ByPaidReservationCountByFilter(EventFilterTopTenDTO eventFilterTopTen) throws DataAccessException;

    EventDTO findByPerformanceID(Long performanceId) throws DataAccessException;
}
