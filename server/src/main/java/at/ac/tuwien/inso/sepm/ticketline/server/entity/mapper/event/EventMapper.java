package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")

public interface EventMapper {

    /**
     * Converts an event entity object to an event DTO
     *
     * @param event the object to be converted
     * @return the converted DTO
     */
    EventDTO eventToEventDTO(Event event);

    /**
     * Converts a list of event entity objects to a list of event DTOs
     *
     * @param all the list to be converted
     * @return the converted list
     */
    List<EventDTO> eventsToEventsDTO(List<Event> all);

    /**
     * Converts an event DTO to an event entity object
     *
     * @param eventDTO the object to be converted
     * @return the converted entity object
     */
    Event eventDTOToEvent(EventDTO eventDTO);

    /**
     * Converts a list of event DTOs to a list of customer entity objects
     *
     * @param all the list to be converted
     * @return the converted list
     */
    List<Event> eventsDTOtoEvents(List<EventDTO> all);
}
