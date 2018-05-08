package at.ac.tuwien.inso.sepm.ticketline.server.user;

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
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserTest {
    private static final String TEST_USERNAME = "test";
    private static final String TEST_PASSWORD = "test";

    @Autowired
    private UserService userService;

    @Autowired
    private DataSource dataSource;

    private JdbcUserDetailsManager mgr;

    @Before
    public void setUp() {
        mgr = new JdbcUserDetailsManager();
        mgr.setDataSource(dataSource);

    }

    @After
    public void tearDown() {
    }

    private void createTestUser(boolean enabled) {
        var authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("USER"));

        if (!mgr.userExists(TEST_USERNAME)) {

            var user = new org.springframework.security.core.userdetails.User(TEST_USERNAME, TEST_PASSWORD,
                enabled, true, true, true, authorities);
            mgr.createUser(user);
            //Now add aditional information for this users
            userService.initiateSecurityUser(user);

        } else {
            var user = new org.springframework.security.core.userdetails.User(TEST_USERNAME, TEST_PASSWORD,
                enabled, true, true, true, authorities);
            mgr.updateUser(user);
            //Now add aditional information for this users
            userService.initiateSecurityUser(user);
        }
    }

    @Test
    public void enableUserTest() {
        createTestUser(false);

        var user = userService.findUserByName(TEST_USERNAME);
        Assert.assertFalse(user.isEnabled());
        userService.enableUser(user);
        user = userService.findUserByName(TEST_USERNAME);
        Assert.assertTrue(user.isEnabled());
    }

    @Test
    public void disableUserTest() {
        createTestUser(true);

        var user = userService.findUserByName(TEST_USERNAME);
        Assert.assertTrue(user.isEnabled());
        userService.disableUser(user);
        user = userService.findUserByName(TEST_USERNAME);
        Assert.assertFalse(user.isEnabled());
    }

}
