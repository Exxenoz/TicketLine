package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventRequestTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventResponseTopTenDTO;

import java.util.List;

public interface EventService {

    /**
     * Find top 10 events by ticket sale for a given month
     *
     * @param eventRequestTopTen filter by month and category
     * @return list of top 10 events
     * @throws DataAccessException in case something went wrong
     */
    List<EventResponseTopTenDTO> findTopTenByMonthAndCategory(EventRequestTopTenDTO eventRequestTopTen) throws DataAccessException;

    /**
     * Find the event to the given performance
     *
     * @param performanceId performance id
     * @return return the event of the given performance
     * @throws DataAccessException if something went wrong while trying to retrieve the event form the database
     */
    EventDTO findByPerformanceID(Long performanceId) throws DataAccessException;
}
