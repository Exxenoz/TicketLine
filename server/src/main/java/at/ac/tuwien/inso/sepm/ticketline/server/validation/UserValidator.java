package at.ac.tuwien.inso.sepm.ticketline.server.validation;

import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;

public abstract class UserValidator {

    public static boolean validateUser(User user) {
        if (user == null) {
            return false;
        } else {
            if (user.getId() <= 0) {
                return false;
            }
            if (user.getUsername().length() < 3) {
                return false;
            }
            if (user.getPassword().length() != 60) {
                return false;
            }
            return user.getStrikes() >= 0 && user.getStrikes() <= 5;
        }
    }

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
