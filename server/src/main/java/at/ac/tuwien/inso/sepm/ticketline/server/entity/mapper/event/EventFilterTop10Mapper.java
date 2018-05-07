package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventFilterTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.EventFilterTop10;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventFilterTop10Mapper {

    EventFilterTop10 eventFilterTop10DTOToEventFilterTop10(EventFilterTopTenDTO eventFilterTopTenDTO);

    EventFilterTopTenDTO eventFilterTopTenToEventFilterTop10DTO(EventFilterTop10 one);
}
