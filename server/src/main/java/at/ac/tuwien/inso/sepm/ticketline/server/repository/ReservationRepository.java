package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * Find all reservation entries by event id.
     *
     * @param eventId the id of the event
     * @return list of all reservation entries with the passed event id
     */
    @Query(value = "SELECT r.*" +
        " FROM performance p, reservation r" +
        " WHERE p.id = r.performance_id AND p.event_id = :eventId", nativeQuery = true)
    List<Reservation> findAllByEventId(@Param("eventId")Long eventId);

    /**
     * Get paid reservation count by event id.
     *
     * @param eventId the id of the event
     * @return count of paid reservation entries with the passed event id
     */
    @Query(value = "SELECT COUNT(r.id)" +
        " FROM performance p, reservation r" +
        " WHERE p.id = r.performance_id AND p.event_id = :eventId AND r.is_paid = true", nativeQuery = true)
    Long getPaidReservationCountByEventId(@Param("eventId")Long eventId);
}
