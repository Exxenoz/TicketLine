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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
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
        LOGGER.info("Enable user \'{}\'", userDTO.getUsername());

        try {
            UserValidator.validateExistingUser(userDTO);
        } catch (UserValidatorException e) {
            LOGGER.warn("User was invalid: {}", e);
            throw new InternalUserValidationException();
        }

        User user = userRepository.findByUsername(userDTO.getUsername());
        if (user == null) {
            LOGGER.warn("User could not be found in the db");
            throw new InternalUserNotFoundException();
        }

        user.setEnabled(true);
        user.setStrikes(0);

        user = userRepository.save(user);
        LOGGER.debug("Successfully enabled User \'{}\'", user.getUsername());
    }

    @Override
    public List<UserDTO> findAll() {
        LOGGER.info("findAll Users");
        return userMapper.userListToUserDTOList(userRepository.findAll());
    }

    @Override
    public void disableUser(UserDTO userDTO) throws InternalUserValidationException, InternalUserNotFoundException {
        LOGGER.info("Disable user \'{}\'", userDTO.getUsername());

        try {
            UserValidator.validateExistingUser(userDTO);
        } catch (UserValidatorException e) {
            LOGGER.warn("User was invalid: {}", e);
            throw new InternalUserValidationException();
        }

        User user = userRepository.findByUsername(userDTO.getUsername());
        if (user == null) {
            LOGGER.warn("User was not found in the db");
            throw new InternalUserNotFoundException();
        }

        user.setEnabled(false);

        user = userRepository.save(user);
        LOGGER.debug("Successfully disabled User \'{}\'", user.getUsername());
    }

    @Override
    public void disableUserButNotSelf(UserDTO userDTO) throws InternalUserValidationException, InternalUserTriedToDisableHimselfException, InternalUserNotFoundException {
        LOGGER.info("Try to disable user \'{}\'", userDTO.getUsername());

        Authentication authentication = authenticationFacade.getAuthentication();
        if (authentication.getName().equals(userDTO.getUsername())) {
            LOGGER.error("User tried to disable himself");
            throw new InternalUserTriedToDisableHimselfException();
        }

        disableUser(userDTO);
    }

    @Override
    public void increaseStrikesAndDisableUserIfStrikesAreTooHigh(UserDTO userDTO) throws InternalUserNotFoundException {
        LOGGER.info("Increase strikes for user \'{}\'", userDTO.getUsername());

        User user = userRepository.findByUsername(userDTO.getUsername());
        if (user == null) {
            LOGGER.error("User was not found in the db");
            throw new InternalUserNotFoundException();
        }

        int userStrikes = user.getStrikes();

        if (userStrikes <= UserService.ALLOWED_STRIKES) {
            userStrikes++;

            LOGGER.debug("Increasing strikes for user: {} to amount: {}", user.getUsername(), userStrikes);
            user.setStrikes(userStrikes);
        }

        if (userStrikes > UserService.ALLOWED_STRIKES)
        {
            LOGGER.debug("Disable user {}, because strike counter is greater than allowed strikes!", user.getUsername());
            user.setEnabled(false);
        }


        userRepository.save(user);
    }

    @Override
    public UserDTO findUserByName(String name) throws InternalUserNotFoundException {
        LOGGER.info("Find user by username {}", name);
        User user = userRepository.findByUsername(name);
        if (user == null) {
            LOGGER.error("User could not be found in the db");
            throw new InternalUserNotFoundException();
        }
        return userMapper.userToUserDTO(user);
    }

    @Override
    public void initiateSecurityUser(org.springframework.security.core.userdetails.User user) {
        LOGGER.info("init sec user {}", user);
        User assimilatedUser = userRepository.findByUsername(user.getUsername());

        assimilatedUser.setUsername(user.getUsername());
        assimilatedUser.setPassword(user.getPassword());
        assimilatedUser.setStrikes(0);
        assimilatedUser.setEnabled(user.isEnabled());

        userRepository.save(assimilatedUser);
    }

    @Override
    public PageResponseDTO<UserDTO> findAll(Pageable pageable) {
        LOGGER.info("Get Page {} of all Users", pageable.getPageNumber());
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
            LOGGER.warn("User was invalid: {}", e.getMessage());
            throw new InternalUserValidationException();
        }

        if (userRepository.findByUsername(userCreateRequestDTO.getUsername()) != null) {
            LOGGER.error("The given username \'{}\' is already used in db", userCreateRequestDTO.getUsername());
            throw new InternalUsernameConflictException();
        }

        var user = userMapper.userDTOToUser(userCreateRequestDTO);
        user.setPassword(new BCryptPasswordEncoder(10).encode(userCreateRequestDTO.getPassword()));
        user = userRepository.save(user);
        LOGGER.debug("Successfully created User \'{}\'", user.getUsername());
        return userMapper.userToUserDTO(user);
    }

    @Override
    public void resetStrikes(UserDTO userDTO) throws InternalUserValidationException, InternalUserNotFoundException {
        LOGGER.info("Reset strikes for user \'{}\'", userDTO.getUsername());

        try {
             UserValidator.validateDTO(userDTO);
        } catch (UserValidatorException e) {
            LOGGER.warn("User was invalid: {}", e.getMessage());
            throw new InternalUserValidationException();
        }
        User u = userRepository.findByUsername(userDTO.getUsername());

        if(u == null) {
            LOGGER.error("User was not found in the db");
            throw new InternalUserNotFoundException();
        } else {
            u.setStrikes(STRIKE_RESET_VALUE);
            userRepository.save(u);
            LOGGER.info("Successfully reset strikes for \'{}\'", u.getUsername());
        }

    }

    @Override
    public UserDTO resetPassword(UserPasswordResetRequestDTO userPasswordResetRequestDTO) throws InternalUserValidationException, InternalUserNotFoundException, InternalBadRequestException {
        LOGGER.info("Reset password for user \'{}\'", userPasswordResetRequestDTO.getUserDTO());

        try {
            UserValidator.validateExistingUser(userPasswordResetRequestDTO.getUserDTO());
        } catch (UserValidatorException e) {
            LOGGER.warn("User was invalid: {}", e.getMessage());
            throw new InternalUserValidationException();
        }

        String passwordChangeKey = userPasswordResetRequestDTO.getPasswordChangeKey();
        if (passwordChangeKey == null || passwordChangeKey.length() != 8) {
            LOGGER.warn("PasswordReset data was invalid");
            throw new InternalBadRequestException();
        }

        User user = userRepository.findByUsername(userPasswordResetRequestDTO.getUserDTO().getUsername());
        if (user == null) {
            LOGGER.error("Could not found User in the db");
            throw new InternalUserNotFoundException();
        }

        user.setPassword("");
        user.setPasswordChangeKey(new BCryptPasswordEncoder(10).encode(passwordChangeKey));
        user.setEnabled(true);
        user.setStrikes(0);
        user = userRepository.save(user);
        LOGGER.debug("Successfully Reset Password for User \'{}\'", user);
        return userMapper.userToUserDTO(user);
    }

    @Override
    public boolean isPasswordChangeKeySet(UserDTO userDTO) throws InternalUserNotFoundException {
        LOGGER.info("Check password change key for user {}", userDTO);

        User user = userRepository.findByUsername(userDTO.getUsername());
        if (user == null) {
            LOGGER.error("Could not find User in the db");
            throw new InternalUserNotFoundException();
        }

        return user.getPasswordChangeKey() != null && !user.getPasswordChangeKey().isEmpty();
    }

    @Override
    public void changePassword(UserPasswordChangeRequestDTO userPasswordChangeRequestDTO) throws InternalUserNotFoundException, InternalBadRequestException {
        LOGGER.info("Change password for user \'{}\'", userPasswordChangeRequestDTO.getUsername());

        try {
            UserValidator.validateUsername(userPasswordChangeRequestDTO.getUsername());
            UserValidator.validatePlainTextPassword(userPasswordChangeRequestDTO.getPassword());
            UserValidator.validatePasswordChangeKey(userPasswordChangeRequestDTO.getPasswordChangeKey());
        } catch (UserValidatorException e) {
            LOGGER.warn("UserPasswordChangeRequest was invalid: {}", e.getMessage());
            throw new InternalBadRequestException();
        }

        User user = userRepository.findByUsername(userPasswordChangeRequestDTO.getUsername());
        if (user == null) {
            LOGGER.error("Could not find User in db");
            throw new InternalUserNotFoundException();
        }

        if (!new BCryptPasswordEncoder(10).matches(userPasswordChangeRequestDTO.getPasswordChangeKey(), user.getPasswordChangeKey())) {
            LOGGER.error("The passwordChangeKey of the User {} does not match with the given ChangeKey");
            throw new InternalBadRequestException();
        }

        user.setPassword(new BCryptPasswordEncoder(10).encode(userPasswordChangeRequestDTO.getPassword()));
        user.setPasswordChangeKey(null);
        user = userRepository.save(user);
        LOGGER.debug("Successfully changed the password of the User \'{}\'", user.getUsername());
    }
}
