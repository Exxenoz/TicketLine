package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventResponseTopTenDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.result.EventSalesResult;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventSalesResultMapper {

    /**
     * Converts a list of event sales result entity objects to a list of event response top ten DTOs
     *
     * @param all the list to be converted
     * @return the converted list
     */
    List<EventResponseTopTenDTO> eventSalesResultListToEventResponseTopTenDTOList(List<EventSalesResult> all);
}
