package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Performance p join p.event as e where p = ?1")
    List<Event> findByPerformance(Performance performance);

    @Query(value = "SELECT e" +
        " FROM event e, performance p, reservation r, seat s, sector sec" +
        " WHERE e.id = p.event_id AND p.id = r.performance_id AND r.seat_id = s.id AND s.sector_id = sec.id" +
        " AND r.is_paid = true AND (:categoryId = null OR sec.category_id = :categoryId)" +
        " AND r.paid_at >= :startOfTheMonth AND r.paid_at <= :endOfTheMonth" +
        " GROUP BY e.id" +
        " ORDER BY COUNT(r.id) DESC" +
        " LIMIT 10", nativeQuery = true)
    List<Event> findTop10ByPaidReservationCountByMonthByCategory(@Param("startOfTheMonth")Timestamp startOfTheMonth, @Param("endOfTheMonth")Timestamp endOfTheMonth, @Param("categoryId")Integer categoryId);
}
