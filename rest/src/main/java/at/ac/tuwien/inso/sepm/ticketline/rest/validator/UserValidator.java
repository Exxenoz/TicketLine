package at.ac.tuwien.inso.sepm.ticketline.rest.validator;

import at.ac.tuwien.inso.sepm.ticketline.rest.exception.UserValidatorException;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.DuplicateFinder;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.RestBundleManager;

public abstract class UserValidator {


    //validates newly created user
    public static void validateNewUser(UserDTO userDTO) throws UserValidatorException {
        validateUsername(userDTO);
        validatePlainTextPassword(userDTO);
        validateStrikes(userDTO);
        validateRoles(userDTO);
    }

    //validates existing user
    public static void validateExistingUser(UserDTO userDTO) throws UserValidatorException {
        validateID(userDTO);
        validateUsername(userDTO);
        validateEncryptedPassword(userDTO); // ToDo: Remove me
        validateStrikes(userDTO);
        validateRoles(userDTO);
    }

    public static void validateDTO(UserDTO userDTO) throws UserValidatorException {
        if (userDTO == null) {
            throw new UserValidatorException(
              "User Validation failed: not found"
            );
        }
    }

    public static void validateID(UserDTO userDTO) throws UserValidatorException {
        validateDTO(userDTO);
        if (userDTO.getId() == null) {
            throw new UserValidatorException(
                "User Validation failed: id not found"
            );
        } else {
            if (userDTO.getId() <= 0) {
                throw new UserValidatorException(
                    "User Validation failed: id invalid"
                );
            }
        }
    }

    public static void validateUsername(UserDTO userDTO) throws UserValidatorException {
        validateDTO(userDTO);
        if (userDTO.getUsername() == null) {
            throw new UserValidatorException(
                "User Validation failed: username is null"
            );
        } else {
            if (userDTO.getUsername().length() < 3) {
                throw new UserValidatorException(
                    "User Validation failed: username too short"
                );
            } else  if (userDTO.getUsername().length() > 30) {
                throw new UserValidatorException(
                    "User Validation failed: username too long"
                );
            }
        }
    }

    public static void validatePlainTextPassword(UserDTO userDTO) throws UserValidatorException {
        validateDTO(userDTO);

        if (userDTO.getPassword() == null ||
            userDTO.getPassword().length() < 3 ||
            userDTO.getPassword().length() > 30 ||
            !userDTO.getPassword().matches("^[\\x00-\\xFF]*$")) {
            throw new UserValidatorException("Plain text password validation failed, because it is invalid!");
        }
    }

    public static void validateEncryptedPassword(UserDTO userDTO) throws UserValidatorException {
        validateDTO(userDTO);
        if (userDTO.getPassword() == null) {
            throw new UserValidatorException(
                "User Validation failed: password null"
            );
        } else {
            if (userDTO.getPassword().length() != 60) {
                throw new UserValidatorException(
                    "User Validation failed: password faulty"
                );
            }
        }
    }

    public static void validateStrikes(UserDTO userDTO) throws UserValidatorException {
        validateDTO(userDTO);
        if (userDTO.getStrikes() == null) {
            throw new UserValidatorException(
                "User Validation failed: strikes faulty"
            );
        } else {
            if ((userDTO.getStrikes() < 0) && (userDTO.getStrikes() > 5)) {
                throw new UserValidatorException(
                    "User Validation failed: strikes faulty"
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
