package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.sector;

import at.ac.tuwien.inso.sepm.ticketline.rest.sector.SectorDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Sector;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SectorMapper {

    SectorDTO sectorToSectorDTO(Sector sector);

    Sector sectorDTOToSector(SectorDTO sectorDTO);
}
