package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventRequestTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.EventRequestTopTen;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventRequestTopTenMapper {

    EventRequestTopTen eventRequestTopTenDTOToEventRequestTopTen(EventRequestTopTenDTO eventRequestTopTenDTO);

    EventRequestTopTenDTO eventRequestTopTenToEventRequestTopTenDTO(EventRequestTopTen one);
}
