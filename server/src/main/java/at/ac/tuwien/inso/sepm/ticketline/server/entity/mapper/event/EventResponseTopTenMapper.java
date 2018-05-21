package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventResponseTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.EventResponseTopTen;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventResponseTopTenMapper {

    EventResponseTopTen eventResponseTopTenDTOToEventResponseTopTen(EventResponseTopTenDTO eventResponseTopTenDTO);

    List<EventResponseTopTenDTO> eventResponseTopTenToEventResponseTopTenDTO(List<EventResponseTopTen> all);

    EventResponseTopTenDTO eventResponseTopTenToEventResponseTopTenDTO(EventResponseTopTen one);

    List<EventResponseTopTen> eventResponseTopTenDTOToEventResponseTopTen(List<EventResponseTopTenDTO> all);
}
