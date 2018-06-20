package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Reservation;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.ReservationSearch;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.InvalidReservationException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalCancelationException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalSeatReservationException;
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
     * @param  reservationId the id of the reservation
     * @return said reservation
     */
    Reservation findOneByPaidFalseAndId(Long reservationId);

    /**
     * finds one reservation with the given reservationnumber
     *
     * @param reservationNr the id of the reservation
     * @return said reservation
     */
    Reservation findOneByReservationNumber(String reservationNr);

    /**
     * Finds all the existing Reservation owned by the User with the given full name for the given performance
     *
     * @param reservationSearch the object holding the search
     * @return a page of the reservations belonging to the performance with the given name and owned by the customer
     */
    Page<Reservation> findAllByCustomerNameAndPerformanceName(ReservationSearch reservationSearch, Pageable pageable);

    /**
     * Get paid reservation count by event id.
     *
     * @param eventId the id of the event
     * @return count of paid reservation entries with the passed event id
     */
    Long getPaidReservationCountByEventId(Long eventId);

    /**
     * create new reservation
     *
     * @param reservation
     * @return created reservation
     * @throws InvalidReservationException
     */
    Reservation createReservation(Reservation reservation) throws InvalidReservationException, InternalSeatReservationException;
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
    Reservation editReservation(Reservation reservation) throws InvalidReservationException;

    /**
     * create a new reservation and set paid true
     *
     * @param reservation
     * @return created Reservation
     * @throws InvalidReservationException
     */
    Reservation createAndPayReservation(Reservation reservation) throws InvalidReservationException, InternalSeatReservationException;

    /**
     * cancel existing reservation
     *
     * @param id
     * @return canceled reservation
     */
    Reservation cancelReservation(Long id) throws InternalCancelationException;

    /**
     * Finds a page of all reservations
     *
     * @param pageable the object specifing the page
     * @return a Page of Reservation
     */
    Page<Reservation> findAll(Pageable pageable);

    /**
     * create new reservation number with 6 alpha numeric values
     *
     * @return reservationnumber
     */
    String generateReservationNumber();

    /**
     *
     * @param id the id of the performance with the according reservations we are looking for
     * @return the list of reservations we found
     */
    List<Reservation> findReservationsForPerformance(Long id);


    /**
     * Calculates the price of a reservation
     * @param reservation the reservation whose price will be checked
     * @return the price of this reservation in cents
     */
    Long calculatePrice(Reservation reservation);
}
