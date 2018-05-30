package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.SearchDTO;

import java.util.List;

public interface PerformanceRestClient {

    /**
     * Returns a list of all performances currently in the database.
     * @return return a list of all performances
     * @throws DataAccessException in case something went wrong while trying to retrieve the performances from the database
     */
    List<PerformanceDTO> findAllPerformances() throws DataAccessException;

    /**
     * Find all performances of given event.
     * @param eventID of event of performances
     * @return list of performances
     * @throws DataAccessException in case something went wrong while trying to retrieve the performances from the database
     */
    List<PerformanceDTO> findByEventID(Long eventID) throws DataAccessException;

    /**
     * Find a list of all performances that match the given search criteria .
     * @param searchDTO a DTO which contains all the given search criteria
     * @return a list of matching performances
     * @throws DataAccessException in case something went wrong while trying to retrieve the performances from the database
     */
    List<PerformanceDTO> findAll(SearchDTO searchDTO) throws DataAccessException;

}
