package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;

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
     * Enables the given user entry
     *
     * @param userDTO the user entry to be enabled
     * @throws DataAccessException in case something went wrong
     */
    void enableUser(UserDTO userDTO) throws DataAccessException;
}
