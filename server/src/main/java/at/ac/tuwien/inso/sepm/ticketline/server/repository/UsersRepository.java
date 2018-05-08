package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UsersRepository extends JpaRepository<User, Long> {

    /**
     * Find a User with the username
     *
     * @param username the username used as filter
     * @return the user with the username
     */
    User findByUsername(String username);
}
