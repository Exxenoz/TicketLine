package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.SearchDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PerformanceRestClient {

    /**
     * Returns a list of all performances currently in the database.
     *
     * @return return a list of all performances
     * @throws DataAccessException in case something went wrong while trying to retrieve the performances from the database
     */
    PageResponseDTO<PerformanceDTO> findAllPerformances(final PageRequestDTO pageRequestDTO) throws DataAccessException;

    /**
     * Find all performances of given event.
     *
     * @param eventID of event of performances
     * @return list of performances
     * @throws DataAccessException in case something went wrong while trying to retrieve the performances from the database
     */
    PageResponseDTO<PerformanceDTO> findByEventID(Long eventID, PageRequestDTO pageRequestDTO) throws DataAccessException;

    /**
     * Find a list of all performances that match the given search criteria.
     *
     * @param searchDTO a DTO which contains all the given search criteria
     * @return a list of matching performances
     * @throws DataAccessException in case something went wrong while trying to retrieve the performances from the database
     */
    PageResponseDTO<PerformanceDTO> findAll(SearchDTO searchDTO, PageRequestDTO pageRequestDTO) throws DataAccessException;

}
