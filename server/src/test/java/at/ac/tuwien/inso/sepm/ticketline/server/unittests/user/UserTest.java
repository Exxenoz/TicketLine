package at.ac.tuwien.inso.sepm.ticketline.server.unittests.user;

import at.ac.tuwien.inso.sepm.ticketline.rest.exception.UserValidatorException;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.service.UserService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.web.server.LocalManagementPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
public class UserTest {
    private static final String TEST_USERNAME = "test";
    private static final String TEST_PASSWORD = "test";

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @LocalServerPort
    int randomServerPort;

    @LocalManagementPort
    int randomManagementPort;

    @Value("${server.servlet.context-path:/}")
    private String contextPath;


    @Autowired
    private UserService userService;

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
    public void enableUserTest() throws UserValidatorException {
        setTestUserEnabled(false);

        var user = userService.findUserByName(TEST_USERNAME);
        Assert.assertFalse(user.isEnabled());
        userService.enableUser(user);
        user = userService.findUserByName(TEST_USERNAME);
        Assert.assertTrue(user.isEnabled());
    }

    @Test
    public void disableUserTest() {
        setTestUserEnabled(true);

        var user = userService.findUserByName(TEST_USERNAME);
        Assert.assertTrue(user.isEnabled());
        userService.disableUser(user);
        user = userService.findUserByName(TEST_USERNAME);
        Assert.assertFalse(user.isEnabled());
    }

    @Test
    public void tryToAuthenticateWithBadCredentials() throws IOException {
        setTestUserEnabled(true);

        String payload = "{" +
            "\"username\": \"" + TEST_USERNAME + "\", " +
            "\"password\": \"pass\"" +
            "}";
        StringEntity entity = new StringEntity(payload,
            ContentType.APPLICATION_JSON);

        HttpPost request = new HttpPost("http://localhost:" + randomServerPort + "/authentication");
        HttpClient httpClient;
        //try to login with bad credentials 5 times so that the user is disabled
        for (int i = 0; i < 4; i++) {
            httpClient = HttpClientBuilder.create().build();
            request.setEntity(entity);

            HttpResponse response = httpClient.execute(request);
            Assert.assertEquals(401, response.getStatusLine().getStatusCode());
        }
        httpClient = HttpClientBuilder.create().build();
        request.setEntity(entity);

        HttpResponse response = httpClient.execute(request);
        Assert.assertEquals(403, response.getStatusLine().getStatusCode());

        User user = userService.findUserByName(TEST_USERNAME);
        Assert.assertFalse(user.isEnabled());
    }
}
