package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.rest.performance.SearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;

import java.util.List;

public interface PerformanceService {

    List<Performance> findAll();

    List<Performance> findByEvent(Event event);

    List<Performance> search(SearchDTO search);

}
