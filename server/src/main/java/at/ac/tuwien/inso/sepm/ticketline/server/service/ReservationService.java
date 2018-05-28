package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Reservation;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.ReservationFilterTopTen;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.InvalidReservationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReservationService {

    /**
     * Find all reservation entries by event id.
     *
     * @param eventId the id of the event
     * @return list of all reservation entries with the passed event id
     */
    List<Reservation> findAllByEventId(Long eventId);

    /**
     * finds One not yet purchased Reservation
     *
     * @param reservationId the id of the reservation
     * @return said reservation
     */
    Reservation findOneByPaidFalseById(Long reservationId);

    /**
     * Finds all the existing Reservation owned by the User with the given full name for the given performance
     * which the customer did not yet purchase
     *
     * @param firstName       the first name of the customer
     * @param lastName        the last name of the customer
     * @param performanceName the name of the performance
     * @return the reservations belonging to the performance with the given name and owned by the customer
     */
    List<Reservation> findAllByPaidFalseByCustomerNameAndPerformanceName(String firstName, String lastName,
                                                                         String performanceName);

    /**
     * Get paid reservation count by event id.
     *
     * @param eventId the id of the event
     * @return count of paid reservation entries with the passed event id
     */
    Long getPaidReservationCountByEventId(Long eventId);

    /**
     * Get paid reservation count by event id and time frame.
     *
     * @param reservationFilterTopTen the filter for the paid reservations
     * @return count of paid reservation entries with the passed event id and time frame
     */
    Long getPaidReservationCountByFilter(ReservationFilterTopTen reservationFilterTopTen);


    Reservation createReservation(Reservation reservation) throws InvalidReservationException;

    /**
     * Invoices an existing Reservation
     *
     * @param reservation the reservation to be invoiced
     * @return the invoiced reservation
     */
    Reservation purchaseReservation(Reservation reservation);

    /**
     * Edits an existing reservation
     *
     * @param reservation existing reservation with the new data
     * @return the edited reservation
     */
    Reservation editReservation(Reservation reservation);

    Reservation createAndPayReservation(Reservation reservation) throws InvalidReservationException;

    /**
     * Finds a page of all reservations
     *
     * @param pageable the object specifing the page
     * @return a Page of Reservation
     */
    Page<Reservation> findAll(Pageable pageable);
}
