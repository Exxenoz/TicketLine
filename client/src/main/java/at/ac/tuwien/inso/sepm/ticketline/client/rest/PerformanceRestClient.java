package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.SearchDTO;

import java.util.List;

public interface PerformanceRestClient {

    List<PerformanceDTO> findAllPerformances() throws DataAccessException;

    List<PerformanceDTO> findByEvent(EventDTO event) throws DataAccessException;

    List<PerformanceDTO> search(SearchDTO search) throws DataAccessException;

}
