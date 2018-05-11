package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
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
    public void enableUser(User user) {
        LOGGER.info("Enabling users: {}", user.getUsername());
        user.setEnabled(true);
        usersRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return usersRepository.findAll();
    }

    @Override
    public void disableUser(User user) {
        LOGGER.info(String.format("Disabling users: %s", user.getUsername()));
        user.setEnabled(false);
        usersRepository.save(user);
    }

    @Override
    public boolean increaseStrikes(User user) {
        if(!user.isEnabled()) {
            return true;
        }
        int strike = user.getStrikes();
        strike += 1;

        user.setStrikes(strike);
        LOGGER.info(String.format("Increasing strikes for users: %s to amount: %d", user.getUsername(), user.getStrikes()));

        if(strike >= 5) {
            user.setEnabled(false);
            LOGGER.info(String.format("User: %s has been disabled", user.getUsername(), user.getStrikes()));
            usersRepository.save(user);
            return true;
        }

        usersRepository.save(user);
        return false;
    }

    @Override
    public User findUserByName(String name) {
        return usersRepository.findByUsername(name);
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