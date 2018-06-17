package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.service.SeatMapService;
import at.ac.tuwien.inso.sepm.ticketline.rest.sector.SectorDTO;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SimpleSeatMapService implements SeatMapService {

    @Override
    public int findMaxSectorY(List<SectorDTO> sectorDTOs, boolean withRows) {
        int maxY = 0;
        int sectorHeight = 0;

        for(SectorDTO sector: sectorDTOs) {
            int checkY = sector.getStartPositionY() + sector.getRows();

            if(checkY > maxY) {
                maxY = checkY;
                sectorHeight = sector.getRows();
            }
        }

        if(withRows) {
            maxY += sectorHeight;
        }

        return maxY;
    }

    @Override
    public int findMaxSectorX(List<SectorDTO> sectorDTOs, boolean withSeatsPerRows) {
        int maxX = 0;
        int sectorWidth = 0;

        for(SectorDTO sector: sectorDTOs) {
            int checkY = sector.getStartPositionX() + sector.getSeatsPerRow();

            if(checkY > maxX) {
                maxX = checkY;
                sectorWidth = sector.getSeatsPerRow();
            }
        }

        if(withSeatsPerRows) {
            maxX += sectorWidth;
        }

        return maxX;
    }
}
