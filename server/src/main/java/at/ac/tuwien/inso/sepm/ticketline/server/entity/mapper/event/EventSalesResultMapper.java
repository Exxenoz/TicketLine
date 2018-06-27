package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventResponseTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.result.EventSalesResult;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventSalesResultMapper {

    List<EventResponseTopTenDTO> eventSalesResultListToEventResponseTopTenDTOList(List<EventSalesResult> all);
}
