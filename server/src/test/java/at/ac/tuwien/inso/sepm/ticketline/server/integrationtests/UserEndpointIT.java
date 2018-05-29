package at.ac.tuwien.inso.sepm.ticketline.server.integrationtests;


import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationRequest;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtests.base.BaseIT;
import io.restassured.RestAssured;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
public class UserEndpointIT extends BaseIT {

    private static final String TEST_USERNAME = "test";
    private static final String TEST_FALSE_PASSWORD = "pass";
    private static final String USER_ENDPOINT = "/authentication";

    @Test
    public void tryToAuthenticateWithBadCredentials() {
        var authenticationRequest = AuthenticationRequest.builder()
            .username(TEST_USERNAME)
            .password(TEST_FALSE_PASSWORD)
            .build();

        //try to login with bad credentials 5 times so that the user is disabled
        for (int i = 0; i < 4; i++) {
            var response = RestAssured
                .given()
                .contentType(io.restassured.http.ContentType.JSON)
                .body(authenticationRequest)
                .when().post(USER_ENDPOINT)
                .then().extract().response();
            Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
        }
        //try a last time with the user disabled
        var response = RestAssured
            .given()
            .contentType(io.restassured.http.ContentType.JSON)
            .body(authenticationRequest)
            .when().post(USER_ENDPOINT)
            .then().extract().response();
        Assert.assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode());
    }
}
