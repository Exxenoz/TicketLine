package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.UserRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.rest.exception.UserValidatorException;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserPasswordChangeRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserPasswordResetRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.validator.UserValidator;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
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
    public UserDTO resetPassword(UserPasswordResetRequestDTO userPasswordResetRequestDTO) throws DataAccessException {
        String resetKey = generateResetKey();
        userPasswordResetRequestDTO.setPasswordChangeKey(resetKey);
        return userRestClient.resetPassword(userPasswordResetRequestDTO);
    }

    @Override
    public void changePassword(UserPasswordChangeRequestDTO userPasswordChangeRequestDTO) throws DataAccessException {
        userRestClient.changePassword(userPasswordChangeRequestDTO);
    }

    // https://stackoverflow.com/a/157202
    private String generateResetKey() {
        final String AB = "123456789ABCDEFGHIJKLMNPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(8);
        for(int i = 0; i < 8; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
}
