package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.CreateReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationFilterTopTenDTO;

import java.util.List;

public interface ReservationService {

    /**
     * Find all reservations of given event
     * @param event event of performances
     * @return list of reservations
     * @throws DataAccessException in case something went wrong
     */
    List<ReservationDTO> findAllByEvent(EventDTO event) throws DataAccessException;

    /**
     * Find number of paid reservations by given event
     * @param event event of performances
     * @return count of paid reservations
     * @throws DataAccessException in case something went wrong
     */
    Long getPaidReservationCountByEvent(EventDTO event) throws DataAccessException;

    /**
     * Find number of paid reservations by given event and month
     * @param reservationFilterTopTen filter containing month and event id
     * @return count of paid reservations
     * @throws DataAccessException in case something went wrong
     */
    Long getPaidReservationCountByFilter(ReservationFilterTopTenDTO reservationFilterTopTen) throws DataAccessException;

    /**
     * Create a reservation with the given values within the createReservationDTO
     * @param createReservationDTO contains chosen values for the attributes of the reservation
     * @return a reservationDTO which includes the id
     * @throws DataAccessException in case something goes wrong while trying to create the reservation in the db
     */
    ReservationDTO createNewReservation(CreateReservationDTO createReservationDTO) throws DataAccessException;

    /**
     * Create a reservation which is paid immediately with the given values within the createReservationDTO
     * @param createReservationDTO contains chosen values for the attributes of the reservation
     * @returna reservationDTO which includes the id
     * @throws DataAccessException in case something goes wrong while trying to create and set the reservation to paid in the db
     */
    ReservationDTO createAndPayReservation(CreateReservationDTO createReservationDTO) throws DataAccessException;

    ReservationDTO findOneByPaidFalseById(Long reservationId) throws DataAccessException;

    List<ReservationDTO> findAllByPaidFalseByCustomerName(CustomerDTO customerDTO) throws DataAccessException;

    ReservationDTO purchaseReservation(ReservationDTO reservationDTO) throws DataAccessException;

    ReservationDTO editReservation(ReservationDTO reservationDTO) throws DataAccessException;

    PageResponseDTO<ReservationDTO> findAll(final PageRequestDTO pageRequestDTO) throws DataAccessException;


}
