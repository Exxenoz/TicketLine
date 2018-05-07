package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.EventFilterTop10;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;

import java.util.List;

public interface EventService {

    List<Event> findByPerformance(Performance performance);

    List<Event> findAll();

    List<Event> findTop10ByPaidReservationCountByFilter(EventFilterTop10 eventFilterTop10);
}
