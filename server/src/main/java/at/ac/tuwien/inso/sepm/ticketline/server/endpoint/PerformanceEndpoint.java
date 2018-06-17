package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.SearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.performance.PerformanceMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PerformanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/performance")
@Api(value = "performance")
public class PerformanceEndpoint {

    private final PerformanceService performanceService;
    private final PerformanceMapper performanceMapper;

    public PerformanceEndpoint(PerformanceService performanceService,
                               PerformanceMapper performanceMapper) {
        this.performanceService = performanceService;
        this.performanceMapper = performanceMapper;
    }

    @GetMapping
    @ApiOperation("Get a list of all performances")
    @PreAuthorize("hasRole('USER')")
    public PageResponseDTO<PerformanceDTO> findAll(Pageable pageable) {
        Page<Performance> performancePage = performanceService.findAll(pageable);
        List<PerformanceDTO> performanceDTOList = performanceMapper.performanceToPerformanceDTO(performancePage.getContent());
        return new PageResponseDTO<>(performanceDTOList, performancePage.getTotalPages());
    }

    @GetMapping("findByEventID/{eventID}")
    @ApiOperation("Get a list of all the performances of a given event")
    @PreAuthorize("hasRole('USER')")
    public PageResponseDTO<PerformanceDTO> findByEventID(@PathVariable("eventID") Long eventID, PageRequestDTO pageRequestDTO) {
        Page<Performance> performancePage = performanceService.findByEventID(eventID, pageRequestDTO.getPageable());
        List<PerformanceDTO> performanceDTOList = performanceMapper.performanceToPerformanceDTO(performancePage.getContent());
        return new PageResponseDTO<>(performanceDTOList, performancePage.getTotalPages());
    }

    @GetMapping("search")
    @ApiOperation("Get a list of all the performances that match the given criteria")
    @PreAuthorize("hasRole('USER')")
    public PageResponseDTO<PerformanceDTO> findAll(SearchDTO search, PageRequestDTO pageRequestDTO) {
        Page<Performance> performancePage = performanceService.findAll(search, pageRequestDTO.getPageable());
        List<PerformanceDTO> performanceDTOList = performanceMapper.performanceToPerformanceDTO(performancePage.getContent());
        return new PageResponseDTO<>(performanceDTOList, performancePage.getTotalPages());
    }
}
