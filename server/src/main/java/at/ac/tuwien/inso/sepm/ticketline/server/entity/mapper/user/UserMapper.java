package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.user;

import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO userToUserDTO(User user);

    User userDTOToUser(UserDTO user);

    List<UserDTO> userListToUserDTOList(List<User> user);

    List<User> userListDTOToUserList(List<UserDTO> user);


}
