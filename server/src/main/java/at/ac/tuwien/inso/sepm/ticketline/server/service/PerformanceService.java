package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.rest.performance.SearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PerformanceService {

    /**
     * Returns a list of all performances.
     *
     * @return list of performances
     */
    Page<Performance> findAll(Pageable pageable);

    /**
     * Finds a list of performances filtered by the given event id.
     *
     * @param eventID event id
     * @return a list of the performances of the events.
     */
    Page<Performance> findByEventID(Long eventID, Pageable pageable);

    /**
     * Finds a list of performances filtered by the given filter criteria.
     *
     * @param search a DTO filled with all the findAll criteria
     * @return a list of performances that match the findAll criteria
     */
    Page<Performance> findAll(SearchDTO search, Pageable pageable);

}
