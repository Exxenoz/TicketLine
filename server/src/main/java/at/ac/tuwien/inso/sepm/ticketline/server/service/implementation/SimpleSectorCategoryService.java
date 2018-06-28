package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.SectorCategory;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SectorCategoryRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.SectorCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class SimpleSectorCategoryService implements SectorCategoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final SectorCategoryRepository sectorCategoryRepository;

    public SimpleSectorCategoryService(SectorCategoryRepository sectorCategoryRepository) {
        this.sectorCategoryRepository = sectorCategoryRepository;
    }

    @Override
    public List<SectorCategory> findAllOrderByBasePriceModAsc() {
        LOGGER.info("Get all SeatCategory ordered by base price ");
        return sectorCategoryRepository.findAllByOrderByBasePriceModAsc();
    }
}
