package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
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
     * Find all user entries
     *
     * @return list of user entries
     * @throws DataAccessException in case something is wrong
     */
    List<UserDTO> findAll() throws DataAccessException;
}
