package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Reservation;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.ReservationSearch;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalCancelationException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalSeatReservationException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InvalidReservationException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ReservationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReservationService {

    /**
     * Setter for the seat service
     *
     * @param seatsService the seat service
     */
    void setSeatsService(SeatsService seatsService);

    /**
     * Find all reservation entries by event id.
     *
     * @param eventId the id of the event
     * @return list of all reservation entries with the passed event id
     */
    List<Reservation> findAllByEventId(Long eventId);

    /**
     * Finds One not yet purchased Reservation
     *
     * @param  reservationId the id of the reservation
     * @return said reservation
     */
    Reservation findOneByPaidFalseAndId(Long reservationId);

    /**
     * Finds one reservation with the given reservationnumber
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
     * Create new reservation
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
     * Create a new reservation and set paid true
     *
     * @param reservation
     * @return created Reservation
     * @throws InvalidReservationException
     */
    Reservation createAndPayReservation(Reservation reservation) throws InvalidReservationException, InternalSeatReservationException;

    /**
     * Cancel existing reservation
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
     * Create new reservation number with 6 alpha numeric values
     *
     * @return reservationnumber
     */
    String generateReservationNumber();

    /**
     * Finds all reservations for the given performance id
     *
     * @param id the id of the performance with the according reservations we are looking for
     * @return the list of reservations we found
     */
    List<Reservation> findReservationsForPerformance(Long id);

    /**
     * Calculates the price of a reservation with tax
     *
     * @param reservation the reservation whose price will be checked
     * @return the price of this reservation in cents
     */
    Long calculatePrice(Reservation reservation);

    /**
     * Calculates the price of a reservation without tax
     *
     * @param reservation the reservation whose price will be checked
     * @return the price of this reservation in cents
     */
    Long calculatePreTaxPrice(Reservation reservation);

    /**
     * Calculates the reimbursed amount of a reservation
     *
     * @param reservation the reservation for which we calculate the reimbursed amount
     * @return the calculated amount that has to be reimbursed
     */
    Long calculateReimbursedAmount(Reservation reservation);

    /**
     * Returns the regular tax rate of price calculation
     *
     * @return the regular tax rate
     */
    Double getRegularTaxRate();

    /**
     * Setter for reservation repository
     *
     * @param reservationRepository
     */
    void setReservationRepository(ReservationRepository reservationRepository);

    /**
     * Setter for performance repository
     *
     * @param performanceRepository
     */
    void setPerformanceRepository(PerformanceRepository performanceRepository);
}
