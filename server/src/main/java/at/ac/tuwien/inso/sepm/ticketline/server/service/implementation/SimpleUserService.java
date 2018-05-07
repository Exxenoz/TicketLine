package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.UsersRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.lang.invoke.MethodHandles;

@Service
public class SimpleUserService implements UserService {

    private UsersRepository usersRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public SimpleUserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDTO enableUser(User user) {
        user.setEnabled(true);
        user = usersRepository.save(user);

        return UserDTO.builder()
            .id(user.getId())
            .username(user.getUsername())
            .password(user.getPassword())
            .enabled(user.isEnabled())
            .build();
    }

    @Override
    public void disableUser(User user) {
        LOGGER.info(String.format("Disabling user:" + user.getUsername()));
        user.setEnabled(false);
        usersRepository.save(user);
    }

    @Override
    public boolean increaseStrikes(User user) {
        LOGGER.info(String.format("Increasing strikes for user:" + user.getUsername()));
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
}
