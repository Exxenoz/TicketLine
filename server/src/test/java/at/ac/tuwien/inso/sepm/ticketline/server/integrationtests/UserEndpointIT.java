package at.ac.tuwien.inso.sepm.ticketline.server.integrationtests;


import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationRequest;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserCreateRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserPasswordChangeRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.UserPasswordResetRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.*;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtests.base.BaseIT;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;

public class UserEndpointIT extends BaseIT {

    private static final String TEST_USERNAME = "test";
    private static final String TEST_FALSE_PASSWORD = "pass";
    private static final String AUTH_ENDPOINT = "/authentication";
    private static final String USER_ENDPOINT = "/users";
    private static final String FIND_ALL_PATH = "/all";
    private static final String ENABLE_USER_PATH = "/enable";
    private static final String DISABLE_USER_PATH = "/disable";
    private static final String CREATE_USER_PATH = "/create";
    private static final String RESET_PASSWORD_PATH = "/password/reset";
    private static final String CHANGE_PASSWORD_PATH = "/password/change";

    @MockBean
    private UserRepository userRepository;

    private static final long USER_ID = 1;
    private static final String USER_NAME = "test";
    private static final String USER_PASS = "test";
    private static final String USER_PASS2 = "test123";
    private static final boolean USER_ENABLED = true;
    private static final int USER_STRIKES = 3;
    private static final String USER_PASS_CHANGE_KEY_NONE = null;
    private static final String USER_PASS_CHANGE_KEY = "ABCDEFGH";
    private static final HashSet<String> USER_ROLES = new HashSet<>(Arrays.asList("USER"));
    private static final HashSet<News> USER_READ_NEWS = new HashSet<News>();

    @Override
    public void beforeBase() throws InternalPasswordResetException, InternalUserPasswordWrongException, InternalUserNotFoundException, InternalForbiddenException, InternalUserValidationException {
        BDDMockito.
            given(userRepository.findByUsername(BaseIT.USER_USERNAME)).
            willReturn(User.builder()
                .id(1L)
                .username(BaseIT.USER_USERNAME)
                .password(new BCryptPasswordEncoder(10).encode(BaseIT.USER_PASSWORD))
                .enabled(true)
                .strikes(0)
                .passwordChangeKey(null)
                .roles(new HashSet<>(Arrays.asList("USER")))
                .readNews(new HashSet<>())
                .build());
        BDDMockito.
            given(userRepository.findByUsername(BaseIT.ADMIN_USERNAME)).
            willReturn(User.builder()
                .id(2L)
                .username(BaseIT.ADMIN_USERNAME)
                .password(new BCryptPasswordEncoder(10).encode(BaseIT.ADMIN_PASSWORD))
                .enabled(true)
                .strikes(0)
                .passwordChangeKey(null)
                .roles(new HashSet<>(Arrays.asList("USER", "ADMIN")))
                .readNews(new HashSet<>())
                .build());

        super.beforeBase();
    }

    private User buildValidUser() {
        return User.builder()
            .id(USER_ID)
            .username(USER_NAME)
            .password(new BCryptPasswordEncoder(10).encode(USER_PASS))
            .enabled(USER_ENABLED)
            .strikes(USER_STRIKES)
            .passwordChangeKey(USER_PASS_CHANGE_KEY_NONE)
            .roles(USER_ROLES)
            .readNews(USER_READ_NEWS)
            .build();
    }

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

    private UserPasswordResetRequestDTO buildValidUserPasswordResetRequestDTO() {
        return UserPasswordResetRequestDTO.builder()
            .userDTO(buildValidUserDTO())
            .passwordChangeKey(USER_PASS_CHANGE_KEY)
            .build();
    }

    private UserPasswordChangeRequestDTO buildValidUserPasswordChangeRequestDTO() {
        return UserPasswordChangeRequestDTO.builder()
            .passwordChangeKey(USER_PASS_CHANGE_KEY)
            .username(USER_NAME)
            .password(USER_PASS2)
            .build();
    }

    @Test
    public void tryToAuthenticateWithBadCredentials() {
        User user = User.builder()
            .id(1L)
            .username(TEST_USERNAME)
            .password(new BCryptPasswordEncoder(10).encode(TEST_FALSE_PASSWORD + "ABC"))
            .enabled(true)
            .strikes(0)
            .passwordChangeKey(null)
            .roles(new HashSet<>(Arrays.asList("USER")))
            .readNews(new HashSet<>())
            .build();

        BDDMockito.
            given(userRepository.findByUsername(TEST_USERNAME)).
            willReturn(user);

        var authenticationRequest = AuthenticationRequest.builder()
            .username(TEST_USERNAME)
            .password(TEST_FALSE_PASSWORD)
            .build();

        //try to login with bad credentials 5 times so that the user is disabled
        for (int i = 0; i <= 4; i++) {
            var response = RestAssured
                .given()
                .contentType(io.restassured.http.ContentType.JSON)
                .body(authenticationRequest)
                .when().post(AUTH_ENDPOINT)
                .then().extract().response();
            Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
        }
        //try a last time with the user disabled
        var response = RestAssured
            .given()
            .contentType(io.restassured.http.ContentType.JSON)
            .body(authenticationRequest)
            .when().post(AUTH_ENDPOINT)
            .then().extract().response();
        Assert.assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode());
    }

    @Test
    public void findAllUsersUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(USER_ENDPOINT + FIND_ALL_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findAllUsersAuthorizedAsAdmin() {
        BDDMockito.
            given(userRepository.findAll()).
            willReturn(Collections.singletonList(
                User.builder()
                    .id(USER_ID)
                    .username(USER_NAME)
                    .password(new BCryptPasswordEncoder(10).encode(USER_PASS))
                    .enabled(USER_ENABLED)
                    .strikes(USER_STRIKES)
                    .passwordChangeKey(USER_PASS_CHANGE_KEY_NONE)
                    .roles(USER_ROLES)
                    .readNews(USER_READ_NEWS)
                    .build()));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .when().get(USER_ENDPOINT + FIND_ALL_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(Arrays.asList(response.as(UserDTO[].class)), is(Collections.singletonList(
            UserDTO.builder()
                .id(USER_ID)
                .username(USER_NAME)
                .enabled(USER_ENABLED)
                .strikes(USER_STRIKES)
                .roles(USER_ROLES)
                .build())));
    }

    @Test
    public void enableDisabledUserUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(buildValidUserDTO())
            .when().post(USER_ENDPOINT + ENABLE_USER_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void enableDisabledUserAuthorizedAsUser() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(buildValidUserDTO())
            .when().post(USER_ENDPOINT + ENABLE_USER_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void enableDisabledUserAuthorizedAsAdmin() {
        User user = buildValidUser();
        user.setEnabled(false);

        BDDMockito.
            given(userRepository.findByUsername(USER_NAME)).
            willReturn(user);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(buildValidUserDTO())
            .when().post(USER_ENDPOINT + ENABLE_USER_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        Assert.assertTrue(user.isEnabled());
    }

    @Test
    public void disableEnabledUserUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(buildValidUserDTO())
            .when().post(USER_ENDPOINT + DISABLE_USER_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void disableEnabledUserAuthorizedAsUser() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(buildValidUserDTO())
            .when().post(USER_ENDPOINT + DISABLE_USER_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void disableEnabledUserAuthorizedAsAdmin() {
        User user = buildValidUser();
        user.setEnabled(true);

        BDDMockito.
            given(userRepository.findByUsername(USER_NAME)).
            willReturn(user);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(buildValidUserDTO())
            .when().post(USER_ENDPOINT + DISABLE_USER_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        Assert.assertTrue(!user.isEnabled());
    }

    @Test
    public void disableEnabledSelfAuthorizedAsAdmin() {
        User user = buildValidUser();
        user.setUsername("admin");
        user.setEnabled(true);

        UserDTO userDTO = buildValidUserDTO();
        userDTO.setUsername("admin");

        BDDMockito.
            given(userRepository.findByUsername("admin")).
            willReturn(user);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(userDTO)
            .when().post(USER_ENDPOINT + DISABLE_USER_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));

        Assert.assertTrue(user.isEnabled());
    }

    @Test
    public void createUserUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(buildValidUserCreateRequestDTO())
            .when().post(USER_ENDPOINT + CREATE_USER_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void createUserAuthorizedAsUser() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(buildValidUserCreateRequestDTO())
            .when().post(USER_ENDPOINT + CREATE_USER_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void createUserAuthorizedAsAdmin() {
        UserCreateRequestDTO userCreateRequestDTO = buildValidUserCreateRequestDTO();

        BDDMockito.
            given(userRepository.findByUsername(anyString())).
            willReturn(null);
        BDDMockito.
            given(userRepository.save(any())).
            willReturn(buildValidUser());

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(userCreateRequestDTO)
            .when().post(USER_ENDPOINT + CREATE_USER_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        Assert.assertThat(response.as(UserDTO.class), is(buildValidUserDTO()));
    }

    @Test
    public void createUserWithInvalidUsernameAuthorizedAsAdmin() {
        UserCreateRequestDTO userCreateRequestDTO = buildValidUserCreateRequestDTO();
        userCreateRequestDTO.setUsername("");

        BDDMockito.
            given(userRepository.findByUsername(anyString())).
            willReturn(null);
        BDDMockito.
            given(userRepository.save(any())).
            willReturn(buildValidUser());

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(userCreateRequestDTO)
            .when().post(USER_ENDPOINT + CREATE_USER_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void createUserWithConflictingUsernameAuthorizedAsAdmin() {
        UserCreateRequestDTO userCreateRequestDTO = buildValidUserCreateRequestDTO();

        BDDMockito.
            given(userRepository.findByUsername(anyString())).
            willReturn(buildValidUser());
        BDDMockito.
            given(userRepository.save(any())).
            willReturn(buildValidUser());

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(userCreateRequestDTO)
            .when().post(USER_ENDPOINT + CREATE_USER_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.CONFLICT.value()));
    }

    @Test
    public void resetPasswordUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(buildValidUserPasswordResetRequestDTO())
            .when().post(USER_ENDPOINT + RESET_PASSWORD_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void resetPasswordAuthorizedAsUser() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(buildValidUserPasswordResetRequestDTO())
            .when().post(USER_ENDPOINT + RESET_PASSWORD_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void resetAndChangePasswordWithValidPasswordChangeKey() {
        User user = buildValidUser();
        user.setEnabled(false);
        user.setStrikes(3);

        BDDMockito.
            given(userRepository.findByUsername(anyString())).
            willReturn(user);
        BDDMockito.
            given(userRepository.save(any())).
            willReturn(user);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(buildValidUserPasswordResetRequestDTO())
            .when().post(USER_ENDPOINT + RESET_PASSWORD_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        Assert.assertTrue(user.isEnabled());
        Assert.assertTrue(user.getStrikes() == 0);
        Assert.assertTrue(new BCryptPasswordEncoder(10).matches(USER_PASS_CHANGE_KEY, user.getPasswordChangeKey()));

        response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(buildValidUserPasswordChangeRequestDTO())
            .when().post(USER_ENDPOINT + CHANGE_PASSWORD_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        Assert.assertTrue(user.getPasswordChangeKey() == null);
        Assert.assertTrue(new BCryptPasswordEncoder(10).matches(USER_PASS2, user.getPassword()));
    }

    @Test
    public void resetAndChangePasswordWithInvalidPasswordChangeKey() {
        User user = buildValidUser();
        user.setEnabled(false);
        user.setStrikes(3);

        BDDMockito.
            given(userRepository.findByUsername(anyString())).
            willReturn(user);
        BDDMockito.
            given(userRepository.save(any())).
            willReturn(user);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(buildValidUserPasswordResetRequestDTO())
            .when().post(USER_ENDPOINT + RESET_PASSWORD_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        Assert.assertTrue(user.isEnabled());
        Assert.assertTrue(user.getStrikes() == 0);
        Assert.assertTrue(new BCryptPasswordEncoder(10).matches(USER_PASS_CHANGE_KEY, user.getPasswordChangeKey()));

        UserPasswordChangeRequestDTO userPasswordChangeRequestDTO = buildValidUserPasswordChangeRequestDTO();
        userPasswordChangeRequestDTO.setPasswordChangeKey("Something invalid");

        response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(userPasswordChangeRequestDTO)
            .when().post(USER_ENDPOINT + CHANGE_PASSWORD_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST.value()));
    }
}
