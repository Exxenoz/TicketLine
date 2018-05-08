package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.sector;

import at.ac.tuwien.inso.sepm.ticketline.rest.sector.SectorCategoryDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.SectorCategory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SectorCategoryMapper {

    List<SectorCategoryDTO> sectorCategoryToSectorCategoryDTO(List<SectorCategory> all);

    List<SectorCategory> sectorCategoryDTOToSectorCategory(List<SectorCategoryDTO> all);
}