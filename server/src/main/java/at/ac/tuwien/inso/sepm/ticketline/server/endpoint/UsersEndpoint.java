package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.user.UserMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.UserDataInvalidException;
import at.ac.tuwien.inso.sepm.ticketline.server.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.server.validation.UserValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
@Api(value = "users")
public class UsersEndpoint {

    private final UserService userService;
    private final UserMapper userMapper;

    public UsersEndpoint(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("find all existing Users")
    public List<UserDTO> findAll() {
        return userMapper.userListToUserDTOList(userService.findAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("enable disabled User")
    public void enableUser(@RequestBody UserDTO userDTO) {
        if (!UserValidator.validateUser(userDTO)) {
            throw new UserDataInvalidException();
        }
        userService.enableUser(userMapper.userDTOToUser(userDTO));
    }
}
