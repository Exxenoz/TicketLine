package at.ac.tuwien.inso.sepm.ticketline.server.unittests.user;

import at.ac.tuwien.inso.sepm.ticketline.rest.exception.UserValidatorException;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.user.UserMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalForbiddenException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalUserNotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.InternalUserValidationException;
import at.ac.tuwien.inso.sepm.ticketline.server.service.UserService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("unit-test")
public class UserServiceTest {
    private static final String TEST_USERNAME = "test";
    private static final String TEST_PASSWORD = "test";

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DataSource dataSource;

    private JdbcUserDetailsManager mgr;
    private PasswordEncoder passwordEncoder;

    @Before
    public void setUp() {
        mgr = new JdbcUserDetailsManager();
        mgr.setDataSource(dataSource);
        passwordEncoder = new BCryptPasswordEncoder(10);
    }

    @After
    public void tearDown() {
    }

    private void setTestUserEnabled(boolean enabled) {
        var authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        var user = new org.springframework.security.core.userdetails.User(TEST_USERNAME, passwordEncoder.encode(TEST_PASSWORD),
            enabled, true, true, true, authorities);
        mgr.updateUser(user);
        userService.initiateSecurityUser(user);
    }

    @Test
    public void enableUserTest() throws InternalUserNotFoundException, InternalUserValidationException {
        setTestUserEnabled(false);

        var user = userService.findUserByName(TEST_USERNAME);
        Assert.assertFalse(user.isEnabled());
        userService.enableUser(user);
        user = userService.findUserByName(TEST_USERNAME);
        Assert.assertTrue(user.isEnabled());
    }

    @Test
    public void disableUserTest() throws InternalUserNotFoundException, InternalForbiddenException, InternalUserValidationException {
        setTestUserEnabled(true);
        var userDTO = userService.findUserByName(TEST_USERNAME);
        Assert.assertTrue(userDTO.isEnabled());
        userService.disableUser(userDTO);
        userDTO = userService.findUserByName(TEST_USERNAME);
        Assert.assertFalse(userDTO.isEnabled());
    }
}
