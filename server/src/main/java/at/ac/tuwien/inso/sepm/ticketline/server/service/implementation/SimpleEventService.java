package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;
import org.springframework.stereotype.Service;

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
}
