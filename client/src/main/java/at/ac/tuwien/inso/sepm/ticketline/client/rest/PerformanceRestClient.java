package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;

import java.util.List;

public interface PerformanceRestClient {

    List<PerformanceDTO> findAllPerformances() throws DataAccessException;

}
