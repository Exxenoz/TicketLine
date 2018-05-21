package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.EventResponseTopTen;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "SELECT e.* FROM event e, performance p WHERE e.id = p.event_id AND p.id = :performanceId", nativeQuery = true)
    Event findByPerformanceId(@Param("performanceId")Long performanceId);

    /**
     * Find top 10 event entries by month and sector category ordered by paid reservation count (descending).
     *
     * @param startOfTheMonth the start of the month
     * @param endOfTheMonth the end of the month
     * @param categoryId the sector category id of the paid reservations
     * @param pageable the page filter
     * @return ordered list of the filtered top 10 entries
     */
    @Query(value = "SELECT new at.ac.tuwien.inso.sepm.ticketline.server.entity.EventResponseTopTen(e, COUNT(e.id) AS cnt)" +
        " FROM Event e, Performance p, Reservation r, Seat s, Sector sec" +
        " WHERE e.id = p.event.id AND p.id = r.performance.id AND s MEMBER OF r.seats AND s.sector.id = sec.id" +
        " AND r.paid = true AND (:categoryId IS null OR sec.category.id = :categoryId)" +
        " AND r.paidAt >= :startOfTheMonth AND r.paidAt <= :endOfTheMonth" +
        " GROUP BY e.id")
    List<EventResponseTopTen> findTopTenByMonthAndCategory(@Param("startOfTheMonth")LocalDateTime startOfTheMonth, @Param("endOfTheMonth")LocalDateTime endOfTheMonth, @Param("categoryId")Long categoryId, Pageable pageable);
}
