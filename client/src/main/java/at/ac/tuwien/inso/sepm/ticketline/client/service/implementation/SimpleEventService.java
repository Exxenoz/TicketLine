package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.EventRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventFilterTopTenDTO;
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
    public List<EventDTO> findTop10ByPaidReservationCountByFilter(EventFilterTopTenDTO eventFilterTopTen) throws DataAccessException {
        return eventRestClient.findTop10ByPaidReservationCountByFilter(eventFilterTopTen);
    }

    @Override
    public EventDTO findByPerformanceID(Long performanceId) throws DataAccessException {
        return eventRestClient.findByPerformanceID(performanceId);
    }
}
