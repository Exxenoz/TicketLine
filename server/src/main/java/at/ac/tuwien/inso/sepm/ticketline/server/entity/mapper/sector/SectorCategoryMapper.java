package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.sector;

import at.ac.tuwien.inso.sepm.ticketline.rest.sector.SectorCategoryDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.SectorCategory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SectorCategoryMapper {

    /**
     * Converts a list of seat category entity objects to a list of seat category DTOs
     *
     * @param all the list to be converted
     * @return the converted list
     */
    List<SectorCategoryDTO> sectorCategoryToSectorCategoryDTO(List<SectorCategory> all);

    /**
     * Converts a list of seat category DTOs to a list of seat category entity objects
     *
     * @param all the list to be converted
     * @return the converted list
     */
    List<SectorCategory> sectorCategoryDTOToSectorCategory(List<SectorCategoryDTO> all);
}