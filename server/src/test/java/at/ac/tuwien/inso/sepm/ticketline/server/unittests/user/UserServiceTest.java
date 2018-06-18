package at.ac.tuwien.inso.sepm.ticketline.server.unittests.user;

import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.user.UserMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalUserNotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalUserTriedToDisableHimselfException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalUserValidationException;
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
        builder.roles(new HashSet<String>(Arrays.asList("USER")));
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
    public void enableDisabledUserAndCheckIfEnabledIsTrueAndStrikesAreZero() throws InternalUserNotFoundException, InternalUserValidationException {
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

    @Test
    @WithMockUser(username = AUTH_USERNAME, roles = "ADMIN")
    public void disableUserAndCheckIfEnabledIsFalse() throws InternalUserNotFoundException, InternalUserValidationException {
        User user = createValidTestUser();
        UserDTO userDTO = createValidTestUserDTO();

        user.setEnabled(true);
        userDTO.setEnabled(true);

        when(userRepository.findByUsername(anyString())).thenReturn(user);

        Assert.assertTrue(user.isEnabled());
        userService.disableUser(userDTO);
        Assert.assertFalse(user.isEnabled());
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
}
