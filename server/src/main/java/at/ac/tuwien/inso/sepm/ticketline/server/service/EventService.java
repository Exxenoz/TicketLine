package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventRequestTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventResponseTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;

import java.util.List;

public interface EventService {

    List<Event> findAll();

    /**
     * Finds Event filtered by the given performance id.
     * @param performanceID the performance id
     * @return the associated event is returned
     */
    Event findByPerformanceID(Long performanceID);

    /**
     * Find top 10 event entries by filter ordered by paid reservation count (descending).
     *
     * @param eventRequestTopTenDTO the filter for the events
     * @return ordered list of the filtered top 10 entries
     */
    List<EventResponseTopTenDTO> findTopTenByFilter(EventRequestTopTenDTO eventRequestTopTenDTO);
}
