package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;

public interface UserService {

    /**
     * Enabling a user again, in order to allow him to authenticate
     * @param user The user that is enabled again
     * @return DTO object created from the user
     */
    UserDTO enableUser(User user);

    /**
     * Disabling a user's right to authenticate
     * @param user The user that is being disabled
     */
    void disableUser(User user);

    /**
     * Increasing the strike counter for a user.
     * @param user The user that earns a strike
     * @return Boolean that indicates whether or not the user is disabled
     */
    boolean increaseStrikes(User user);

    /**
     * Searching for a user by name
     * @param name The name of the user
     * @return The matching user, note that user names are unique
     */
    User findUserByName(String name);

    /**
     * Handles a created Spring Security User and completes the data for our defined entity
     * @param user The spring security User that will be processed
     */
    void initiateSecurityUser(org.springframework.security.core.userdetails.User user);
}
