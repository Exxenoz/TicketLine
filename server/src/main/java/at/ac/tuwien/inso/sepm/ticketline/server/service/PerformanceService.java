package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.rest.performance.SearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;

import java.util.List;

public interface PerformanceService {

    List<Performance> findAll();

    List<Performance> findByEventID(Long eventID);

    List<Performance> search(SearchDTO search);

}
