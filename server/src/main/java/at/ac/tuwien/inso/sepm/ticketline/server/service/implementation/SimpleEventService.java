package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.EventFilterTop10;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SimpleEventService implements EventService {

    private final EventRepository eventRepository;

    public SimpleEventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Event> findByPerformance(Performance performance) {
        return eventRepository.findByPerformance(performance);
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    @Override
    public List<Event> findTop10ByPaidReservationCountByFilter(EventFilterTop10 eventFilterTop10) {
        LocalDateTime startOfTheMonthDateTime = LocalDateTime.of(LocalDateTime.now().getYear(), eventFilterTop10.getMonth(), 1, 0, 0);
        LocalDateTime endOfTheMonthDateTime = LocalDateTime.of(startOfTheMonthDateTime.getYear(), eventFilterTop10.getMonth(), startOfTheMonthDateTime.toLocalDate().lengthOfMonth(), 23, 59, 59);
        Timestamp startOfTheMonth = Timestamp.valueOf(startOfTheMonthDateTime);
        Timestamp endOfTheMonth = Timestamp.valueOf(endOfTheMonthDateTime);
        return eventRepository.findTop10ByPaidReservationCountByFilter(startOfTheMonth, endOfTheMonth, eventFilterTop10.getCategoryId());
    }
}
