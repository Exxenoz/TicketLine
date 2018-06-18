package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.exception.UserValidatorException;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserCreateRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserPasswordChangeRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserPasswordResetRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.validator.UserValidator;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.user.UserMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.*;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.UserRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.security.IAuthenticationFacade;
import at.ac.tuwien.inso.sepm.ticketline.server.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class SimpleUserService implements UserService {

    private IAuthenticationFacade authenticationFacade;
    private UserRepository userRepository;
    private UserMapper userMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public SimpleUserService(IAuthenticationFacade authenticationFacade, UserRepository userRepository, UserMapper userMapper) {
        this.authenticationFacade = authenticationFacade;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public void enableUser(UserDTO userDTO) throws InternalUserValidationException, InternalUserNotFoundException {
        LOGGER.info("Enable user {}", userDTO);

        try {
            UserValidator.validateExistingUser(userDTO);
        } catch (UserValidatorException e) {
            throw new InternalUserValidationException();
        }

        User user = userRepository.findByUsername(userDTO.getUsername());
        if (user == null) {
            throw new InternalUserNotFoundException();
        }

        user.setEnabled(true);
        user.setStrikes(0);

        userRepository.save(user);
    }

    @Override
    public List<UserDTO> findAll() {
        return userMapper.userListToUserDTOList(userRepository.findAll());
    }

    @Override
    public void disableUser(UserDTO userDTO) throws InternalUserValidationException, InternalUserNotFoundException {
        LOGGER.info("Disable user {}", userDTO);

        try {
            UserValidator.validateExistingUser(userDTO);
        } catch (UserValidatorException e) {
            throw new InternalUserValidationException();
        }

        User user = userRepository.findByUsername(userDTO.getUsername());
        if (user == null) {
            throw new InternalUserNotFoundException();
        }

        user.setEnabled(false);

        userRepository.save(user);
    }

    @Override
    public void disableUserButNotSelf(UserDTO userDTO) throws InternalUserValidationException, InternalUserTriedToDisableHimselfException, InternalUserNotFoundException {
        LOGGER.info("Try to disable user {}", userDTO);

        Authentication authentication = authenticationFacade.getAuthentication();
        if (authentication.getName().equals(userDTO.getUsername())) {
            throw new InternalUserTriedToDisableHimselfException();
        }

        disableUser(userDTO);
    }

    @Override
    public void increaseStrikes(UserDTO userDTO) throws InternalUserValidationException, InternalUserNotFoundException,
    InternalUserDisabledException {
        LOGGER.info("Increase strikes for user {}", userDTO.getUsername());

        User user = userRepository.findByUsername(userDTO.getUsername());
        if (user == null) {
            throw new InternalUserNotFoundException();
        }

        if(!user.isEnabled()) {
            throw new InternalUserDisabledException("User is already disabled.");
        }

        int userStrikes = user.getStrikes();
        if(!(userStrikes > UserService.ALLOWED_STRIKES)) {
            user.setStrikes(user.getStrikes() + 1);
            LOGGER.info("Increasing strikes for user: {} to amount: {}", user.getUsername(), user.getStrikes());
        }
        userRepository.save(user);
    }

    @Override
    public boolean isUserBelowAllowedStrikes(UserDTO userDTO) throws InternalUserValidationException, InternalUserNotFoundException {
        try {
            UserValidator.validateExistingUser(userDTO);
        } catch(UserValidatorException e) {
            throw new InternalUserValidationException();
        }

        User user = userRepository.findByUsername(userDTO.getUsername());
        if(user == null) {
            throw new InternalUserNotFoundException();
        }

        if(user.getStrikes() > UserService.ALLOWED_STRIKES) {
            user.setEnabled(false);
            userRepository.save(user);
            return false;
        } else {
            return true;
        }

    }

    @Override
    public UserDTO findUserByName(String name) throws InternalUserNotFoundException {
        User user = userRepository.findByUsername(name);
        if (user == null) {
            throw new InternalUserNotFoundException();
        }
        return userMapper.userToUserDTO(user);
    }

    @Override
    public void initiateSecurityUser(org.springframework.security.core.userdetails.User user) {
        User assimilatedUser = userRepository.findByUsername(user.getUsername());

        assimilatedUser.setUsername(user.getUsername());
        assimilatedUser.setPassword(user.getPassword());
        assimilatedUser.setStrikes(0);
        assimilatedUser.setEnabled(user.isEnabled());

        userRepository.save(assimilatedUser);
    }

    @Override
    public PageResponseDTO<UserDTO> findAll(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        List<UserDTO> customerDTOList = userMapper.userListToUserDTOList(userPage.getContent());
        return new PageResponseDTO<>(customerDTOList, userPage.getTotalPages());
    }

    @Override
    public UserDTO save(UserCreateRequestDTO userCreateRequestDTO) throws InternalUserValidationException, InternalUsernameConflictException {
        LOGGER.info("Save user {}", userCreateRequestDTO.getUsername());

        try {
            UserValidator.validateNewUser(userCreateRequestDTO);
        } catch (UserValidatorException e) {
            throw new InternalUserValidationException();
        }

        if (userRepository.findByUsername(userCreateRequestDTO.getUsername()) != null) {
            throw new InternalUsernameConflictException();
        }

        var user = userMapper.userDTOToUser(userCreateRequestDTO);

        user.setPassword(new BCryptPasswordEncoder(10).encode(userCreateRequestDTO.getPassword()));

        return userMapper.userToUserDTO(userRepository.save(user));
    }

    @Override
    public void resetStrikes(UserDTO userDTO) throws InternalUserValidationException, InternalUserNotFoundException {
        LOGGER.info("Reset strikes for user {}", userDTO.getUsername());

        try {
             UserValidator.validateDTO(userDTO);
        } catch (UserValidatorException e) {
            throw new InternalUserValidationException();
        }
        User u = userRepository.findByUsername(userDTO.getUsername());

        if(u == null) {
            throw new InternalUserNotFoundException();
        } else {
            u.setStrikes(STRIKE_RESET_VALUE);
            userRepository.save(u);
            LOGGER.info("Successfully reset strikes for {}.", u.getUsername());
        }

    }

    @Override
    public UserDTO resetPassword(UserPasswordResetRequestDTO userPasswordResetRequestDTO) throws InternalUserValidationException, InternalUserNotFoundException, InternalBadRequestException {
        LOGGER.info("Reset password for user {}", userPasswordResetRequestDTO.getUserDTO());

        try {
            UserValidator.validateExistingUser(userPasswordResetRequestDTO.getUserDTO());
        } catch (UserValidatorException e) {
            throw new InternalUserValidationException();
        }

        String passwordChangeKey = userPasswordResetRequestDTO.getPasswordChangeKey();
        if (passwordChangeKey == null || passwordChangeKey.length() != 8) {
            throw new InternalBadRequestException();
        }

        User user = userRepository.findByUsername(userPasswordResetRequestDTO.getUserDTO().getUsername());
        if (user == null) {
            throw new InternalUserNotFoundException();
        }

        user.setPassword("");
        user.setPasswordChangeKey(new BCryptPasswordEncoder(10).encode(passwordChangeKey));
        user.setEnabled(true);
        user.setStrikes(0);

        return userMapper.userToUserDTO(userRepository.save(user));
    }

    @Override
    public boolean isPasswordChangeKeySet(UserDTO userDTO) throws InternalUserNotFoundException {
        LOGGER.info("Check password change key for user {}", userDTO);

        User user = userRepository.findByUsername(userDTO.getUsername());
        if (user == null) {
            throw new InternalUserNotFoundException();
        }

        return user.getPasswordChangeKey() != null && !user.getPasswordChangeKey().isEmpty();
    }

    @Override
    public void changePassword(UserPasswordChangeRequestDTO userPasswordChangeRequestDTO) throws InternalUserNotFoundException, InternalBadRequestException {
        LOGGER.info("Change password for user {}", userPasswordChangeRequestDTO.getUsername());

        try {
            UserValidator.validateUsername(userPasswordChangeRequestDTO.getUsername());
            UserValidator.validatePlainTextPassword(userPasswordChangeRequestDTO.getPassword());
            UserValidator.validatePasswordChangeKey(userPasswordChangeRequestDTO.getPasswordChangeKey());
        } catch (UserValidatorException e) {
            throw new InternalBadRequestException();
        }

        User user = userRepository.findByUsername(userPasswordChangeRequestDTO.getUsername());
        if (user == null) {
            throw new InternalUserNotFoundException();
        }

        if (!new BCryptPasswordEncoder(10).matches(userPasswordChangeRequestDTO.getPasswordChangeKey(), user.getPasswordChangeKey())) {
            throw new InternalBadRequestException();
        }

        user.setPassword(new BCryptPasswordEncoder(10).encode(userPasswordChangeRequestDTO.getPassword()));
        user.setPasswordChangeKey(null);
        userRepository.save(user);
    }
}
