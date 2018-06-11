package at.ac.tuwien.inso.sepm.ticketline.rest.validator;

import at.ac.tuwien.inso.sepm.ticketline.rest.exception.ReservationSearchValidationException;
import at.ac.tuwien.inso.sepm.ticketline.rest.reservation.ReservationSearchDTO;

public abstract class ReservationSearchValidator {


    private static final String NAME_REGEX = "^[-a-zA-Z ]+$";

    public static void validateReservationSearchDTO(ReservationSearchDTO reservationSearchDTO) throws ReservationSearchValidationException {
        validateDTO(reservationSearchDTO);
        validateCustomerFirstName(reservationSearchDTO);
        validateCustomerLastName(reservationSearchDTO);
        validatePerformanceName(reservationSearchDTO);
    }

    public static void validateDTO(ReservationSearchDTO reservationSearchDTO) throws ReservationSearchValidationException {
        if (reservationSearchDTO == null) {
            throw new ReservationSearchValidationException("ReservationSearchDTO object can not be null");
        }
    }

    public static void validateCustomerFirstName(ReservationSearchDTO reservationSearchDTO) throws ReservationSearchValidationException {
        if (reservationSearchDTO.getFirstName() == null) {
            throw new ReservationSearchValidationException("first name of the customer for the search can not be null");
        }
        if (reservationSearchDTO.getFirstName().length() < 2) {
            throw new ReservationSearchValidationException("first name of the customer for the search  has to be at least 2 characters long");
        }

        if (reservationSearchDTO.getFirstName().length() > 50) {
            throw new ReservationSearchValidationException("first name of the customer for the search can not be longer than 50 characters");
        }

        if (!reservationSearchDTO.getFirstName().matches(NAME_REGEX)) {
            throw new ReservationSearchValidationException("the first name of the customer can only be letters");
        }

    }

    public static void validateCustomerLastName(ReservationSearchDTO reservationSearchDTO) throws ReservationSearchValidationException {
        if (reservationSearchDTO.getLastName() == null) {
            throw new ReservationSearchValidationException("last name of the customer for the search can not be null");
        }
        if (reservationSearchDTO.getLastName().length() < 2) {
            throw new ReservationSearchValidationException("last name of the customer for the search has to be at least 2 characters long");
        }

        if (reservationSearchDTO.getLastName().length() > 50) {
            throw new ReservationSearchValidationException("last name of the customer for the search can not be longer than 50 characters");
        }

        if (!reservationSearchDTO.getLastName().matches(NAME_REGEX)) {
            throw new ReservationSearchValidationException("the last name of the customer can only be letters");
        }

    }

    public static void validatePerformanceName(ReservationSearchDTO reservationSearchDTO) throws ReservationSearchValidationException {
        if (reservationSearchDTO.getPerformanceName() == null) {
            throw new ReservationSearchValidationException("the performance name for the search can not be null");
        }
        if (reservationSearchDTO.getPerformanceName().length() < 2) {
            throw new ReservationSearchValidationException("the performance name for the search has to be at least 2 characters long");
        }

        if (reservationSearchDTO.getPerformanceName().length() > 100) {
            throw new ReservationSearchValidationException("the performance name for the search can not be longer than 50 characters");
        }

        if (!reservationSearchDTO.getPerformanceName().matches(NAME_REGEX)) {
            throw new ReservationSearchValidationException("the performance name can only be letters");
        }

    }
}
