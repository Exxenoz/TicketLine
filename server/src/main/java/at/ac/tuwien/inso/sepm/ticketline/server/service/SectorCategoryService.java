package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.SectorCategory;

import java.util.List;

public interface SectorCategoryService {

    /**
     * Find all sector category entries ordered by base price modifier (ascending).
     *
     * @return ordered list of all sector category entries
     */
    List<SectorCategory> findAllOrderByBasePriceModAsc();
}
