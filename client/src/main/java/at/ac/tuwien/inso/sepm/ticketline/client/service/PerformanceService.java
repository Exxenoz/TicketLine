package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.SearchDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PerformanceService {

    /**
     * Finds a list of all performances currently in the database.
     * @return list of all performances
     * @throws DataAccessException
     */
    PageResponseDTO<PerformanceDTO> findAll(PageRequestDTO pageRequestDTO) throws DataAccessException;

    /**
     * Find all performances of given event
     * @param eventID of event of performances
     * @return list of performances
     * @throws DataAccessException in case something went wrong
     */
    PageResponseDTO<PerformanceDTO> findByEventID(Long eventID, PageRequestDTO pageRequestDTO) throws DataAccessException;

    /**
     * Finds a list of all performances that match the given search criteria.
     * @param searchDTO a DTO that contains all the given search criteria
     * @return returns a list of the matching performances
     * @throws DataAccessException in case something went wrong while trying to retrieve the performances from the database
     */
    PageResponseDTO<PerformanceDTO> findAll(SearchDTO searchDTO, PageRequestDTO pageRequestDTO) throws DataAccessException;

}
