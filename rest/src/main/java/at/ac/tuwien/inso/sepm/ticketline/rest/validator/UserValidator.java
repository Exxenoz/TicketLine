package at.ac.tuwien.inso.sepm.ticketline.rest.validator;

import at.ac.tuwien.inso.sepm.ticketline.rest.exception.UserValidatorException;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.DuplicateFinder;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.RestBundleManager;

public abstract class UserValidator {


    //validates newly created user
    public static void validateNewUser(UserDTO userDTO) throws UserValidatorException {
        validateUsername(userDTO);
        validateEncryptedPassword(userDTO);
        validateStrikes(userDTO);
        validateRoles(userDTO);
    }

    //validates existing user
    public static void validateExistingUser(UserDTO userDTO) throws UserValidatorException {
        validateID(userDTO);
        validateNewUser(userDTO);
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
            } else  if (userDTO.getUsername().length() > 30) {
                throw new UserValidatorException("username validation failed");
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

    public static void validateRoles(UserDTO userDTO) throws UserValidatorException {
        validateDTO(userDTO);

        if (userDTO.getRoles() == null) {
            throw new UserValidatorException("User roles validation failed, because roles may not be null!");
        }

        if (! new DuplicateFinder(userDTO.getRoles()).Duplicates.isEmpty()) {
            throw new UserValidatorException("User roles validation failed, because role duplicates are not allowed!");
        }

        for (String role : userDTO.getRoles()) {
            if (role.isEmpty()) {
                throw new UserValidatorException("User roles validation failed, because empty roles are not allowed!");
            }

            if (role.length() > 64) {
                throw new UserValidatorException("User roles validation failed, because roles character count must not exceed 64!");
            }
        }
    }
}
