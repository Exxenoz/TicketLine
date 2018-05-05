package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;

import java.util.List;

public interface PerformanceService {

    List<PerformanceDTO> findAll() throws DataAccessException;
}
