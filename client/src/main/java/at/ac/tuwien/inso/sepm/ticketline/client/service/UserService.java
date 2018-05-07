package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> findAll() throws DataAccessException;

    void enableUser(UserDTO userDTO) throws DataAccessException;
}
