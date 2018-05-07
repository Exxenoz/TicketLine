package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.sector.SectorCategoryDTO;

import java.util.List;

public interface SectorCategoryRestClient {
    /**
     * Find all categories
     * @return list of categories ordered by base price(ascending)
     * @throws DataAccessException in case something went wrong
     */
    List<SectorCategoryDTO> findAllOrderByBasePriceModAsc() throws DataAccessException;
}
