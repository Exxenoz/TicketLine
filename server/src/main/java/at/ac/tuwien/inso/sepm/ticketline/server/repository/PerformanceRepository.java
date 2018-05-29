package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long>, QueryByExampleExecutor<Performance>, JpaSpecificationExecutor<Performance> {

    /**
     * Finds a list of performances filtered by the given event id.
     * @param eventID event id
     * @return all performances of the given event
     */
    List<Performance> findByEventId(Long eventID);
}
