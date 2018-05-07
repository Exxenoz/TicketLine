package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;

import java.util.List;

public interface UserRestClient {
    void enableUser(UserDTO user) throws DataAccessException;

    List<UserDTO> findAll() throws DataAccessException;
}
