package at.ac.tuwien.inso.sepm.ticketline.client.validator;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.ReservationSearchValidationException;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;

import javafx.scene.control.*;

public class ReservationSearchValidator {

    public static final String RESERVATION_NUMBER_REGEX = "^[ABCDEFGHJKLMNPQRSTUVWXYZ0123456789]+$";
    public static final String PERFORMANCE_NAME_REGEX = "^[-' a-zA-ZöüöäÜÖÄ]+$";

    public static String validateReservationNumber(TextField reservationNumberTextField) throws ReservationSearchValidationException {
        String reservationNumber = reservationNumberTextField.getText();
        if (reservationNumber.length() != 7) {
            throw new ReservationSearchValidationException(BundleManager.getExceptionBundle().getString("exception.validator.reservation.reservationnumber_length"));
        }
        if (!reservationNumber.matches(RESERVATION_NUMBER_REGEX)) {
            throw new ReservationSearchValidationException(BundleManager.getExceptionBundle().getString("exception.validator.reservation.reservationnumber_characters"));
        }
        return reservationNumber;
    }

    public static String validatePerformanceName(TextField performanceNameField) {
        String performanceName = performanceNameField.getText();
        if (performanceName.length() < 2) {

        }
        if (performanceName.length() > 100) {

        }
        if (!performanceName.matches(PERFORMANCE_NAME_REGEX)) {

        }

    }
}
