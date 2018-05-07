package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;

public interface UserService {

    /**
     * Enabling a user again, in order to allow him to authenticate
     * @param user the user hat is enabled again
     * @return
     */
    UserDTO enableUser(User user);

    /**
     * Disabling a user's right to authenticate
     * @param user the user that is being disabled
     */
    void disableUser(User user);

    /**
     * Increasing the strike counter for a user.
     * @param user the user that earns a strike
     * @return boolean that indicates whether or not the user is disabled
     */
    boolean increaseStrikes(User user);

    /**
     * Searching for a user by name
     * @param name the name of the user
     * @return the matching user, note that user names are unique
     */
    User findUserByName(String name);
}
