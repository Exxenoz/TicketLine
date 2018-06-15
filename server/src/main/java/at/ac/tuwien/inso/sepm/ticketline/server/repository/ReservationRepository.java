package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Query(value = "SELECT r" +
        " FROM Performance p, Reservation r" +
        " WHERE p.id = r.performance.id AND p.event.id = :eventId")
    List<Reservation> findAllByEventId(@Param("eventId")Long eventId);

    /**
     * Get paid reservation count by event id.
     *
     * @param eventId the id of the event
     * @return count of paid reservation entries with the passed event id
     */
    @Query(value = "SELECT COUNT(r)" +
        " FROM Performance p, Reservation r" +
        " WHERE p.id = r.performance.id AND p.event.id = :eventId AND r.paid = true")
    Long getPaidReservationCountByEventId(@Param("eventId")Long eventId);

    /**
     * Finds a non invoiced reservation by reservation id
     *
     * @param reservationId the id of the reservation to be found
     * @return the not yet purchased reservation
     */
    Reservation findByPaidFalseAndId(Long reservationId);

    /**
     * Finds a reservation by the name of the customer and performance
     *
     * @param firstName       first name of the customer
     * @param lastName        last name of the customer
     * @param performanceName name of the performance
     * @return a page of the found  not yet purchased reservations
     */
    @Query(value = "SELECT r " +
        "FROM Reservation r, Customer c, Performance p " +
        "WHERE c.id = r.customer.id AND p.id = r.performance.id " +
        "AND c.firstName = :firstName AND c.lastName = :lastName AND p.name  = :performanceName")
    Page<Reservation> findAllByCustomerNameAndPerformanceName(@Param("firstName") String firstName,
                                                              @Param("lastName") String lastName,
                                                              @Param("performanceName") String performanceName,
                                                              Pageable pageable);

    /**
     * Finds reservation by the unique reservationNr
     *
     * @param reservationNr the unique number of the reservation to be found
     * @return the not yet purchased reservation
     */
    Reservation findByReservationNumber(String reservationNr);
}
