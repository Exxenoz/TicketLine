package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")

public interface EventMapper {

    EventDTO eventToEventDTO(Event event);

    List<EventDTO> eventsToEventsDTO(List<Event> all);

    Event eventDTOToEvent(EventDTO eventDTO);

    List<Event> eventsDTOtoEvents(List<Event> event);
}
