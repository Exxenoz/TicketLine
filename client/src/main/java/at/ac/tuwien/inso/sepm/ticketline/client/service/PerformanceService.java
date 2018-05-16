package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.SearchDTO;

import java.util.List;

public interface PerformanceService {

    List<PerformanceDTO> findAll() throws DataAccessException;

    List<PerformanceDTO> search(SearchDTO searchDTO) throws DataAccessException;

    /**
     * Find all performances of given event
     * @param eventID of event of performances
     * @return list of performances
     * @throws DataAccessException in case something went wrong
     */
    List<PerformanceDTO> findByEventID(Long eventID) throws DataAccessException;

}
