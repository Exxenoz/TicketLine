package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventRequestTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventResponseTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event.EventRequestTopTenMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event.EventMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event.EventResponseTopTenMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/event")
@Api(value = "event")
public class EventEndpoint {

    private final EventService eventService;
    private final EventMapper eventMapper;
    private final EventRequestTopTenMapper eventRequestTopTenMapper;
    private final EventResponseTopTenMapper eventResponseTopTenMapper;

    public EventEndpoint(EventService eventService, EventMapper eventMapper, EventRequestTopTenMapper eventRequestTopTenMapper, EventResponseTopTenMapper eventResponseTopTenMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
        this.eventRequestTopTenMapper = eventRequestTopTenMapper;
        this.eventResponseTopTenMapper = eventResponseTopTenMapper;
    }

    @GetMapping
    @ApiOperation("Get a list of all the events")
    public List<EventDTO> findAll() {
        return eventMapper.eventsToEventsDTO(eventService.findAll());
    }

    @GetMapping("findByPerformanceID/{performanceID}")
    @ApiOperation("Get the event of a given performance")
    public EventDTO findByPerformanceID(@PathVariable("performanceID") Long performanceID) {
        return eventMapper.eventToEventDTO(eventService.findByPerformanceID(performanceID));
    }

    @PostMapping("/top_ten")
    public List<EventResponseTopTenDTO> findTopTenByMonthAndCategory(@RequestBody final EventRequestTopTenDTO eventRequestTopTenDTO) {
        var eventRequestTopTen = eventRequestTopTenMapper.eventRequestTopTenDTOToEventRequestTopTen(eventRequestTopTenDTO);
        return eventResponseTopTenMapper.eventResponseTopTenToEventResponseTopTenDTO(eventService.findTopTenByMonthAndCategory(eventRequestTopTen));
    }
}
