package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventFilterTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event.EventFilterTop10Mapper;
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
    private final EventFilterTop10Mapper eventFilterTop10Mapper;

    public EventEndpoint(EventService eventService, EventMapper eventMapper, EventFilterTop10Mapper eventFilterTop10Mapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
        this.eventFilterTop10Mapper = eventFilterTop10Mapper;
    }

    @GetMapping
    public List<EventDTO> findAll() {
        return eventMapper.eventsToEventsDTO(eventService.findAll());
    }

    @GetMapping("findByPerformanceID/{performanceID}")
    public EventDTO findByPerformanceID(@PathVariable("performanceID") Long performanceID) {
        return eventMapper.eventToEventDTO(eventService.findByPerformanceID(performanceID));
    }

    @PostMapping("/top_ten")
    public List<EventDTO> findTop10ByPaidReservationCountByFilter(@RequestBody final EventFilterTopTenDTO eventFilterTopTenDTO) {
        var eventFilterTop10 = eventFilterTop10Mapper.eventFilterTop10DTOToEventFilterTop10(eventFilterTopTenDTO);
        return eventMapper.eventsToEventsDTO(eventService.findTop10ByPaidReservationCountByFilter(eventFilterTop10));
    }
}
