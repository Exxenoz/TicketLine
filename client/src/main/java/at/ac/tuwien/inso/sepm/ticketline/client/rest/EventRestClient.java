package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;

import java.util.List;

public interface EventRestClient {
    List<EventDTO> findAll() throws DataAccessException;

    List<EventDTO> findByPerformance(PerformanceDTO performance) throws DataAccessException;
}
