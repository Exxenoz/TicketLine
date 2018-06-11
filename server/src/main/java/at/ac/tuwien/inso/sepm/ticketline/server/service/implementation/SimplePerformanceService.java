package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.performance.SearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PerformanceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SimplePerformanceService implements PerformanceService {

    private final PerformanceRepository performanceRepository;

    public SimplePerformanceService(PerformanceRepository performanceRepository) {
        this.performanceRepository = performanceRepository;
    }

    public Page<Performance> findAll(Pageable pageable) {
        return performanceRepository.findAll(pageable);
    }

    public Page<Performance> findAll(SearchDTO searchDTO, Pageable pageable) {
        return performanceRepository.findAll(searchDTO, pageable);
    }

    @Override
    public Page<Performance> findByEventID(Long eventID, Pageable pageable) {
        return performanceRepository.findByEventId(eventID, pageable);
    }
}
