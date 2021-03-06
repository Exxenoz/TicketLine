package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.rest.exception.UserValidatorException;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserCreateRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserPasswordChangeRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserPasswordResetRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    int ALLOWED_STRIKES = 4;
    int STRIKE_RESET_VALUE = 0;
    /**
     * Enabling a user again, in order to allow him to authenticate
     *
     * @param userDTO The user that is enabled again
     * @throws InternalUserValidationException in case the user validation failed
     * @throws InternalUserNotFoundException in case the user was not found
     */
    void enableUser(UserDTO userDTO) throws InternalUserValidationException, InternalUserNotFoundException;

    /**
     * Finds all user entries
     *
     * @return all user entries
     */
    List<UserDTO> findAll();

    /**
     * Disabling a user's right to authenticate
     *
     * @param userDTO The user that is being disabled
     * @throws InternalUserValidationException in case the user validation failed
     * @throws InternalUserNotFoundException in case the user was not found
     */
    void disableUser(UserDTO userDTO) throws InternalUserValidationException, InternalUserNotFoundException;

    /**
     * Disabling a user's right to authenticate, if the user to disable is not the authenticated user.
     *
     * @param userDTO The user that is being disabled
     * @throws InternalUserValidationException in case the user validation failed
     * @throws InternalUserTriedToDisableHimselfException in case the user wanted to disable himself
     * @throws InternalUserNotFoundException in case the user was not found
     */
    void disableUserButNotSelf(UserDTO userDTO) throws InternalUserValidationException, InternalUserTriedToDisableHimselfException, InternalUserNotFoundException;

    /**
     * Increases the strike counter for the specified user and disables him if the strike counter is too high.
     *
     * @param userDTO The user that earns a strike
     * @throws InternalUserNotFoundException in case the user was not found
     */
    void increaseStrikesAndDisableUserIfStrikesAreTooHigh(UserDTO userDTO) throws InternalUserNotFoundException;

    /**
     * Resets the strikes of a given user.
     * @param userDTO the DTO of the user, that whose strikes will be reset
     * @throws InternalUserValidationException in case the user validation failed
     * @throws InternalUserNotFoundException in the corresponding user for the DTO was not found
     */
    void resetStrikes(UserDTO userDTO) throws InternalUserValidationException, InternalUserNotFoundException;

    /**
     * Searching for a user by name
     *
     * @param name The name of the user
     * @throws InternalUserNotFoundException in case the user was not found
     * @return The matching users, note that users names are unique
     */
    UserDTO findUserByName(String name) throws InternalUserNotFoundException;

    /**
     * Handles a created Spring Security User and completes the data for our defined entity
     *
     * @param user The spring security User that will be processed
     */
    void initiateSecurityUser(org.springframework.security.core.userdetails.User user);

    /**
     * Gets a Page of all existing Users
     *
     * @param pageable the object specifing the page data
     * @return a page of users
     */
    PageResponseDTO<UserDTO> findAll(Pageable pageable);

    /**
     * Creates a new user
     *
     * @param userCreateRequestDTO information about the user to create
     * @return created user
     * @throws InternalUserValidationException in case user validation failed
     * @throws InternalUsernameConflictException in case the username is already taken
     * @return the saved user
     */
    UserDTO save(UserCreateRequestDTO userCreateRequestDTO) throws InternalUserValidationException, InternalUsernameConflictException;

    /**
     * Resets the password of a user
     *
     * @param userPasswordResetRequestDTO the data needed to reset the password
     * @throws InternalUserValidationException in case user validation failed
     * @throws InternalUserNotFoundException in case the user was not found
     * @throws InternalBadRequestException in case the password change key was invalid
     * @return the updated user
     */
    UserDTO resetPassword(UserPasswordResetRequestDTO userPasswordResetRequestDTO) throws InternalUserValidationException, InternalUserNotFoundException, InternalBadRequestException;

    /**
     * Checks if the password change key for the specified user is set
     *
     * @param userDTO user to check
     * @throws InternalUserNotFoundException in case the user was not found
     * @return true, if the password change key is set, otherwise false
     */
    boolean isPasswordChangeKeySet(UserDTO userDTO) throws InternalUserNotFoundException;

    /**
     * Changes the password of a user
     *
     * @param userPasswordChangeRequestDTO the data needed to change the password
     * @throws InternalUserNotFoundException in case the user was not found
     * @throws InternalBadRequestException in case the password change keys did not match or the password of the user was not reset or the new password was invalid
     */
    void changePassword(UserPasswordChangeRequestDTO userPasswordChangeRequestDTO) throws InternalUserNotFoundException, InternalBadRequestException;
}
