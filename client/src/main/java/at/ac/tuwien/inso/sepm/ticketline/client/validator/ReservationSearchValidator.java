package at.ac.tuwien.inso.sepm.ticketline.client.validator;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ReservationSearchValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;

import javafx.scene.control.*;

public class ReservationSearchValidator {

    public static final String RESERVATION_NUMBER_REGEX = "^[ABCDEF0123456789]+$";
    public static final String PERFORMANCE_NAME_REGEX = "^[-' a-zA-ZöüäÜÖÄß]+$";
    public static final int RESERVATION_NUMBER_LENGTH = 7;
    public static final int MIN_PERFORMANCE_NAME = 1;
    public static final int MAX_PERFORMANCE_NAME = 50;

    public static String validateReservationNumber(TextField reservationNumberTextField) throws ReservationSearchValidationException {
        String reservationNumber = reservationNumberTextField.getText();
        if (reservationNumber.length() != RESERVATION_NUMBER_LENGTH) {
            throw new ReservationSearchValidationException(BundleManager.getExceptionBundle().getString("exception.validator.reservation.reservationnumber_length"), "exception.validator.reservation.reservationnumber_length");
        }
        if (!reservationNumber.matches(RESERVATION_NUMBER_REGEX)) {
            throw new ReservationSearchValidationException(BundleManager.getExceptionBundle().getString("exception.validator.reservation.reservationnumber_characters"), "exception.validator.reservation.reservationnumber_characters");
        }
        return reservationNumber;
    }

    public static String validatePerformanceName(TextField performanceNameField) throws ReservationSearchValidationException {
        String performanceName = performanceNameField.getText();
        if (performanceName.length() < MIN_PERFORMANCE_NAME) {
            throw new ReservationSearchValidationException(BundleManager.getExceptionBundle().getString("exception.validator.reservation.performancename_length"), "exception.validator.reservation.performancename_length");
        }
        if (performanceName.length() > MAX_PERFORMANCE_NAME) {
            throw new ReservationSearchValidationException(BundleManager.getExceptionBundle().getString("exception.validator.reservation.performancename_length"), "exception.validator.reservation.performancename_length");
        }
        if (!performanceName.matches(PERFORMANCE_NAME_REGEX)) {
            throw new ReservationSearchValidationException(BundleManager.getExceptionBundle().getString("exception.validator.reservation.performancename_characters"), "exception.validator.reservation.performancename_characters");
        }
        return performanceName;

    }
}
