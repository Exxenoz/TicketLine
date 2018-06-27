package at.ac.tuwien.inso.sepm.ticketline.server.unittests.event;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventRequestTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventResponseTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventTypeDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.EventType;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event.EventMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event.EventSalesResultMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.result.EventSalesResult;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.implementation.SimpleEventService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ActiveProfiles("unit-test")
public class EventServiceTests {

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private EventSalesResultMapper eventSalesResultMapper;

    private EventService eventService;

    @Before
    public void setUp() {
        eventService = new SimpleEventService(eventRepository, eventSalesResultMapper);
    }

    private Event createValidEvent() {
        return new Event("Test Event", EventType.SECTOR, "Test Event Description");
    }

    private EventDTO createValidEventDTO() {
        return new EventDTO("Test Event", EventTypeDTO.SECTOR, "Test Event Description");
    }

    @Test
    public void findAllTest() {
        Event event = createValidEvent();

        when(eventRepository.findAll()).thenReturn(Arrays.asList(event));

        List<Event> eventList = eventService.findAll();
        Assert.assertTrue(eventList.size() == 1);
        Assert.assertTrue(eventList.get(0).equals(event));
    }

    @Test
    public void findByPerformanceIdTest() {
        Event event = createValidEvent();

        when(eventRepository.findByPerformanceId(1L)).thenReturn(event);

        Event eventResult = eventService.findByPerformanceID(1L);
        Assert.assertTrue(eventResult.equals(event));
    }

    @Test
    public void findTopTenByFilterTest() {
        Event event = createValidEvent();
        EventDTO eventDTO = createValidEventDTO();

        List<EventSalesResult> eventSalesResults = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            eventSalesResults.add(new EventSalesResult(event, 20 - i));
        }

        List<EventResponseTopTenDTO> eventResponseTopTenDTOS = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            eventResponseTopTenDTOS.add(new EventResponseTopTenDTO(eventDTO, 20 - i));
        }

        when(eventRepository.findByMonthAndCategory(any(), any(), any(), any())).thenReturn(eventSalesResults);
        when(eventSalesResultMapper.eventSalesResultListToEventResponseTopTenDTOList(any())).
            thenReturn(eventResponseTopTenDTOS);

        List<EventResponseTopTenDTO> result = eventService.findTopTenByFilter(new EventRequestTopTenDTO(1, 1, 1L));
        Assert.assertTrue(result.size() == 10);

        for (int i = 0; i < result.size(); i++) {
            EventResponseTopTenDTO eventResponseTopTenDTO = result.get(i);
            Assert.assertTrue(eventResponseTopTenDTO.getEvent().equals(eventDTO));
            Assert.assertTrue(eventResponseTopTenDTO.getSales() == (20 - i));
        }
    }
}
