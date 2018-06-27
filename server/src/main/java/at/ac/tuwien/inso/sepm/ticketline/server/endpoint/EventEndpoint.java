package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventRequestTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventResponseTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event.EventMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event.EventSalesResultMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Get a list of all the events")
    public List<EventDTO> findAll() {
        return eventMapper.eventsToEventsDTO(eventService.findAll());
    }

    @GetMapping("findByPerformanceID/{performanceID}")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("Get the event of a given performance")
    public EventDTO findByPerformanceID(@PathVariable("performanceID") Long performanceID) {
        return eventMapper.eventToEventDTO(eventService.findByPerformanceID(performanceID));
    }

    @PostMapping("/top_ten")
    @PreAuthorize("hasRole('USER')")
    public List<EventResponseTopTenDTO> findTopTenByFilter(@RequestBody final EventRequestTopTenDTO eventRequestTopTenDTO) {
        return eventService.findTopTenByFilter(eventRequestTopTenDTO);
    }
}
