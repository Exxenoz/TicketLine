package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.SectorCategory;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SectorCategoryRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.SectorCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimpleSectorCategoryService implements SectorCategoryService {

    private final SectorCategoryRepository sectorCategoryRepository;

    public SimpleSectorCategoryService(SectorCategoryRepository sectorCategoryRepository) {
        this.sectorCategoryRepository = sectorCategoryRepository;
    }

    @Override
    public List<SectorCategory> findAllOrderByBasePriceModAsc() {
        return sectorCategoryRepository.findAllOrderByBasePriceModAsc();
    }
}
