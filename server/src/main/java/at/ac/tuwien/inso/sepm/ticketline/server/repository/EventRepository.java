package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.result.EventSalesResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Find the associated event of a given performance id.
     * @param performanceId performance id
     * @return the associated event
     */
    @Query(value = "SELECT e FROM Performance p JOIN p.event AS e WHERE p.id = :performanceId")
    Event findByPerformanceId(@Param("performanceId")Long performanceId);

    /**
     * Find event sales entries by month and sector category
     *
     * @param startOfTheMonth the start of the month
     * @param endOfTheMonth the end of the month
     * @param pageable the page filter
     * @return list of the filtered event sales entries
     */
    @Query(value = "SELECT new at.ac.tuwien.inso.sepm.ticketline.server.entity.result.EventSalesResult(e, COUNT(e.id) AS cnt)" +
        " FROM Event e, Performance p, Reservation r, Seat s, Sector sec" +
        " WHERE e.id = p.event.id AND p.id = r.performance.id AND s MEMBER OF r.seats AND s.sector.id = sec.id" +
        " AND r.paid = true AND (:categoryId IS null OR sec.category.id = :categoryId)" +
        " AND r.paidAt >= :startOfTheMonth AND r.paidAt <= :endOfTheMonth" +
        " GROUP BY e.id")
    List<EventSalesResult> findByMonthAndCategory(@Param("startOfTheMonth")LocalDateTime startOfTheMonth, @Param("endOfTheMonth")LocalDateTime endOfTheMonth, @Param("categoryId")Long categoryId, Pageable pageable);
}
