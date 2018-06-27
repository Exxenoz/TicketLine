package at.ac.tuwien.inso.sepm.ticketline.client.validator;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ReservationSearchValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;

import javafx.scene.control.*;

public class ReservationSearchValidator {

    public static final String RESERVATION_NUMBER_REGEX = "^[ABCDEFGHJKLMNPQRSTUVWXYZ0123456789]+$";
    public static final String PERFORMANCE_NAME_REGEX = "^[-' a-zA-ZöüäÜÖÄ]+$";

    public static String validateReservationNumber(TextField reservationNumberTextField) throws ReservationSearchValidationException {
        String reservationNumber = reservationNumberTextField.getText();
        if (reservationNumber.length() != 7) {
            throw new ReservationSearchValidationException(BundleManager.getExceptionBundle().getString("exception.validator.reservation.reservationnumber_length"), "exception.validator.reservation.reservationnumber_length");
        }
        if (!reservationNumber.matches(RESERVATION_NUMBER_REGEX)) {
            throw new ReservationSearchValidationException(BundleManager.getExceptionBundle().getString("exception.validator.reservation.reservationnumber_characters"), "exception.validator.reservation.reservationnumber_characters");
        }
        return reservationNumber;
    }

    public static String validatePerformanceName(TextField performanceNameField) throws ReservationSearchValidationException {
        String performanceName = performanceNameField.getText();
        if (performanceName.length() < 1) {
            throw new ReservationSearchValidationException(BundleManager.getExceptionBundle().getString("exception.validator.reservation.performancename_length"), "exception.validator.reservation.performancename_length");
        }
        if (performanceName.length() > 100) {
            throw new ReservationSearchValidationException(BundleManager.getExceptionBundle().getString("exception.validator.reservation.performancename_length"), "exception.validator.reservation.performancename_length");
        }
        if (!performanceName.matches(PERFORMANCE_NAME_REGEX)) {
            throw new ReservationSearchValidationException(BundleManager.getExceptionBundle().getString("exception.validator.reservation.performancename_characters"), "exception.validator.reservation.performancename_characters");
        }
        return performanceName;

    }
}
