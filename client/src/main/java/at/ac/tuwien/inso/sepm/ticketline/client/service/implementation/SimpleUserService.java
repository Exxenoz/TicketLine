package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.UserRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.rest.exception.UserValidatorException;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserPasswordResetRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.validator.UserValidator;
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
        try {
            UserValidator.validateExistingUser(userDTO);
        } catch (UserValidatorException e) {
            throw new DataAccessException(e.getMessage());
        }
        userRestClient.enableUser(userDTO);
    }

    @Override
    public void disableUser(UserDTO userDTO) throws DataAccessException {
        try {
            UserValidator.validateExistingUser(userDTO);
        } catch (UserValidatorException e) {
            throw new DataAccessException(e.getMessage());
        }
        userRestClient.disableUser(userDTO);
    }

    @Override
    public UserDTO create(UserDTO userDTO) throws DataAccessException {
        return userRestClient.create(userDTO);
    }

    @Override
    public void resetPassword(UserPasswordResetRequestDTO userPasswordResetRequestDTO) throws DataAccessException {

    }
}
