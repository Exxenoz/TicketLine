package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.SearchDTO;

import java.util.List;

public interface PerformanceRestClient {

    List<PerformanceDTO> findAllPerformances() throws DataAccessException;

    /**
     * Find all performances of given event
     * @param event event of performances
     * @return list of performances
     * @throws DataAccessException in case something went wrong
     */
    List<PerformanceDTO> findByEvent(EventDTO event) throws DataAccessException;

    List<PerformanceDTO> search(SearchDTO search) throws DataAccessException;

}
