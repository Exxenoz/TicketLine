package at.ac.tuwien.inso.sepm.ticketline.client.validation;

import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;

public abstract class UserValidator {

    public static boolean validateUser(UserDTO userDTO) {
        if (userDTO == null) {
            return false;
        } else {
            if (userDTO.getId() <= 0) {
                return false;
            }
            if (userDTO.getUsername().length() < 3) {
                return false;
            }
            if (userDTO.getPassword().length() != 60) {
                return false;
            }
            return userDTO.getStrikes() >= 0 && userDTO.getStrikes() <= 5;
        }
    }
}
