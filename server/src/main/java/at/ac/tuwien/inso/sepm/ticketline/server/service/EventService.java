package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.EventFilterTop10;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;

import java.util.List;

public interface EventService {

    List<Event> findByPerformance(Performance performance);

    List<Event> findAll();

    /**
     * Find top 10 event entries by filter ordered by paid reservation count (descending).
     *
     * @param eventFilterTop10 the filter for the events
     * @return ordered list of the filtered top 10 entries
     */
    List<Event> findTop10ByPaidReservationCountByFilter(EventFilterTop10 eventFilterTop10);
}
