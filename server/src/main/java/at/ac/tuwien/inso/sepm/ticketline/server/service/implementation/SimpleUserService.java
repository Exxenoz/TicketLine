package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.exception.UserValidatorException;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.validator.UserValidator;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.UsersRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class SimpleUserService implements UserService {

    private UsersRepository usersRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public SimpleUserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public void enableUser(User user) throws UserValidatorException {
        UserValidator.validateUser(UserDTO.builder()
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
        usersRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return usersRepository.findAll();
    }

    @Override
    public void disableUser(User user) {
        LOGGER.info(String.format("Disabling user: %s", user.getUsername()));
        user.setEnabled(false);
        usersRepository.save(user);
    }

    @Override
    public boolean increaseStrikes(User user) throws UserValidatorException {
        UserValidator.validateUser(UserDTO.builder()
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

        usersRepository.save(user);
        return false;
    }

    @Override
    public User findUserByName(String name) {
        User user = usersRepository.findByUsername(name);
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

        usersRepository.save(assimilatedUser);
    }
}
