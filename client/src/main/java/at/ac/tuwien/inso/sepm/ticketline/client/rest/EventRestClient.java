package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventRequestTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventResponseTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import org.w3c.dom.events.Event;

import java.util.List;

public interface EventRestClient {

    /**
     * Returns a list of all the events currently in the database.
     * @return a list of all events
     * @throws DataAccessException in case something went wrong while trying to retrieve the events from the database
     */
    List<EventDTO> findAll() throws DataAccessException;

    /**
     * Finds the associated event to the given performance id.
     * @param performanceID performance id
     * @return the associated event
     * @throws DataAccessException in case something went wrong while trying to retrieve the event from the database
     */
    EventDTO findByPerformanceID(Long performanceID) throws DataAccessException;

    /**
     * Find top 10 events by ticket sale for a given month
     *
     * @param eventRequestTopTen filter by month and categoryId
     * @return list of top 10 events
     * @throws DataAccessException in case something went wrong
     */
    List<EventResponseTopTenDTO> findTopTenByMonthAndCategory(EventRequestTopTenDTO eventRequestTopTen) throws DataAccessException;
}
