package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.performance.SearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PerformanceService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SimplePerformanceService implements PerformanceService {

    private final PerformanceRepository performanceRepository;

    public SimplePerformanceService(PerformanceRepository performanceRepository) {
        this.performanceRepository = performanceRepository;
    }

    @Override
    public List<Performance> findAll() {
        return performanceRepository.findAll();
    }

    public List<Performance> findAll(SearchDTO searchDTO) {return performanceRepository.findAll(searchDTO);}

    @Override
    public List<Performance> findByEventID(Long eventID) {
        return performanceRepository.findByEventId(eventID);
    }
}
