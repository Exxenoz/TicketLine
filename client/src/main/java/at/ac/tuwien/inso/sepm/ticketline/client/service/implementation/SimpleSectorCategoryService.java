package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.SectorCategoryRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.SectorCategoryService;
import at.ac.tuwien.inso.sepm.ticketline.rest.sector.SectorCategoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class SimpleSectorCategoryService implements SectorCategoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final SectorCategoryRestClient sectorCategoryRestClient;

    public SimpleSectorCategoryService(SectorCategoryRestClient sectorCategoryRestClient) {
        this.sectorCategoryRestClient = sectorCategoryRestClient;
    }

    @Override
    public List<SectorCategoryDTO> findAllOrderByBasePriceModAsc() throws DataAccessException {
        return sectorCategoryRestClient.findAllOrderByBasePriceModAsc();
    }
}
