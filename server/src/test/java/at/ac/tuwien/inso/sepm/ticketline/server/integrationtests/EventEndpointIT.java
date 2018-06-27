package at.ac.tuwien.inso.sepm.ticketline.server.integrationtests;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventRequestTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventResponseTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventTypeDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.EventType;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.result.EventSalesResult;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtests.base.BaseIT;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;

public class EventEndpointIT extends BaseIT {

    private static final String EVENT_ENDPOINT = "/event";
    private static final String FIND_BY_PERFORMANCE_ID_PATH = "/findByPerformanceID/{performanceID}";
    private static final String FIND_TOP_TEN_BY_FILTER_PATH = "/top_ten";

    @MockBean
    private EventRepository eventRepository;

    private Event createValidEvent() {
        return new Event("Test Event", EventType.SECTOR, "Test Event Description");
    }

    private EventDTO createValidEventDTO() {
        return new EventDTO("Test Event", EventTypeDTO.SECTOR, "Test Event Description");
    }

    @Test
    public void findAllAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findAllAsUser() {
        Event event = createValidEvent();
        EventDTO eventDTO = createValidEventDTO();

        BDDMockito.
            given(eventRepository.findAll()).
            willReturn(Arrays.asList(event));

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        List<EventDTO> eventDTOList = response.as(new ArrayList<EventDTO>().getClass());
        Assert.assertTrue(eventDTOList.size() == 1);

        LinkedHashMap<String, String> content = (LinkedHashMap<String, String>)(Object)eventDTOList.get(0);
        Assert.assertTrue(content.get("name").equals(eventDTO.getName()));
        Assert.assertTrue(content.get("eventType").equals(eventDTO.getEventType().toString()));
        Assert.assertTrue(content.get("description").equals(eventDTO.getDescription()));
    }

    @Test
    public void findAllAsAdmin() {
        Event event = createValidEvent();
        EventDTO eventDTO = createValidEventDTO();

        BDDMockito.
            given(eventRepository.findAll()).
            willReturn(Arrays.asList(event));

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(EVENT_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        List<EventDTO> eventDTOList = response.as(new ArrayList<EventDTO>().getClass());
        Assert.assertTrue(eventDTOList.size() == 1);

        LinkedHashMap<String, String> content = (LinkedHashMap<String, String>)(Object)eventDTOList.get(0);
        Assert.assertTrue(content.get("name").equals(eventDTO.getName()));
        Assert.assertTrue(content.get("eventType").equals(eventDTO.getEventType().toString()));
        Assert.assertTrue(content.get("description").equals(eventDTO.getDescription()));
    }

    @Test
    public void findByPerformanceIdAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(EVENT_ENDPOINT + FIND_BY_PERFORMANCE_ID_PATH, 1)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findByPerformanceIdAsUser() {
        Event event = createValidEvent();
        EventDTO eventDTO = createValidEventDTO();

        BDDMockito.
            given(eventRepository.findByPerformanceId(any())).
            willReturn(event);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(EVENT_ENDPOINT + FIND_BY_PERFORMANCE_ID_PATH, 1)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        EventDTO eventDTOResult = response.as(EventDTO.class);
        Assert.assertTrue(eventDTOResult.equals(eventDTO));
    }

    @Test
    public void findByPerformanceIdAsAdmin() {
        Event event = createValidEvent();
        EventDTO eventDTO = createValidEventDTO();

        BDDMockito.
            given(eventRepository.findByPerformanceId(any())).
            willReturn(event);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(EVENT_ENDPOINT + FIND_BY_PERFORMANCE_ID_PATH, 1)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        EventDTO eventDTOResult = response.as(EventDTO.class);
        Assert.assertTrue(eventDTOResult.equals(eventDTO));
    }

    @Test
    public void findTopTenByFilterAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(new EventRequestTopTenDTO(1, 1, 1L))
            .when().post(EVENT_ENDPOINT + FIND_TOP_TEN_BY_FILTER_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findTopTenByFilterAsUser() {
        Event event = createValidEvent();
        EventDTO eventDTO = createValidEventDTO();

        List<EventSalesResult> eventSalesResults = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            eventSalesResults.add(new EventSalesResult(event, 20 - i));
        }

        BDDMockito.
            given(eventRepository.findByMonthAndCategory(any(), any(), any(), any())).
            willReturn(eventSalesResults);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(new EventRequestTopTenDTO(1, 1, 1L))
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().post(EVENT_ENDPOINT + FIND_TOP_TEN_BY_FILTER_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        List<EventResponseTopTenDTO> eventResponseTopTenDTOS = response.as(new ArrayList<EventResponseTopTenDTO>().getClass());
        Assert.assertTrue(eventResponseTopTenDTOS.size() == 10);

        for (int i = 0; i < eventResponseTopTenDTOS.size(); i++) {
            LinkedHashMap<String, LinkedHashMap<String, String>> content = (LinkedHashMap<String, LinkedHashMap<String, String>>)(Object)eventResponseTopTenDTOS.get(i);
            LinkedHashMap<String, String> eventContent = content.get("event");
            Assert.assertTrue(eventContent.get("name").equals(eventDTO.getName()));
            Assert.assertTrue(eventContent.get("eventType").equals(eventDTO.getEventType().toString()));
            Assert.assertTrue(eventContent.get("description").equals(eventDTO.getDescription()));
            LinkedHashMap<String, Integer> content2 = (LinkedHashMap<String, Integer>)(Object)eventResponseTopTenDTOS.get(i);
            Assert.assertTrue(content2.get("sales").equals(20 - i));
        }
    }

    @Test
    public void findTopTenByFilterAsAdmin() {
        Event event = createValidEvent();
        EventDTO eventDTO = createValidEventDTO();

        List<EventSalesResult> eventSalesResults = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            eventSalesResults.add(new EventSalesResult(event, 20 - i));
        }

        BDDMockito.
            given(eventRepository.findByMonthAndCategory(any(), any(), any(), any())).
            willReturn(eventSalesResults);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(new EventRequestTopTenDTO(1, 1, 1L))
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().post(EVENT_ENDPOINT + FIND_TOP_TEN_BY_FILTER_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        List<EventResponseTopTenDTO> eventResponseTopTenDTOS = response.as(new ArrayList<EventResponseTopTenDTO>().getClass());
        Assert.assertTrue(eventResponseTopTenDTOS.size() == 10);

        for (int i = 0; i < eventResponseTopTenDTOS.size(); i++) {
            LinkedHashMap<String, LinkedHashMap<String, String>> content = (LinkedHashMap<String, LinkedHashMap<String, String>>)(Object)eventResponseTopTenDTOS.get(i);
            LinkedHashMap<String, String> eventContent = content.get("event");
            Assert.assertTrue(eventContent.get("name").equals(eventDTO.getName()));
            Assert.assertTrue(eventContent.get("eventType").equals(eventDTO.getEventType().toString()));
            Assert.assertTrue(eventContent.get("description").equals(eventDTO.getDescription()));
            LinkedHashMap<String, Integer> content2 = (LinkedHashMap<String, Integer>)(Object)eventResponseTopTenDTOS.get(i);
            Assert.assertTrue(content2.get("sales").equals(20 - i));
        }
    }
}
