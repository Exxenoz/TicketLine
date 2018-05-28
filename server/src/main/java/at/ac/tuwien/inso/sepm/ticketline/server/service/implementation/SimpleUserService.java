package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.exception.UserValidatorException;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserPasswordChangeRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserPasswordResetRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.validator.UserValidator;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.user.UserMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.ForbiddenException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.InvalidRequestException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.UserRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.UsernameAlreadyTakenException;
import at.ac.tuwien.inso.sepm.ticketline.server.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private UserRepository userRepository;
    private UserMapper userMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public SimpleUserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public void enableUser(UserDTO userDTO) throws UserValidatorException {
        LOGGER.info("Enable user {}", userDTO);

        UserValidator.validateExistingUser(userDTO);

        User user = userRepository.findByUsername(userDTO.getUsername());
        if (user == null) {
            throw new NotFoundException();
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
    public void disableUser(UserDTO userDTO) throws UserValidatorException {
        LOGGER.info("Disable user {}", userDTO);

        UserValidator.validateExistingUser(userDTO);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName().equals(userDTO.getUsername())) {
            throw new ForbiddenException();
        }

        User user = userRepository.findByUsername(userDTO.getUsername());
        if (user == null) {
            throw new NotFoundException();
        }

        user.setEnabled(false);

        userRepository.save(user);
    }

    @Override
    public boolean increaseStrikes(UserDTO userDTO) throws UserValidatorException {
        LOGGER.info("Increase strikes for user {}", userDTO);

        UserValidator.validateExistingUser(userDTO);

        User user = userRepository.findByUsername(userDTO.getUsername());
        if (user == null) {
            throw new NotFoundException();
        }

        if(!user.isEnabled()) {
            return true;
        }

        int strike = user.getStrikes();
        strike += 1;

        user.setStrikes(strike);
        LOGGER.info(String.format("Increasing strikes for user: %s to amount: %d", user.getUsername(), user.getStrikes()));

        if(strike >= 5) {
            user.setEnabled(false);
            userRepository.save(user);
            return true;
        }

        userRepository.save(user);
        return false;
    }

    @Override
    public UserDTO findUserByName(String name) {
        User user = userRepository.findByUsername(name);
        if (user == null) {
            throw new NotFoundException();
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
    public UserDTO save(UserDTO userDTO) throws UserValidatorException, UsernameAlreadyTakenException {
        LOGGER.info("Save user {}", userDTO);

        UserValidator.validateNewUser(userDTO);

        if (userRepository.findByUsername(userDTO.getUsername()) != null) {
            throw new UsernameAlreadyTakenException();
        }

        var user = userMapper.userDTOToUser(userDTO);
        return userMapper.userToUserDTO(userRepository.save(user));
    }

    @Override
    public void resetPassword(UserPasswordResetRequestDTO userPasswordResetRequestDTO) throws UserValidatorException {
        LOGGER.info("Reset password for user {}", userPasswordResetRequestDTO.getUserDTO());

        UserValidator.validateExistingUser(userPasswordResetRequestDTO.getUserDTO());

        String passwordChangeKey = userPasswordResetRequestDTO.getPasswordChangeKey();
        if (passwordChangeKey == null || passwordChangeKey.length() != 8) {
            throw new InvalidRequestException();
        }

        User user = userRepository.findByUsername(userPasswordResetRequestDTO.getUserDTO().getUsername());
        if (user == null) {
            throw new NotFoundException();
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        user.setPassword("");
        user.setPasswordChangeKey(passwordEncoder.encode(passwordChangeKey));

        userRepository.save(user);
    }

    @Override
    public boolean isPasswordChangeKeySet(UserDTO userDTO) {
        LOGGER.info("Check password change key for user {}", userDTO);

        User user = userRepository.findByUsername(userDTO.getUsername());
        if (user == null) {
            throw new NotFoundException();
        }

        return user.getPasswordChangeKey() != null && !user.getPasswordChangeKey().isEmpty();
    }

    @Override
    public void changePassword(UserPasswordChangeRequestDTO userPasswordChangeRequestDTO) {
        LOGGER.info("Change password for user {}", userPasswordChangeRequestDTO.getUsername());

        User user = userRepository.findByUsername(userPasswordChangeRequestDTO.getUsername());
        if (user == null) {
            throw new NotFoundException();
        }

        if (user.getPasswordChangeKey() == null) {
            throw new InvalidRequestException();
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        if (!user.getPasswordChangeKey().equals(passwordEncoder.encode(userPasswordChangeRequestDTO.getPasswordChangeKey()))) {
            throw new InvalidRequestException();
        }

        user.setPassword(passwordEncoder.encode(userPasswordChangeRequestDTO.getPassword()));
        user.setPasswordChangeKey(null);
        userRepository.save(user);
    }
}
