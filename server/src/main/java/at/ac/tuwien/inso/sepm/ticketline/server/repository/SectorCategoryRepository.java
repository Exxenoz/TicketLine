package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.SectorCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectorCategoryRepository extends JpaRepository<SectorCategory, Long> {

    /**
     * Find all sector category entries ordered by base price modifier (ascending).
     *
     * @return ordered list of all sector category entries
     */
    List<SectorCategory> findAllByOrderByBasePriceModAsc();
}
