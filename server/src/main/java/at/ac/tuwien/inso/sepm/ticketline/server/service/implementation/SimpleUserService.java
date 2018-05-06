package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.UsersRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class SimpleUserService implements UserService {

    private UsersRepository usersRepository;

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
}
