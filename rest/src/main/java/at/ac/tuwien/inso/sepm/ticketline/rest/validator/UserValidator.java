package at.ac.tuwien.inso.sepm.ticketline.rest.validator;

import at.ac.tuwien.inso.sepm.ticketline.rest.exception.UserValidatorException;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserCreateRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.DuplicateFinder;

public abstract class UserValidator {

    public static final String nameRegex = "^[-' a-zA-ZöüöäÜÖÄ0-9]+$";

    //validates newly created user
    public static void validateNewUser(UserCreateRequestDTO userCreateRequestDTO) throws UserValidatorException {
        validateUsername(userCreateRequestDTO);
        validatePlainTextPassword(userCreateRequestDTO);
        validateStrikes(userCreateRequestDTO);
        validateRoles(userCreateRequestDTO);
    }

    //validates existing user
    public static void validateExistingUser(UserDTO userDTO) throws UserValidatorException {
        validateID(userDTO);
        validateUsername(userDTO);
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

    public static void validateUsername(String username) throws UserValidatorException {
        if (username == null) {
            throw new UserValidatorException(
                "User Validation failed: username is null"
            );
        } else {
            if(!username.matches(nameRegex)) {
                throw new UserValidatorException(
                    "User Validation failed: does not fit regex"
                );
            }
            if (username.length() < 3) {
                throw new UserValidatorException(
                    "User Validation failed: username too short"
                );
            } else if (username.length() > 30) {
                throw new UserValidatorException(
                    "User Validation failed: username too long"
                );
            }
        }
    }

    public static void validateUsername(UserDTO userDTO) throws UserValidatorException {
        validateDTO(userDTO);
        validateUsername(userDTO.getUsername());
    }

    public static void validatePlainTextPassword(String password) throws UserValidatorException {
        if (password == null ||
            password.length() < 3 ||
            password.length() > 30 ||
            !password.matches("^[\\x00-\\xFF]*$")) {
            throw new UserValidatorException("Plain text password validation failed, because it is invalid!");
        }
    }

    public static void validatePlainTextPassword(UserCreateRequestDTO userCreateRequestDTO) throws UserValidatorException {
        validateDTO(userCreateRequestDTO);
        validatePlainTextPassword(userCreateRequestDTO.getPassword());
    }

    public static void validatePasswordChangeKey(String passwordChangeKey) throws UserValidatorException {
        if (passwordChangeKey == null) {
            throw new UserValidatorException("Password change key validation failed, because the object reference is null!");
        }

        if (!passwordChangeKey.matches("^[1-9a-zA-Z]{8}$")) {
            throw new UserValidatorException("Password change key validation failed, because it is invalid!");
        }
    }

    public static void validateStrikes(UserDTO userDTO) throws UserValidatorException {
        validateDTO(userDTO);
        if (userDTO.getStrikes() == null) {
            throw new UserValidatorException(
                "User Validation failed: strikes faulty"
            );
        } else {
            if ((userDTO.getStrikes() < 0) || (userDTO.getStrikes() > 5+1)) {
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
