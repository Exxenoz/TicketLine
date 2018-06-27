package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.sector;

import at.ac.tuwien.inso.sepm.ticketline.rest.sector.SectorDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Sector;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SectorMapper {

    /**
     * Converts a sector entity object to a sector DTO
     *
     * @param sector the object to be converted
     * @return the converted DTO
     */
    SectorDTO sectorToSectorDTO(Sector sector);

    /**
     * Converts a sector DTO to a sector entity object
     *
     * @param sectorDTO the object to be converted
     * @return the converted entity object
     */
    Sector sectorDTOToSector(SectorDTO sectorDTO);
}
