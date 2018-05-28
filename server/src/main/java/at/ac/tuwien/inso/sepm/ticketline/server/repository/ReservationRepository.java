package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
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

    /**
     * Get paid reservation count by event id and time frame.
     *
     * @param eventId the id of the event
     * @param startTime the start of the time frame
     * @param endTime the end of the time frame
     * @return count of paid reservation entries with the passed event id and time frame
     */
    @Query(value = "SELECT COUNT(r.id)" +
        " FROM performance p, reservation r" +
        " WHERE p.id = r.performance_id AND p.event_id = :eventId" +
        " AND r.is_paid = true AND r.paid_at >= :startTime AND r.paid_at <= :endTime", nativeQuery = true)
    Long getPaidReservationCountByEventIdAndTimeFrame(@Param("eventId")Long eventId, @Param("startTime")Timestamp startTime, @Param("endTime")Timestamp endTime);

    /**
     * Finds a non invoiced reservation by reservation id
     *
     * @param reservationId the id of the reservation to be found
     * @return the not yet purchased reservation
     */
    Reservation findByPaidFalseAndId(Long reservationId);

    /**
     * Finds a not yet purchased reservation by the name of the customer and performance
     *
     * @param firstName       first name of the customer
     * @param lastName        last name of the customer
     * @param performanceName name of the performance
     * @return the not yed purchased reservation
     */
    @Query(value = "SELECT r.* " +
        "FROM reservation r, customer c, performance p " +
        "WHERE c.id = r.customer_id AND p.id = r.performance_id AND r.paid = false " +
        "AND c.first_name = :firstName AND c.last_name = :lastName AND p.name  = :performanceName",
        nativeQuery = true)
    List<Reservation> findAllByPaidFalseAndCustomerNameAndPerformnceName(@Param("firstName") String firstName,
                                                                         @Param("lastName") String lastName,
                                                                         @Param("performanceName") String performanceName);
}
