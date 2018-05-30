package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.SearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.performance.PerformanceMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PerformanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/performance")
@Api(value = "performance")
public class PerformanceEndpoint {

    private final PerformanceService performanceService;
    private final PerformanceMapper performanceMapper;

    public PerformanceEndpoint(PerformanceService performanceService, PerformanceMapper performanceMapper) {
        this.performanceService = performanceService;
        this.performanceMapper = performanceMapper;
    }

    @GetMapping
    @ApiOperation("Get a list of all performances")
    @PreAuthorize("hasRole('USER')")
    public List<PerformanceDTO> findAll() {
        return performanceMapper.performanceToPerformanceDTO(performanceService.findAll());
    }

    @GetMapping("findByEventID/{eventID}")
    @ApiOperation("Get a list of all the performances of a given event")
    @PreAuthorize("hasRole('USER')")
    public List<PerformanceDTO> findByEventID(@PathVariable("eventID") Long eventID) {
        return performanceMapper.performanceToPerformanceDTO(performanceService.findByEventID(eventID));
    }

    @PostMapping("search")
    @ApiOperation("Get a list of all the performances that match the given criteria")
    @PreAuthorize("hasRole('USER')")
    public List<PerformanceDTO> search(SearchDTO search) {
        return performanceMapper.performanceToPerformanceDTO(performanceService.search(search));
    public List<PerformanceDTO> findAll(@RequestBody final SearchDTO search) {
        return performanceMapper.performanceToPerformanceDTO(performanceService.findAll(search));
    }
}
