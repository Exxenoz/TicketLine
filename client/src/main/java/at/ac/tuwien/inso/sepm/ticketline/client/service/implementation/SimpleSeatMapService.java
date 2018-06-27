package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.service.SeatMapService;
import at.ac.tuwien.inso.sepm.ticketline.rest.sector.SectorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class SimpleSeatMapService implements SeatMapService {


    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public int findMaxSectorY(List<SectorDTO> sectorDTOs, boolean withRows) {
        LOGGER.debug("find longest Y Sector from {} Sector(s)", sectorDTOs.size());
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
        LOGGER.debug("find longest X Sector from {} Sector(s)", sectorDTOs.size());
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
