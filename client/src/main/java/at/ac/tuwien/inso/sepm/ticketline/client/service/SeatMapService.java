package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.rest.sector.SectorDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SeatMapService {

    /**
     * Finds the biggest y-value in the grid for a list of sectors
     * @param sectorDTOs the list of sectors in which we look for the highest y-value
     * @param withRows decides whether or not we should include the rows (or rather height) of a sector itself
     * @return
     */
    int findMaxSectorY(List<SectorDTO> sectorDTOs, boolean withRows);

    /**
     * Finds the biggest x-value in the grid for a list of sectors
     * @param sectorDTOs the list of sectors in which we look for the highest x-value
     * @param withSeatsPerRows decides whether or not we should include the seats per row (or rather width) of the sector itself
     * @return
     */
    int findMaxSectorX(List<SectorDTO> sectorDTOs, boolean withSeatsPerRows);

}
