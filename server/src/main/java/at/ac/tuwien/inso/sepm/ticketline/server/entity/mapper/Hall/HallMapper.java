package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.Hall;

import at.ac.tuwien.inso.sepm.ticketline.rest.Hall.HallDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Hall;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HallMapper {

    /**
     * Converts a hall entity object to a hall DTO
     *
     * @param hall the object to be converted
     * @return the converted DTO
     */
    HallDTO hallToHallDTO(Hall hall);

    /**
     * Converts a hall DTO to a hall entity object
     *
     * @param hallDTO the object to be converted
     * @return the converted entity object
     */
    Hall hallDTOToHall(HallDTO hallDTO);
}
