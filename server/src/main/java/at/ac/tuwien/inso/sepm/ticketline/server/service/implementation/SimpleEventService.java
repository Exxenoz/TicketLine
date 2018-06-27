package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventRequestTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventResponseTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event.EventSalesResultMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.result.EventSalesResult;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SimpleEventService implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final EventRepository eventRepository;
    private final EventSalesResultMapper eventSalesResultMapper;

    public SimpleEventService(EventRepository eventRepository, EventSalesResultMapper eventSalesResultMapper) {
        this.eventRepository = eventRepository;
        this.eventSalesResultMapper = eventSalesResultMapper;
    }

    @Override
    public List<Event> findAll() {
        LOGGER.info("Get all Events");
        return eventRepository.findAll();
    }

    @Override
    public Event findByPerformanceID(Long performanceID) {
        LOGGER.info("Get Event with id={}", performanceID);
        return eventRepository.findByPerformanceId(performanceID);
    }

    @Override
    public List<EventResponseTopTenDTO> findTopTenByFilter(EventRequestTopTenDTO eventRequestTopTenDTO) {
        LOGGER.info("Get Top Ten Events using the filter {}", eventRequestTopTenDTO);
        LocalDateTime startOfTheMonth = LocalDateTime.of(eventRequestTopTenDTO.getYear(), eventRequestTopTenDTO.getMonth(), 1, 0, 0);
        LocalDateTime endOfTheMonth = LocalDateTime.of(startOfTheMonth.getYear(), eventRequestTopTenDTO.getMonth(), startOfTheMonth.toLocalDate().lengthOfMonth(), 23, 59, 59);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "cnt"));
        List<EventSalesResult> eventSalesResults = eventRepository.findByMonthAndCategory(startOfTheMonth, endOfTheMonth, eventRequestTopTenDTO.getCategoryId(), pageable);
        return eventSalesResultMapper.eventSalesResultListToEventResponseTopTenDTOList(eventSalesResults);
    }
}
