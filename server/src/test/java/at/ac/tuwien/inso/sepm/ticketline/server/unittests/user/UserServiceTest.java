package at.ac.tuwien.inso.sepm.ticketline.server.unittests.user;

import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserCreateRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserPasswordChangeRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserPasswordResetRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.user.UserMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.*;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.UserRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.security.IAuthenticationFacade;
import at.ac.tuwien.inso.sepm.ticketline.server.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.implementation.SimpleUserService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("unit-test")
public class UserServiceTest {
    private static final String AUTH_USERNAME = "hans";
    private static final String TEST_USERNAME = "test";
    private static final String TEST_PASSWORD = "test";

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    @Autowired
    private DataSource dataSource;

    private JdbcUserDetailsManager mgr;
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;
    private UserRepository userRepository;
    private UserService userService;

    @Before
    public void setUp() {
        mgr = new JdbcUserDetailsManager();
        mgr.setDataSource(dataSource);
        passwordEncoder = new BCryptPasswordEncoder(10);
        userMapper = mock(UserMapper.class);
        userRepository = mock(UserRepository.class);
        userService = new SimpleUserService(authenticationFacade, userRepository, userMapper);
    }

    @After
    public void tearDown() {
    }

    private UserDTO createValidTestUserDTO() {
        UserDTO.UserDTOBuilder builder = UserDTO.builder();
        builder.id(1L);
        builder.username(TEST_USERNAME);
        builder.enabled(true);
        builder.strikes(0);
        builder.roles(new HashSet<>(Arrays.asList("USER")));
        return builder.build();
    }

    private User createValidTestUser() {
        User.UserBuilder builder = User.builder();
        builder.id(1L);
        builder.username(TEST_USERNAME);
        builder.password(new BCryptPasswordEncoder(10).encode(TEST_PASSWORD));
        builder.enabled(true);
        builder.strikes(0);
        builder.passwordChangeKey(null);
        builder.roles(new HashSet<>(Arrays.asList("USER")));
        builder.readNews(new HashSet<>());
        return builder.build();
    }

    @Test
    public void enableDisabledUserShouldSetEnabledToTrueAndStrikesToZero() throws InternalUserNotFoundException, InternalUserValidationException {
        User user = createValidTestUser();
        UserDTO userDTO = createValidTestUserDTO();

        user.setEnabled(false);
        userDTO.setEnabled(false);

        when(userRepository.findByUsername(anyString())).thenReturn(user);

        Assert.assertFalse(user.isEnabled());
        userService.enableUser(userDTO);
        Assert.assertTrue(user.isEnabled());
        Assert.assertTrue(user.getStrikes() == 0);
    }

    @Test(expected = InternalUserNotFoundException.class)
    public void enableNonExistentUserShouldThrowInternalUserNotFoundException() throws InternalUserNotFoundException, InternalUserValidationException {
        when(userRepository.findByUsername(anyString())).thenReturn(null);
        userService.enableUser(createValidTestUserDTO());
    }

    @Test
    public void disableUserShouldSetEnabledToFalse() throws InternalUserNotFoundException, InternalUserValidationException {
        User user = createValidTestUser();
        UserDTO userDTO = createValidTestUserDTO();

        user.setEnabled(true);
        userDTO.setEnabled(true);

        when(userRepository.findByUsername(anyString())).thenReturn(user);

        Assert.assertTrue(user.isEnabled());
        userService.disableUser(userDTO);
        Assert.assertFalse(user.isEnabled());
    }

    @Test(expected = InternalUserNotFoundException.class)
    public void disableNonExistentUserShouldThrowInternalUserNotFoundException() throws InternalUserNotFoundException, InternalUserValidationException {
        when(userRepository.findByUsername(anyString())).thenReturn(null);
        userService.disableUser(createValidTestUserDTO());
    }

    @Test(expected = InternalUserTriedToDisableHimselfException.class)
    @WithMockUser(username = TEST_USERNAME, roles = "ADMIN")
    public void disableSelfShouldThrowInternalUserTriedToDisableHimselfException() throws InternalUserNotFoundException, InternalUserTriedToDisableHimselfException, InternalUserValidationException {
        User user = createValidTestUser();
        UserDTO userDTO = createValidTestUserDTO();

        user.setEnabled(true);
        userDTO.setEnabled(true);

        when(userRepository.findByUsername(anyString())).thenReturn(user);

        userService.disableUserButNotSelf(userDTO);
    }

    @Test
    @WithMockUser(username = AUTH_USERNAME, roles = "ADMIN")
    public void disableUserButNotSelfShouldSetEnabledToFalse() throws InternalUserNotFoundException, InternalUserTriedToDisableHimselfException, InternalUserValidationException {
        User user = createValidTestUser();
        UserDTO userDTO = createValidTestUserDTO();

        user.setEnabled(true);
        userDTO.setEnabled(true);

        when(userRepository.findByUsername(anyString())).thenReturn(user);

        Assert.assertTrue(user.isEnabled());
        userService.disableUserButNotSelf(userDTO);
        Assert.assertFalse(user.isEnabled());
    }

    @Test(expected = InternalUserNotFoundException.class)
    @WithMockUser(username = AUTH_USERNAME, roles = "ADMIN")
    public void disableUserButNotSelfWithNonExistentUserShouldThrowInternalUserNotFoundException() throws InternalUserValidationException, InternalUserNotFoundException, InternalUserTriedToDisableHimselfException {
        when(userRepository.findByUsername(anyString())).thenReturn(null);
        userService.disableUserButNotSelf(createValidTestUserDTO());
    }

    @Test
    public void increaseStrikesShouldDisableUserIfStrikesAreTooHigh() throws InternalUserNotFoundException {
        User user = createValidTestUser();
        UserDTO userDTO = createValidTestUserDTO();

        user.setEnabled(true);
        userDTO.setEnabled(true);

        user.setStrikes(0);
        userDTO.setStrikes(0);

        when(userRepository.findByUsername(anyString())).thenReturn(user);

        for (int i = 0; i < UserService.ALLOWED_STRIKES; i++) {
            userService.increaseStrikesAndDisableUserIfStrikesAreTooHigh(userDTO);
            Assert.assertTrue(user.isEnabled());
            Assert.assertTrue(user.getStrikes() == (i + 1));
        }

        userService.increaseStrikesAndDisableUserIfStrikesAreTooHigh(userDTO);

        Assert.assertTrue(!user.isEnabled());
        Assert.assertTrue(user.getStrikes() == (UserService.ALLOWED_STRIKES + 1));
    }

    @Test(expected = InternalUserNotFoundException.class)
    public void findUserByNameWithNonExistentUserShouldThrowInternalUserNotFoundException() throws InternalUserNotFoundException {
        when(userRepository.findByUsername(anyString())).thenReturn(null);
        userService.findUserByName(TEST_USERNAME);
    }

    @Test
    public void resetStrikesShouldSetStrikesToZero() throws InternalUserNotFoundException, InternalUserValidationException {
        User user = createValidTestUser();
        UserDTO userDTO = createValidTestUserDTO();

        user.setStrikes(5);
        userDTO.setStrikes(5);

        when(userRepository.findByUsername(anyString())).thenReturn(user);

        Assert.assertTrue(user.getStrikes() == 5);
        userService.resetStrikes(userDTO);
        Assert.assertTrue(user.getStrikes() == UserService.STRIKE_RESET_VALUE);
    }

    @Test
    public void saveValidUserShouldSetPasswordHashAndReturnSavedUser() throws InternalUsernameConflictException, InternalUserValidationException {
        User user = createValidTestUser();
        UserDTO userDTO = createValidTestUserDTO();
        UserCreateRequestDTO userCreateRequestDTO = new UserCreateRequestDTO();

        userCreateRequestDTO.update(userDTO);
        userCreateRequestDTO.setPassword(TEST_PASSWORD);

        when(userMapper.userDTOToUser(any())).thenReturn(user);
        when(userMapper.userToUserDTO(any())).thenReturn(userDTO);
        when(userRepository.findByUsername(anyString())).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);

        UserDTO savedUserDTO = userService.save(userCreateRequestDTO);

        Assert.assertTrue(new BCryptPasswordEncoder(10).matches(TEST_PASSWORD, user.getPassword()));
        Assert.assertEquals(userDTO, savedUserDTO);
    }

    @Test(expected = InternalUsernameConflictException.class)
    public void saveUserWithAlreadyTakenUsernameShouldThrowInternalUsernameConflictException() throws InternalUsernameConflictException, InternalUserValidationException {
        User user = createValidTestUser();
        UserDTO userDTO = createValidTestUserDTO();
        UserCreateRequestDTO userCreateRequestDTO = new UserCreateRequestDTO();

        userCreateRequestDTO.update(userDTO);
        userCreateRequestDTO.setPassword(TEST_PASSWORD);

        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        userService.save(userCreateRequestDTO);
    }

    @Test(expected = InternalUserValidationException.class)
    public void saveUserWithInvalidUsernameShouldThrowInternalUserValidationException() throws InternalUsernameConflictException, InternalUserValidationException {
        User user = createValidTestUser();
        UserDTO userDTO = createValidTestUserDTO();
        UserCreateRequestDTO userCreateRequestDTO = new UserCreateRequestDTO();

        user.setUsername("39ir39f8387uhrd78fh87tg783z7h7874338478748h387438778377384847h478h7h87h34h7");
        userDTO.setUsername("39ir39f8387uhrd78fh87tg783z7h7874338478748h387438778377384847h478h7h87h34h7");

        userCreateRequestDTO.update(userDTO);
        userCreateRequestDTO.setPassword(TEST_PASSWORD);

        when(userMapper.userDTOToUser(any())).thenReturn(user);
        when(userMapper.userToUserDTO(any())).thenReturn(userDTO);
        when(userRepository.findByUsername(anyString())).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);

        userService.save(userCreateRequestDTO);
    }

    @Test(expected = InternalUserValidationException.class)
    public void saveUserWithEmptyUsernameShouldThrowInternalUserValidationException() throws InternalUsernameConflictException, InternalUserValidationException {
        User user = createValidTestUser();
        UserDTO userDTO = createValidTestUserDTO();
        UserCreateRequestDTO userCreateRequestDTO = new UserCreateRequestDTO();

        user.setUsername("");
        userDTO.setUsername("");

        userCreateRequestDTO.update(userDTO);
        userCreateRequestDTO.setPassword(TEST_PASSWORD);

        when(userMapper.userDTOToUser(any())).thenReturn(user);
        when(userMapper.userToUserDTO(any())).thenReturn(userDTO);
        when(userRepository.findByUsername(anyString())).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);

        userService.save(userCreateRequestDTO);
    }

    @Test(expected = InternalUserValidationException.class)
    public void saveUserWithInvalidPasswordShouldThrowInternalUserValidationException() throws InternalUsernameConflictException, InternalUserValidationException {
        User user = createValidTestUser();
        UserDTO userDTO = createValidTestUserDTO();
        UserCreateRequestDTO userCreateRequestDTO = new UserCreateRequestDTO();

        userCreateRequestDTO.update(userDTO);
        userCreateRequestDTO.setPassword("83hd8h(/$()URHDF/(D()HDSDQO($GO)G(h893hr9pH()E§JD(H$EP(FEP");

        when(userMapper.userDTOToUser(any())).thenReturn(user);
        when(userMapper.userToUserDTO(any())).thenReturn(userDTO);
        when(userRepository.findByUsername(anyString())).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);

        userService.save(userCreateRequestDTO);
    }

    @Test(expected = InternalUserValidationException.class)
    public void saveUserWithEmptyPasswordShouldThrowInternalUserValidationException() throws InternalUsernameConflictException, InternalUserValidationException {
        User user = createValidTestUser();
        UserDTO userDTO = createValidTestUserDTO();
        UserCreateRequestDTO userCreateRequestDTO = new UserCreateRequestDTO();

        userCreateRequestDTO.update(userDTO);
        userCreateRequestDTO.setPassword("");

        when(userMapper.userDTOToUser(any())).thenReturn(user);
        when(userMapper.userToUserDTO(any())).thenReturn(userDTO);
        when(userRepository.findByUsername(anyString())).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);

        userService.save(userCreateRequestDTO);
    }

    @Test
    public void resetPasswordShouldClearPasswordAndSetPasswordChangeKeyAndSetEnabledToTrueAndStrikeCounterToZero() throws InternalUserValidationException, InternalBadRequestException, InternalUserNotFoundException {
        User user = createValidTestUser();
        UserDTO userDTO = createValidTestUserDTO();
        UserPasswordResetRequestDTO userPasswordResetRequestDTO = new UserPasswordResetRequestDTO();

        userPasswordResetRequestDTO.setPasswordChangeKey("ABCDEFGH");
        userPasswordResetRequestDTO.setUserDTO(userDTO);

        when(userMapper.userToUserDTO(any())).thenReturn(userDTO);
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        userService.resetPassword(userPasswordResetRequestDTO);

        Assert.assertTrue(user.getPassword().isEmpty());
        Assert.assertTrue(new BCryptPasswordEncoder(10).matches("ABCDEFGH", user.getPasswordChangeKey()));
        Assert.assertTrue(user.isEnabled());
        Assert.assertTrue(user.getStrikes() == 0);
    }

    @Test(expected = InternalBadRequestException.class)
    public void resetPasswordWithInvalidPasswordChangeKeyShouldThrowInternalBadRequestException() throws InternalUserValidationException, InternalBadRequestException, InternalUserNotFoundException {
        User user = createValidTestUser();
        UserDTO userDTO = createValidTestUserDTO();
        UserPasswordResetRequestDTO userPasswordResetRequestDTO = new UserPasswordResetRequestDTO();

        userPasswordResetRequestDTO.setPasswordChangeKey("invalid");
        userPasswordResetRequestDTO.setUserDTO(userDTO);

        when(userMapper.userToUserDTO(any())).thenReturn(userDTO);
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        userService.resetPassword(userPasswordResetRequestDTO);
    }

    @Test(expected = InternalUserNotFoundException.class)
    public void resetPasswordWithInvalidUserShouldThrowInternalUserNotFoundException() throws InternalUserValidationException, InternalBadRequestException, InternalUserNotFoundException {
        User user = createValidTestUser();
        UserDTO userDTO = createValidTestUserDTO();
        UserPasswordResetRequestDTO userPasswordResetRequestDTO = new UserPasswordResetRequestDTO();

        userPasswordResetRequestDTO.setPasswordChangeKey("ABCDEFGH");
        userPasswordResetRequestDTO.setUserDTO(userDTO);

        when(userMapper.userToUserDTO(any())).thenReturn(userDTO);
        when(userRepository.findByUsername(anyString())).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);

        userService.resetPassword(userPasswordResetRequestDTO);
    }

    @Test
    public void isPasswordChangeKeySetShouldReturnTrueIfPasswordChangeKeyIsSet() throws InternalUserValidationException, InternalBadRequestException, InternalUserNotFoundException {
        User user = createValidTestUser();
        UserDTO userDTO = createValidTestUserDTO();
        UserPasswordResetRequestDTO userPasswordResetRequestDTO = new UserPasswordResetRequestDTO();

        userPasswordResetRequestDTO.setPasswordChangeKey("ABCDEFGH");
        userPasswordResetRequestDTO.setUserDTO(userDTO);

        when(userMapper.userToUserDTO(any())).thenReturn(userDTO);
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        userService.resetPassword(userPasswordResetRequestDTO);

        Assert.assertTrue(userService.isPasswordChangeKeySet(userDTO));
    }

    @Test
    public void changePasswordWithValidPasswordChangeKeyShouldChangePasswordAndSetPasswordChangeKeyToNull() throws InternalUserValidationException, InternalBadRequestException, InternalUserNotFoundException {
        User user = createValidTestUser();
        UserDTO userDTO = createValidTestUserDTO();
        UserPasswordResetRequestDTO userPasswordResetRequestDTO = new UserPasswordResetRequestDTO();
        UserPasswordChangeRequestDTO userPasswordChangeRequestDTO = new UserPasswordChangeRequestDTO();

        userPasswordResetRequestDTO.setPasswordChangeKey("ABCDEFGH");
        userPasswordResetRequestDTO.setUserDTO(userDTO);

        userPasswordChangeRequestDTO.setPassword(TEST_PASSWORD);
        userPasswordChangeRequestDTO.setUsername(userDTO.getUsername());
        userPasswordChangeRequestDTO.setPasswordChangeKey("ABCDEFGH");

        when(userMapper.userToUserDTO(any())).thenReturn(userDTO);
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        userService.resetPassword(userPasswordResetRequestDTO);
        userService.changePassword(userPasswordChangeRequestDTO);

        Assert.assertTrue(new BCryptPasswordEncoder(10).matches(TEST_PASSWORD, user.getPassword()));
        Assert.assertTrue(user.getPasswordChangeKey() == null);
    }

    @Test(expected = InternalBadRequestException.class)
    public void changePasswordWithInvalidPlainTextPasswordShouldThrowInternalBadRequestException() throws InternalUserValidationException, InternalBadRequestException, InternalUserNotFoundException {
        User user = createValidTestUser();
        UserDTO userDTO = createValidTestUserDTO();
        UserPasswordResetRequestDTO userPasswordResetRequestDTO = new UserPasswordResetRequestDTO();
        UserPasswordChangeRequestDTO userPasswordChangeRequestDTO = new UserPasswordChangeRequestDTO();

        userPasswordResetRequestDTO.setPasswordChangeKey("ABCDEFGH");
        userPasswordResetRequestDTO.setUserDTO(userDTO);

        userPasswordChangeRequestDTO.setPassword("8(DJ(§=$?DJijdjhdihfi8u9f8hsejh9fwhe8f9s89h8");
        userPasswordChangeRequestDTO.setUsername(userDTO.getUsername());
        userPasswordChangeRequestDTO.setPasswordChangeKey("ABCDEFGH");

        when(userMapper.userToUserDTO(any())).thenReturn(userDTO);
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        userService.resetPassword(userPasswordResetRequestDTO);
        userService.changePassword(userPasswordChangeRequestDTO);
    }

    @Test(expected = InternalBadRequestException.class)
    public void changePasswordWithEmptyPlainTextPasswordShouldThrowInternalBadRequestException() throws InternalUserValidationException, InternalBadRequestException, InternalUserNotFoundException {
        User user = createValidTestUser();
        UserDTO userDTO = createValidTestUserDTO();
        UserPasswordResetRequestDTO userPasswordResetRequestDTO = new UserPasswordResetRequestDTO();
        UserPasswordChangeRequestDTO userPasswordChangeRequestDTO = new UserPasswordChangeRequestDTO();

        userPasswordResetRequestDTO.setPasswordChangeKey("ABCDEFGH");
        userPasswordResetRequestDTO.setUserDTO(userDTO);

        userPasswordChangeRequestDTO.setPassword("");
        userPasswordChangeRequestDTO.setUsername(userDTO.getUsername());
        userPasswordChangeRequestDTO.setPasswordChangeKey("ABCDEFGH");

        when(userMapper.userToUserDTO(any())).thenReturn(userDTO);
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        userService.resetPassword(userPasswordResetRequestDTO);
        userService.changePassword(userPasswordChangeRequestDTO);
    }

    @Test(expected = InternalUserNotFoundException.class)
    public void changePasswordWithNonExistentUserShouldThrowInternalUserNotFoundException() throws InternalUserValidationException, InternalBadRequestException, InternalUserNotFoundException {
        User user = createValidTestUser();
        UserDTO userDTO = createValidTestUserDTO();
        UserPasswordResetRequestDTO userPasswordResetRequestDTO = new UserPasswordResetRequestDTO();
        UserPasswordChangeRequestDTO userPasswordChangeRequestDTO = new UserPasswordChangeRequestDTO();

        userPasswordResetRequestDTO.setPasswordChangeKey("ABCDEFGH");
        userPasswordResetRequestDTO.setUserDTO(userDTO);

        userPasswordChangeRequestDTO.setPassword(TEST_PASSWORD);
        userPasswordChangeRequestDTO.setUsername(userDTO.getUsername());
        userPasswordChangeRequestDTO.setPasswordChangeKey("ABCDEFGH");

        when(userMapper.userToUserDTO(any())).thenReturn(userDTO);
        when(userRepository.findByUsername(anyString())).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);

        userService.resetPassword(userPasswordResetRequestDTO);
        userService.changePassword(userPasswordChangeRequestDTO);
    }

    @Test(expected = InternalBadRequestException.class)
    public void changePasswordWithNonExistentPasswordChangeKeyShouldShowInternalBadRequestException() throws InternalUserValidationException, InternalBadRequestException, InternalUserNotFoundException {
        User user = createValidTestUser();
        UserDTO userDTO = createValidTestUserDTO();
        UserPasswordResetRequestDTO userPasswordResetRequestDTO = new UserPasswordResetRequestDTO();
        UserPasswordChangeRequestDTO userPasswordChangeRequestDTO = new UserPasswordChangeRequestDTO();

        userPasswordResetRequestDTO.setPasswordChangeKey("ABCDEFGH");
        userPasswordResetRequestDTO.setUserDTO(userDTO);

        userPasswordChangeRequestDTO.setPassword(TEST_PASSWORD);
        userPasswordChangeRequestDTO.setUsername(userDTO.getUsername());
        userPasswordChangeRequestDTO.setPasswordChangeKey("IJKLMNOP");

        when(userMapper.userToUserDTO(any())).thenReturn(userDTO);
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        userService.resetPassword(userPasswordResetRequestDTO);
        userService.changePassword(userPasswordChangeRequestDTO);
    }
}
