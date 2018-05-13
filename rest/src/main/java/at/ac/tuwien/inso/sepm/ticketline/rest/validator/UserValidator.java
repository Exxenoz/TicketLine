package at.ac.tuwien.inso.sepm.ticketline.rest.validator;

import at.ac.tuwien.inso.sepm.ticketline.rest.exception.UserValidatorException;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.RestBundleManager;

public abstract class UserValidator {

    public static void validateUser(UserDTO userDTO) throws UserValidatorException {
        if (userDTO == null) {
            throw new UserValidatorException(
                RestBundleManager.getExceptionBundle().getString("exception.validator.user.is_null")
            );
        } else {
            if (userDTO.getId() <= 0) {
                throw new UserValidatorException(
                    RestBundleManager.getExceptionBundle().getString("exception.validator.user.id_is_negative")
                );
            }
            if (userDTO.getUsername().length() < 3) {
                throw new UserValidatorException(
                    RestBundleManager.getExceptionBundle().getString("exception.validator.user.username_too_short")
                );
            }
            if (userDTO.getPassword().length() != 60) {
                throw new UserValidatorException(
                    RestBundleManager.getExceptionBundle().getString("exception.validator.user.password_invalid_length")
                );
            }
            if ((userDTO.getStrikes() < 0) && (userDTO.getStrikes() > 5)) {
                throw new UserValidatorException(
                    RestBundleManager.getExceptionBundle().getString("exception.validator.user.invalid_strikes")
                );
            }
        }
    }
}
