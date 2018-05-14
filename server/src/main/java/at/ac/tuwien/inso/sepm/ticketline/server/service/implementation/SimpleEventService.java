package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.EventRequestTopTen;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.EventResponseTopTen;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
    public List<EventResponseTopTen> findTopTenByMonthAndCategory(EventRequestTopTen eventRequestTopTen) {
        LocalDateTime startOfTheMonth = LocalDateTime.of(LocalDateTime.now().getYear(), eventRequestTopTen.getMonth(), 1, 0, 0);
        LocalDateTime endOfTheMonth = LocalDateTime.of(startOfTheMonth.getYear(), eventRequestTopTen.getMonth(), startOfTheMonth.toLocalDate().lengthOfMonth(), 23, 59, 59);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "cnt"));
        return eventRepository.findTopTenByMonthAndCategory(startOfTheMonth, endOfTheMonth, eventRequestTopTen.getCategoryId(), pageable);
    }
}
