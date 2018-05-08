package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventFilterTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;

import java.util.List;

public interface EventRestClient {
    List<EventDTO> findAll() throws DataAccessException;

    List<EventDTO> findByPerformance(PerformanceDTO performance) throws DataAccessException;

    /**
     * Find top 10 events by ticket sale for a given month
     * @param eventFilterTopTen filter by month and categoryId
     * @return list of top 10 events
     * @throws DataAccessException in case something went wrong
     */
    List<EventDTO> findTop10ByPaidReservationCountByFilter(EventFilterTopTenDTO eventFilterTopTen) throws DataAccessException;
}
