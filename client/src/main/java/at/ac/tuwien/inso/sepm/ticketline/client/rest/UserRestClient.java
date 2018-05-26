package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;

import java.util.List;

public interface UserRestClient {

    /**
     * Enables the given user entry
     *
     * @param user the user entry to be enabled
     * @throws DataAccessException in case something went wrong
     */
    void enableUser(UserDTO user) throws DataAccessException;

    /**
     * Disables the given user entry
     *
     * @param user the user entry to be disabled
     * @throws DataAccessException in case something went wrong
     */
    void disableUser(UserDTO user) throws DataAccessException;

    /**
     * Find all user entries
     *
     * @return list of user entries
     * @throws DataAccessException in case something went wrong
     */
    List<UserDTO> findAll() throws DataAccessException;

    /**
     * finds a page of all users
     *
     * @param request the object that specifies the request
     * @return the Page response with the users in it
     * @throws DataAccessException in case something went wrong
     */
    PageResponseDTO<UserDTO> findAll(PageRequestDTO request) throws DataAccessException;

    /**
     * Create a user with the specified data transfer object.
     *
     * @param userDTO the user to create
     * @return the created user
     * @throws DataAccessException in case something went wrong
     */
    UserDTO create(UserDTO userDTO) throws DataAccessException;
}
