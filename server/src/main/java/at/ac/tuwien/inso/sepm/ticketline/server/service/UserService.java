package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;

import java.util.List;

public interface UserService {

    /**
     * Enabling a user again, in order to allow him to authenticate
     * @param user The user that is enabled again
     */
    void enableUser(User user);

    /**
     * Finds all user entries
     *
     * @return all user entries
     */
    List<User> findAll();

    /**
     * Disabling a user's right to authenticate
     * @param user The user that is being disabled
     */
    void disableUser(User user);

    /**
     * Increasing the strike counter for a users.
     * @param user The user that earns a strike
     * @return Boolean that indicates whether or not the users is disabled
     */
    boolean increaseStrikes(User user);

    /**
     * Searching for a user by name
     * @param name The name of the user
     * @return The matching users, note that users names are unique
     */
    User findUserByName(String name);

    /**
     * Handles a created Spring Security User and completes the data for our defined entity
     * @param user The spring security User that will be processed
     */
    void initiateSecurityUser(org.springframework.security.core.userdetails.User user);
}
