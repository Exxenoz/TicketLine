package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;

import java.util.List;

public interface PerformanceService {

    List<PerformanceDTO> findAll() throws DataAccessException;

    /**
     * Find all performances of given event
     * @param event event of performances
     * @return list of performances
     * @throws DataAccessException in case something went wrong
     */
    List<PerformanceDTO> findByEvent(EventDTO event) throws DataAccessException;
}
