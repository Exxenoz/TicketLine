package at.ac.tuwien.inso.sepm.ticketline.rest.validator;

import at.ac.tuwien.inso.sepm.ticketline.rest.exception.UserValidatorException;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.util.RestBundleManager;

public abstract class UserValidator {

    public static void validateUser(UserDTO userDTO) throws UserValidatorException {
        if (userDTO == null) {
            throw new UserValidatorException("User validation failed, because object reference is null!");
        } else {
            if (userDTO.getId() <= 0) {
                throw new UserValidatorException("User validation failed, because user ID is negative!");
            }
            if (userDTO.getUsername().length() < 3) {
                throw new UserValidatorException("User validation failed, because user name is too short!");
            }
            if (userDTO.getPassword().length() != 60) {
                throw new UserValidatorException("User validation failed, because user password has an invalid length!");
            }
            if ((userDTO.getStrikes() < 0) && (userDTO.getStrikes() > 5)) {
                throw new UserValidatorException("User validation failed, because user strikes are invalid!");
            }
        }
    }
}
