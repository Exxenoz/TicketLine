package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.EventRequestTopTen;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.EventResponseTopTen;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;

import java.util.List;

public interface EventService {

    /**
     * Finds Event filtered by the given performance id.
     * @param performanceID the performance id
     * @return the associated event is returned
     */
    Event findByPerformanceID(Long performanceID);


    List<Event> findAll();

    /**
     * Find top 10 event entries by filter ordered by paid reservation count (descending).
     *
     * @param eventRequestTopTen the filter for the events
     * @return ordered list of the filtered top 10 entries
     */
    List<EventResponseTopTen> findTopTenByMonthAndCategory(EventRequestTopTen eventRequestTopTen);
}
