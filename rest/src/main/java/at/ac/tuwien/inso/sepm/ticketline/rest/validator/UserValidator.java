package at.ac.tuwien.inso.sepm.ticketline.rest.validator;

import at.ac.tuwien.inso.sepm.ticketline.rest.exception.UserValidatorException;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.RestBundleManager;

public abstract class UserValidator {

    //validates existing user
    public static void validateExistingUser(UserDTO userDTO) throws UserValidatorException {
        validateDTO(userDTO);
        validateUsername(userDTO);
        validateEncryptedPassword(userDTO);
        validateStrikes(userDTO);
    }

    //validates newly created user
    public static void validateNewUser(UserDTO userDTO) throws UserValidatorException {
        validateDTO(userDTO);
        validateID(userDTO);
        validateExistingUser(userDTO);
    }

    public static void validateDTO(UserDTO userDTO) throws UserValidatorException {
        if (userDTO == null) {
            throw new UserValidatorException(
                RestBundleManager.getExceptionBundle().getString("exception.validator.user.is_null")
            );
        }
    }

    public static void validateID(UserDTO userDTO) throws UserValidatorException {
        validateDTO(userDTO);
        if (userDTO.getId() == null) {
            throw new UserValidatorException(
                RestBundleManager.getExceptionBundle().getString("exception.validator.user.id.is_null")
            );
        } else {
            if (userDTO.getId() <= 0) {
                throw new UserValidatorException(
                    RestBundleManager.getExceptionBundle().getString("exception.validator.user.id.is_negative")
                );
            }
        }
    }

    public static void validateUsername(UserDTO userDTO) throws UserValidatorException {
        validateDTO(userDTO);
        if (userDTO.getUsername() == null) {
            throw new UserValidatorException(
                RestBundleManager.getExceptionBundle().getString("exception.validator.user.username.is_null")
            );
        } else {
            if (userDTO.getUsername().length() < 3) {
                throw new UserValidatorException(
                    RestBundleManager.getExceptionBundle().getString("exception.validator.user.username.too_short")
                );
            }
        }
    }

    public static void validateEncryptedPassword(UserDTO userDTO) throws UserValidatorException {
        validateDTO(userDTO);
        if (userDTO.getPassword() == null) {
            throw new UserValidatorException(
                RestBundleManager.getExceptionBundle().getString("exception.validator.user.password.is_null")
            );
        } else {
            if (userDTO.getPassword().length() != 60) {
                throw new UserValidatorException(
                    RestBundleManager.getExceptionBundle().getString("exception.validator.user.password.invalid_length")
                );
            }
        }
    }

    public static void validateStrikes(UserDTO userDTO) throws UserValidatorException {
        validateDTO(userDTO);
        if (userDTO.getStrikes() == null) {
            throw new UserValidatorException(
                RestBundleManager.getExceptionBundle().getString("exception.validator.user.strikes.is_null")
            );
        } else {
            if ((userDTO.getStrikes() < 0) && (userDTO.getStrikes() > 5)) {
                throw new UserValidatorException(
                    RestBundleManager.getExceptionBundle().getString("exception.validator.user.strikes.invalid")
                );
            }
        }
    }
}
