package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserCreateRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserPasswordChangeRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserPasswordResetRequestDTO;

import java.util.List;

public interface UserService {

    /**
     * Find all user entries
     *
     * @return all user entries
     * @throws DataAccessException in case something went wrong
     */
    List<UserDTO> findAll() throws DataAccessException;

    /**
     * Finds a page of all users
     *
     * @param request object specifing the request
     * @return a page of all users
     * @throws DataAccessException in case something went wrong
     */
    PageResponseDTO<UserDTO> findAll(PageRequestDTO request) throws DataAccessException;

    /**
     * Enables the given user entry
     *
     * @param userDTO the user entry to be enabled
     * @throws DataAccessException in case something went wrong
     */
    void enableUser(UserDTO userDTO) throws DataAccessException;

    /**
     * Disables the given user entry
     *
     * @param userDTO the user entry to be disabled
     * @throws DataAccessException in case something went wrong
     */
    void disableUser(UserDTO userDTO) throws DataAccessException;

    /**
     * Create a user with the specified data transfer object.
     *
     * @param userCreateRequestDTO information about the user to create
     * @return the created user
     * @throws DataAccessException in case something went wrong
     */
    UserDTO create(UserCreateRequestDTO userCreateRequestDTO) throws DataAccessException;

    /**
     * Reset a users password, so he has to specify a new one at his next login
     *
     * @param userPasswordResetRequestDTO information for the reset process
     * @throws DataAccessException in case something went wrong
     * @return the updated user
     */
    UserDTO resetPassword(UserPasswordResetRequestDTO userPasswordResetRequestDTO) throws DataAccessException;

    /**
     * Change a users password, with a given token to authenticate
     *
     * @param userPasswordChangeRequestDTO information for the change process
     * @throws DataAccessException in case something went wrong
     */
    void changePassword(UserPasswordChangeRequestDTO userPasswordChangeRequestDTO) throws DataAccessException;
}
