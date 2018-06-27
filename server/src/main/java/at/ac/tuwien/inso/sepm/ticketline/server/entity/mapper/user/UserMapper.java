package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.user;

import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Converts a user entity object to an user DTO
     *
     * @param user the object to be converted
     * @return the converted DTO
     */
    UserDTO userToUserDTO(User user);

    /**
     * Converts a user DTO into a user entity object
     *
     * @param user the DTO to be converted
     * @return the converted entity
     */
    User userDTOToUser(UserDTO user);

    /**
     * Converts a user list into a user DTO list
     *
     * @param users the user list to be converted
     * @return the converted list
     */
    List<UserDTO> userListToUserDTOList(List<User> users);

    /**
     * Converts a user DTO list into a user entity list
     *
     * @param users the DTO list to be converted
     * @return the converted list
     */
    List<User> userListDTOToUserList(List<UserDTO> users);
}
