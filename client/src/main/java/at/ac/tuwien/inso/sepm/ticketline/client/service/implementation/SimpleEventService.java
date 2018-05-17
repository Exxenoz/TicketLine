package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.EventRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventRequestTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventResponseTopTenDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class SimpleEventService implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final EventRestClient eventRestClient;

    public SimpleEventService(EventRestClient eventRestClient) {
        this.eventRestClient = eventRestClient;
    }

    @Override
    public List<EventResponseTopTenDTO> findTopTenByMonthAndCategory(EventRequestTopTenDTO eventRequestTopTen) throws DataAccessException {
        return eventRestClient.findTopTenByMonthAndCategory(eventRequestTopTen);
    }

    @Override
    public EventDTO findByPerformanceID(Long performanceId) throws DataAccessException {
        return eventRestClient.findByPerformanceID(performanceId);
    }
}
