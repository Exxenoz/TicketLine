package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.UserRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimpleUserService implements UserService {


    private final UserRestClient userRestClient;

    public SimpleUserService(UserRestClient userRestClient) {
        this.userRestClient = userRestClient;
    }

    @Override
    public List<UserDTO> findAll() throws DataAccessException {
        return userRestClient.findAll();
    }

    @Override
    public PageResponseDTO<UserDTO> findAll(PageRequestDTO request) throws DataAccessException {
        return userRestClient.findAll(request);
    }

    @Override
    public void enableUser(UserDTO userDTO) throws DataAccessException {
        userRestClient.enableUser(userDTO);
    }
}
