package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.Hall;

import at.ac.tuwien.inso.sepm.ticketline.rest.Hall.HallDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Hall;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HallMapper {

    HallDTO hallToHallDTO(Hall hall);

    Hall hallDTOToHall(HallDTO hallDTO);
}
