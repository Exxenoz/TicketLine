package at.ac.tuwien.inso.sepm.ticketline.server.integrationtests;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.web.server.LocalManagementPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
public class UserEndpointIT {

    private static final String TEST_USERNAME = "test";

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @LocalServerPort
    int randomServerPort;
    @LocalManagementPort
    int randomManagementPort;
    @Value("${server.servlet.context-path:/}")
    private String contextPath;

    @Test
    public void tryToAuthenticateWithBadCredentials() throws IOException {
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
            Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusLine().getStatusCode());
        }
        httpClient = HttpClientBuilder.create().build();
        request.setEntity(entity);

        HttpResponse response = httpClient.execute(request);
        Assert.assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusLine().getStatusCode());
    }
}
