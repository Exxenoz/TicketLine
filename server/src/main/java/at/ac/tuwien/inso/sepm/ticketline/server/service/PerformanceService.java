package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.rest.performance.SearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;

import java.util.List;

public interface PerformanceService {

    /**
     * Returns a list of all performances.
     * @return list of performances
     */
    List<Performance> findAll();

    /**
     * Finds a list of performances filtered by the given event id.
     * @param eventID event id
     * @return a list of the performances of the events.
     */
    List<Performance> findByEventID(Long eventID);

    /**
     * Finds a list of performances filtered by the given filter criteria.
     * @param search a DTO filled with all the findAll criteria
     * @return a list of performances that match the findAll criteria
     */
    List<Performance> findAll(SearchDTO search);

}
