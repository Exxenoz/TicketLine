package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.EventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.CreateReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationSearchDTO;

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
     * Create a reservation with the given values within the createReservationDTO
     * @param createReservationDTO contains chosen values for the attributes of the reservation
     * @return a reservationDTO which includes the id
     * @throws DataAccessException in case something goes wrong while trying to create the reservation in the db
     */
    ReservationDTO createNewReservation(CreateReservationDTO createReservationDTO) throws DataAccessException;

    /**
     * Create a reservation which is paid immediately with the given values within the createReservationDTO
     * @param createReservationDTO contains chosen values for the attributes of the reservation
     * @return a reservationDTO which includes the id
     * @throws DataAccessException in case something goes wrong while trying to create and set the reservation to paid in the db
     */
    ReservationDTO createAndPayReservation(CreateReservationDTO createReservationDTO) throws DataAccessException;

    /**
     * Finds a not yet purchased reservation with the given id
     *
     * @param reservationId the id of the reservation
     * @return the found reservation
     * @throws DataAccessException in case something went during the action
     */
    ReservationDTO findOneByPaidFalseById(Long reservationId) throws DataAccessException;

    /**
     * Finds a reservation with the given reservation number
     *
     * @param reservationNr the unique number of the reservation
     * @return the found reservation
     * @throws DataAccessException in case something went during the action
     */
    ReservationDTO findOneByReservationNumber(String reservationNr) throws DataAccessException;

    /**
     * Finds a not yet purchased reservation with the full name of the customer and name of the performance
     *
     * @param reservationSearchDTO the DTO object with the search parameters
     * @return a page of the found reservations
     * @throws DataAccessException in case something went wrong during the action
     */
    PageResponseDTO<ReservationDTO> findAllByCustomerNameAndPerformanceName(ReservationSearchDTO reservationSearchDTO)
        throws DataAccessException;

    /**
     * Finds a page of all existing reservations
     *
     * @param pageRequestDTO the DTO object specifing the requested page
     * @return a page of reservations
     * @throws DataAccessException in case something went wrong
     */
    PageResponseDTO<ReservationDTO> findAll(final PageRequestDTO pageRequestDTO) throws DataAccessException;

    /**
     * Purchases a reservation.
     *
     * @param reservationDTO contains the reservation that is to be purchased
     * @return the updated reservation
     * @throws DataAccessException in case something goes wrong while trying to change the reservation to purchased
     */
    ReservationDTO purchaseReservation(ReservationDTO reservationDTO) throws DataAccessException;

    /**
     * Edits a reservation.
     *
     * @param reservationDTO contains the new seats for the reservation
     * @return the updated reservation
     * @throws DataAccessException in case something goes wrong while trying to update the seats
     */
    ReservationDTO editReservation(ReservationDTO reservationDTO) throws DataAccessException;


    /**
     * Cancels a reservation.
     * @param id the id of the reservation that will be cancelled
     * @return the reservation that has been cancelled
     * @throws DataAccessException in case something goes wrong while cancelling the reservation
     */
    ReservationDTO cancelReservation(Long id) throws DataAccessException;


    /**
     * Finds all reservations for a performance
     * @param id the id of the performance
     * @return all reservations for the according performance
     * @throws DataAccessException in case something goes wrong while finding the reservations
     */
    List<ReservationDTO> findReservationsForPerformance(Long id) throws DataAccessException;

}
