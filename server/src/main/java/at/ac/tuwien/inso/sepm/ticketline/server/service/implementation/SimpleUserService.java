package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.exception.UserValidatorException;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.validator.UserValidator;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.user.UserMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.UsernameAlreadyTakenException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.UserRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public void enableUser(User user) throws UserValidatorException {
        UserValidator.validateExistingUser(UserDTO.builder()
            .id(user.getId())
            .username(user.getUsername())
            .password(user.getPassword())
            .enabled(user.isEnabled())
            .strikes(user.getStrikes())
            .build()
        );
        LOGGER.info("Enabling user: {}", user.getUsername());
        user.setEnabled(true);
        user.setStrikes(0);
        userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void disableUser(User user) {
        LOGGER.info(String.format("Disabling user: %s", user.getUsername()));
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    public boolean increaseStrikes(User user) throws UserValidatorException {
        UserValidator.validateExistingUser(UserDTO.builder()
            .id(user.getId())
            .username(user.getUsername())
            .password(user.getPassword())
            .enabled(user.isEnabled())
            .strikes(user.getStrikes())
            .build()
        );

        if(!user.isEnabled()) {
            return true;
        }
        int strike = user.getStrikes();
        strike += 1;

        user.setStrikes(strike);
        LOGGER.info(String.format("Increasing strikes for user: %s to amount: %d", user.getUsername(), user.getStrikes()));

        if(strike >= 5) {
            this.disableUser(user);
            return true;
        }

        userRepository.save(user);
        return false;
    }

    @Override
    public User findUserByName(String name) {
        User user = userRepository.findByUsername(name);
        if (user == null) {
            throw new NotFoundException();
        }
        return user;
    }

    @Override
    public void initiateSecurityUser(org.springframework.security.core.userdetails.User user) {
        User assimilatedUser = findUserByName(user.getUsername());

        assimilatedUser.setUsername(user.getUsername());
        assimilatedUser.setPassword(user.getPassword());
        assimilatedUser.setStrikes(0);
        assimilatedUser.setEnabled(user.isEnabled());

        userRepository.save(assimilatedUser);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public UserDTO save(UserDTO userDTO) throws UserValidatorException, UsernameAlreadyTakenException {
        UserValidator.validateNewUser(userDTO);

        if (userRepository.findByUsername(userDTO.getUsername()) != null) {
            throw new UsernameAlreadyTakenException("User validation failed, because username is already taken!");
        }

        var user = userMapper.userDTOToUser(userDTO);
        return userMapper.userToUserDTO(userRepository.save(user));
    }
}
