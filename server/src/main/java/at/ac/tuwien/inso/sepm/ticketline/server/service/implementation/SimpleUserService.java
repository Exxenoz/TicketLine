package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.UsersRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

public class SimpleUserService implements UserService {

    private UsersRepository usersRepository;

    public SimpleUserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public void enableUser(User user) {
        user.setEnabled(true);
        usersRepository.save(user);
    }
}
