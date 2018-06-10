package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.PerformanceRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.PerformanceService;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.SearchDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class SimplePerformanceService implements PerformanceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PerformanceRestClient performanceRestClient;

    public SimplePerformanceService(PerformanceRestClient performanceRestClient) {
        this.performanceRestClient = performanceRestClient;
    }

    @Override
    public PageResponseDTO<PerformanceDTO> findAll(PageRequestDTO pageRequestDTO) throws DataAccessException {
        return performanceRestClient.findAllPerformances(pageRequestDTO);
    }

    public PageResponseDTO<PerformanceDTO> findAll(SearchDTO searchDTO, PageRequestDTO pageRequestDTO) throws DataAccessException {
        return performanceRestClient.findAll(searchDTO, pageRequestDTO);
    }

    @Override
    public PageResponseDTO<PerformanceDTO> findByEventID(Long eventID, PageRequestDTO pageRequestDTO) throws DataAccessException {
        return performanceRestClient.findByEventID(eventID, pageRequestDTO);
    }
}
