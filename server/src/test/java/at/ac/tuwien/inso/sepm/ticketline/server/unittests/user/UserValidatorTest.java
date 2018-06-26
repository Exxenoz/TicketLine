package at.ac.tuwien.inso.sepm.ticketline.server.unittests.user;

import at.ac.tuwien.inso.sepm.ticketline.rest.exception.UserValidatorException;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserCreateRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.validator.UserValidator;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashSet;

@RunWith(SpringRunner.class)
@ActiveProfiles("unit-test")
public class UserValidatorTest {

    private static final long USER_ID = 1;
    private static final String USER_NAME = "test";
    private static final String USER_PASS = "test";
    private static final boolean USER_ENABLED = true;
    private static final int USER_STRIKES = 3;
    private static final HashSet<String> USER_ROLES = new HashSet<>(Arrays.asList("USER"));

    private UserDTO buildValidUserDTO() {
        return UserDTO.builder()
            .id(USER_ID)
            .username(USER_NAME)
            .enabled(USER_ENABLED)
            .strikes(USER_STRIKES)
            .roles(USER_ROLES)
            .build();
    }

    private UserCreateRequestDTO buildValidUserCreateRequestDTO() {
        UserCreateRequestDTO userCreateRequestDTO = new UserCreateRequestDTO();
        userCreateRequestDTO.setId(USER_ID);
        userCreateRequestDTO.setUsername(USER_NAME);
        userCreateRequestDTO.setPassword(USER_PASS);
        userCreateRequestDTO.setEnabled(USER_ENABLED);
        userCreateRequestDTO.setStrikes(USER_STRIKES);
        userCreateRequestDTO.setRoles(USER_ROLES);
        return userCreateRequestDTO;
    }

    @Test
    public void validateValidID() throws UserValidatorException {
        UserValidator.validateID(buildValidUserDTO());
    }

    @Test(expected = UserValidatorException.class)
    public void validateNegativeIDShouldThrowUserValidatorException() throws UserValidatorException {
        UserDTO userDTO = buildValidUserDTO();
        userDTO.setId(-1L);
        UserValidator.validateID(userDTO);
    }

    @Test(expected = UserValidatorException.class)
    public void validateNullIDShouldThrowUserValidatorException() throws UserValidatorException {
        UserDTO userDTO = buildValidUserDTO();
        userDTO.setId(null);
        UserValidator.validateID(userDTO);
    }

    @Test
    public void validateValidUsername() throws UserValidatorException {
        UserValidator.validateUsername(buildValidUserDTO());
    }

    @Test(expected = UserValidatorException.class)
    public void validateNullUsernameShouldThrowUserValidatorException() throws UserValidatorException {
        UserDTO userDTO = buildValidUserDTO();
        userDTO.setUsername(null);
        UserValidator.validateUsername(userDTO);
    }

    @Test(expected = UserValidatorException.class)
    public void validateEmptyUsernameShouldThrowUserValidatorException() throws UserValidatorException {
        UserDTO userDTO = buildValidUserDTO();
        userDTO.setUsername("");
        UserValidator.validateUsername(userDTO);
    }

    @Test(expected = UserValidatorException.class)
    public void validateTooShortUsernameShouldThrowUserValidatorException() throws UserValidatorException {
        UserDTO userDTO = buildValidUserDTO();
        userDTO.setUsername("AB");
        UserValidator.validateUsername(userDTO);
    }

    @Test(expected = UserValidatorException.class)
    public void validateTooLongUsernameShouldThrowUserValidatorException() throws UserValidatorException {
        UserDTO userDTO = buildValidUserDTO();
        userDTO.setUsername("ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJK");
        UserValidator.validateUsername(userDTO);
    }

    @Test(expected = UserValidatorException.class)
    public void validateUsernameWithInvalidCharactersShouldThrowUserValidatorException() throws UserValidatorException {
        UserDTO userDTO = buildValidUserDTO();
        userDTO.setUsername("§)($97--´´<");
        UserValidator.validateUsername(userDTO);
    }

    @Test
    public void validateValidPlainTextPassword() throws UserValidatorException {
        UserValidator.validatePlainTextPassword(buildValidUserCreateRequestDTO());
    }

    @Test(expected = UserValidatorException.class)
    public void validateNullPlainTextPasswordShouldThrowUserValidatorException() throws UserValidatorException {
        UserCreateRequestDTO userCreateRequestDTO = buildValidUserCreateRequestDTO();
        userCreateRequestDTO.setPassword(null);
        UserValidator.validatePlainTextPassword(userCreateRequestDTO);
    }

    @Test(expected = UserValidatorException.class)
    public void validateEmptyPlainTextPasswordShouldThrowUserValidatorException() throws UserValidatorException {
        UserCreateRequestDTO userCreateRequestDTO = buildValidUserCreateRequestDTO();
        userCreateRequestDTO.setPassword("");
        UserValidator.validatePlainTextPassword(userCreateRequestDTO);
    }

    @Test(expected = UserValidatorException.class)
    public void validateTooShortPlainTextPasswordShouldThrowUserValidatorException() throws UserValidatorException {
        UserCreateRequestDTO userCreateRequestDTO = buildValidUserCreateRequestDTO();
        userCreateRequestDTO.setPassword("AB");
        UserValidator.validatePlainTextPassword(userCreateRequestDTO);
    }

    @Test(expected = UserValidatorException.class)
    public void validateTooLongPlainTextPasswordShouldThrowUserValidatorException() throws UserValidatorException {
        UserCreateRequestDTO userCreateRequestDTO = buildValidUserCreateRequestDTO();
        userCreateRequestDTO.setPassword("ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJK");
        UserValidator.validatePlainTextPassword(userCreateRequestDTO);
    }

    @Test
    public void validateValidPasswordChangeKey() throws UserValidatorException {
        UserValidator.validatePasswordChangeKey("ABCDEFGH");
    }

    @Test(expected = UserValidatorException.class)
    public void validateInvalidPasswordChangeKeyShouldThrowUserValidatorException() throws UserValidatorException {
        UserValidator.validatePasswordChangeKey("ABCDEFGHI");
    }

    @Test
    public void validateValidStrikes() throws UserValidatorException {
        UserValidator.validateStrikes(buildValidUserDTO());
    }

    @Test(expected = UserValidatorException.class)
    public void validateNegativeStrikesShouldThrowUserValidatorException() throws UserValidatorException {
        UserDTO userDTO = buildValidUserDTO();
        userDTO.setStrikes(-1);
        UserValidator.validateStrikes(userDTO);
    }

    @Test(expected = UserValidatorException.class)
    public void validateTooManyStrikesShouldThrowUserValidatorException() throws UserValidatorException {
        UserDTO userDTO = buildValidUserDTO();
        userDTO.setStrikes(30);
        UserValidator.validateStrikes(userDTO);
    }

    @Test
    public void validateValidUserRoles() throws UserValidatorException {
        UserValidator.validateRoles(buildValidUserDTO());
    }

    @Test(expected = UserValidatorException.class)
    public void validateEmptyUserRolesShouldThrowUserValidatorException() throws UserValidatorException {
        UserDTO userDTO = buildValidUserDTO();
        userDTO.setRoles(new HashSet<>(Arrays.asList("")));
        UserValidator.validateRoles(userDTO);
    }

    @Test(expected = UserValidatorException.class)
    public void validateNullUserRolesShouldThrowUserValidatorException() throws UserValidatorException {
        UserDTO userDTO = buildValidUserDTO();
        userDTO.setRoles(null);
        UserValidator.validateRoles(userDTO);
    }

    @Test(expected = UserValidatorException.class)
    public void validateUserRolesWithTooManyCharactersShouldThrowUserValidatorException() throws UserValidatorException {
        UserDTO userDTO = buildValidUserDTO();
        userDTO.setRoles(new HashSet<>(Arrays.asList("ABCDEFGHIJKLMNOPQRSTUVXYZABCDEFGHIJKLMNOPQRSTUVXYZABCDEFGHIJKLMNOPQRSTUVXYZ")));
        UserValidator.validateRoles(userDTO);
    }
}
