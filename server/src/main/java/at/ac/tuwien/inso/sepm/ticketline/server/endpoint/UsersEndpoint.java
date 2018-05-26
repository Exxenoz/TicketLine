package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.exception.UserValidatorException;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.validator.UserValidator;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.user.UserMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.InvalidRequestException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.UsernameAlreadyTakenException;
import at.ac.tuwien.inso.sepm.ticketline.server.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
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

    @GetMapping("all")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("find all existing Users")
    public List<UserDTO> findAll() {
        return userMapper.userListToUserDTOList(userService.findAll());
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("find all existing Users")
    public PageResponseDTO<UserDTO> findAll(@RequestBody final PageRequestDTO pageRequestDTO) {
        Page<User> userPage = (userService.findAll(pageRequestDTO.getPageable()));
        List<UserDTO> customerDTOList = userMapper.userListToUserDTOList(userPage.getContent());
        return new PageResponseDTO<>(customerDTOList, userPage.getTotalPages());
    }

    @PostMapping("enable")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("enable a disabled User")
    public void enableUser(@RequestBody UserDTO userDTO) {
        try {
            UserValidator.validateExistingUser(userDTO);
            userService.enableUser(userMapper.userDTOToUser(userDTO));
        } catch (UserValidatorException e) {
            throw new InvalidRequestException();
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Create a new user")
    public UserDTO save(@RequestBody UserDTO userDTO) {
        try {
            return userService.save(userDTO);
        } catch (UserValidatorException e) {
            throw new InvalidRequestException();
        }
    }
}
