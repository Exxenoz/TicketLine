package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Reservation;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.ReservationFilterTopTen;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.InvalidReservationException;

import java.util.List;

public interface ReservationService {

    /**
     * Find all reservation entries by event id.
     *
     * @param eventId the id of the event
     * @return list of all reservation entries with the passed event id
     */
    List<Reservation> findAllByEventId(Long eventId);

    Reservation findOneByPaidFalseById(Long reservationId);

    List<Reservation> findAllByPaidFalseByCustomerName(Customer customer);

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

    Reservation purchaseReservation(Reservation reservation);

    void deleteReservation(Reservation reservation);

    Reservation editReservation(Reservation reservation);
}
