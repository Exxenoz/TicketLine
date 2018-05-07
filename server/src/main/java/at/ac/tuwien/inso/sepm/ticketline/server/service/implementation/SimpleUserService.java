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
        LOGGER.info("Enabling user: {}", user.getUsername());
        user.setEnabled(true);
        usersRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return usersRepository.findAll();
    }

    @Override
    public void disableUser(User user) {
        LOGGER.info(String.format("Disabling user: %s",user.getUsername()));
        user.setEnabled(false);
        usersRepository.save(user);
    }

    @Override
    public boolean increaseStrikes(User user) {
        LOGGER.info(String.format("Increasing strikes for user: %s", user.getUsername()));
        int strike = user.getStrikes();
        strike += 1;

        if(strike >= 5) {
            //Make sure that the user is disabled
            if (user.isEnabled()) {
                user.setEnabled(false);
                usersRepository.save(user);
            }
            return true;
        }

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
