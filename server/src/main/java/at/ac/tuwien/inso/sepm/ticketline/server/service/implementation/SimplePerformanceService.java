package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.performance.SearchDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.PerformanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class SimplePerformanceService implements PerformanceService {

    private final PerformanceRepository performanceRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public SimplePerformanceService(PerformanceRepository performanceRepository) {
        this.performanceRepository = performanceRepository;
    }

    public Page<Performance> findAll(Pageable pageable) {
        LOGGER.info("Get Page {} of Performances", pageable.getPageNumber());
        return performanceRepository.findAll(pageable);
    }

    public Page<Performance> findAll(SearchDTO searchDTO, Pageable pageable) {
        LOGGER.info("Get Page {} of Performances, satisfying the filter {}", pageable.getPageNumber(), searchDTO);
        return performanceRepository.findAll(searchDTO, pageable);
    }

    @Override
    public Page<Performance> findByEventID(Long eventID, Pageable pageable) {
        LOGGER.info("Get Page {} of Performances connected to the Event with id={}", pageable.getPageNumber(), eventID);
        return performanceRepository.findByEventId(eventID, pageable);
    }
}
