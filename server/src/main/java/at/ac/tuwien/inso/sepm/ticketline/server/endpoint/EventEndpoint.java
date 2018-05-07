package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event.EventMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/event")
@Api(value = "event")
public class EventEndpoint {

    private final EventService eventService;
    private final EventMapper eventMapper;

    public EventEndpoint(EventService eventService, EventMapper eventMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
    }

    @GetMapping
    public List<EventDTO> findAll() {
        return eventMapper.eventToEventDTO(eventService.findAll());
    }

    @GetMapping("performance")
    public List<EventDTO> findByPerformance(Performance performance) {
        return eventMapper.eventToEventDTO(eventService.findByPerformance(performance));
    }

    @GetMapping("/top_ten/{month}/{categoryId}")
    public List<EventDTO> findTop10ByPaidReservationCountByMonthByCategory(@PathVariable Integer month, @PathVariable Integer categoryId) {
        return eventMapper.eventToEventDTO(eventService.findTop10ByPaidReservationCountByMonthByCategory(month, categoryId));
    }
}
