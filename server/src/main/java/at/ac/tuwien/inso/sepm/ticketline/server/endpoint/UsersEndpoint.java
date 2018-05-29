package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.exception.UserValidatorException;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserPasswordChangeRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserPasswordResetRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.user.UserMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.endpoint.HttpBadRequestException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.endpoint.HttpConflictException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.*;
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

    @GetMapping("all")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("find all existing Users")
    public List<UserDTO> findAll() {
        return userService.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("find all existing Users")
    public PageResponseDTO<UserDTO> findAll(@RequestBody final PageRequestDTO pageRequestDTO) {
        return userService.findAll(pageRequestDTO.getPageable());
    }

    @PostMapping("enable")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("enable a disabled User")
    public void enableUser(@RequestBody UserDTO userDTO) {
        try {
            userService.enableUser(userDTO);
        } catch (InternalUserValidationException e) {
            throw new HttpBadRequestException();
        } catch (InternalUserNotFoundException e) {
            throw new HttpBadRequestException();
        }
    }

    @PostMapping("/disable")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Disable an enabled user")
    public void disableUser(@RequestBody UserDTO userDTO) {
        try {
            userService.disableUser(userDTO);
        } catch (InternalUserNotFoundException e) {
            throw new HttpBadRequestException();
        } catch (InternalForbiddenException e) {
            throw new HttpBadRequestException();
        } catch (InternalUserValidationException e) {
            throw new HttpBadRequestException();
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Create a new user")
    public UserDTO save(@RequestBody UserDTO userDTO) {
        try {
            return userService.save(userDTO);
        } catch (InternalUserValidationException e) {
            throw new HttpBadRequestException();
        } catch (InternalUsernameConflictException e) {
            throw new HttpConflictException();
        }
    }

    @PostMapping("/password/reset")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Reset password of the specified user")
    public void resetPassword(@RequestBody final UserPasswordResetRequestDTO userPasswordResetRequestDTO) {
        try {
            userService.resetPassword(userPasswordResetRequestDTO);
        } catch (InternalUserNotFoundException e) {
            throw new HttpBadRequestException();
        } catch (InternalBadRequestException e) {
            throw new HttpBadRequestException();
        } catch (InternalUserValidationException e) {
            throw new HttpBadRequestException();
        }
    }

    @PostMapping("/password/change")
    // No authorization needed
    @ApiOperation("Change password of the specified user")
    public void changePassword(@RequestBody final UserPasswordChangeRequestDTO userPasswordChangeRequestDTO) {
        try {
            userService.changePassword(userPasswordChangeRequestDTO);
        } catch (InternalUserNotFoundException e) {
            throw new HttpBadRequestException();
        } catch (InternalBadRequestException e) {
            throw new HttpBadRequestException();
        }
    }
}
