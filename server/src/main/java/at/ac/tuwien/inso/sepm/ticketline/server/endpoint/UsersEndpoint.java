package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.exception.UserValidatorException;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.validator.UserValidator;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.user.UserMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.InvalidRequestException;
import at.ac.tuwien.inso.sepm.ticketline.server.service.UserService;
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

    @PostMapping("enable")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("enable a disabled User")
    public void enableUser(@RequestBody UserDTO userDTO) {
        try {
            UserValidator.validateUser(userDTO);
            userService.enableUser(userMapper.userDTOToUser(userDTO));
        } catch (UserValidatorException e) {
            throw new InvalidRequestException();
        }
    }
}
