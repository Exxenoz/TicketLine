package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.SearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.performance.PerformanceMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.specification.SearchSpecBuilder;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PerformanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public List<PerformanceDTO> findAll() {
        return performanceMapper.performanceToPerformanceDTO(performanceService.findAll());
    }

    @GetMapping("findByEventID/{eventID}")
    @ApiOperation("Get a list of all the performances of a given event")
    public List<PerformanceDTO> findByEventID(@PathVariable("eventID") Long eventID) {
        return performanceMapper.performanceToPerformanceDTO(performanceService.findByEventID(eventID));
    }

    @GetMapping("search")
    @ApiOperation("Get a list of all the performances that match the given criteria")
    public List<PerformanceDTO> findAll(@RequestBody final SearchDTO search) {
        return performanceMapper.performanceToPerformanceDTO(performanceService.findAll(search));
    }
}
