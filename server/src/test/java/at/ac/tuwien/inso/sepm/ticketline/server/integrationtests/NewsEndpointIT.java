package at.ac.tuwien.inso.sepm.ticketline.server.integrationtests;

import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.service.*;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtests.base.BaseIT;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.NewsRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;

public class NewsEndpointIT extends BaseIT {

    private static final String NEWS_ENDPOINT = "/news";
    private static final String NEWS_FIND_ALL_PARAMS = "?read={read}&page=0&size=1";
    private static final String SPECIFIC_NEWS_PATH = "/{newsId}";

    private static final String TEST_NEWS_TEXT = "<html><head></head><body contenteditable=\"true\"><p>Debitis amet dolorem saepe consequatur fuga in.</p></body></html>";
    private static final String TEST_NEWS_TITLE = "title";
    private static final String TEST_NEWS_SUMMARY = "This is a news test summary!";
    private static final LocalDateTime TEST_NEWS_PUBLISHED_AT =
        LocalDateTime.of(2016, 11, 13, 12, 15, 0, 0);
    private static final long TEST_NEWS_ID = 1L;

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

    @MockBean
    private NewsRepository newsRepository;

    @MockBean
    private UserRepository userRepository;

    @Override
    public void beforeBase() throws InternalPasswordResetException, InternalUserPasswordWrongException, InternalUserNotFoundException, InternalForbiddenException, InternalUserValidationException {
        BDDMockito.
            given(userRepository.findByUsername("user")).
            willReturn(User.builder()
                .id(1L)
                .username("user")
                .password(new BCryptPasswordEncoder(10).encode("password"))
                .enabled(true)
                .strikes(0)
                .passwordChangeKey(null)
                .roles(new HashSet<>(Arrays.asList("USER")))
                .readNews(new HashSet<>())
                .build());
        BDDMockito.
            given(userRepository.findByUsername("admin")).
            willReturn(User.builder()
                .id(2L)
                .username("admin")
                .password(new BCryptPasswordEncoder(10).encode("password"))
                .enabled(true)
                .strikes(0)
                .passwordChangeKey(null)
                .roles(new HashSet<>(Arrays.asList("USER", "ADMIN")))
                .readNews(new HashSet<>())
                .build());

        super.beforeBase();
    }

    @Test
    public void findAllUnreadNewsUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(NEWS_ENDPOINT + NEWS_FIND_ALL_PARAMS, false)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findAllReadNewsUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(NEWS_ENDPOINT + NEWS_FIND_ALL_PARAMS, true)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findAllUnreadNewsAsUser() {
        BDDMockito.
            given(newsRepository.findAllUnreadByUsername(anyString(), any())).
            willReturn(new PageImpl<>(Arrays.asList(
                News.builder()
                    .id(TEST_NEWS_ID)
                    .title(TEST_NEWS_TITLE)
                    .text(TEST_NEWS_TEXT)
                    .summary(TEST_NEWS_SUMMARY)
                    .publishedAt(TEST_NEWS_PUBLISHED_AT)
                    .build())));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(NEWS_ENDPOINT + NEWS_FIND_ALL_PARAMS, false)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        PageResponseDTO<SimpleNewsDTO> pageResponseDTO = response.as(new PageResponseDTO<SimpleNewsDTO>().getClass());
        Assert.assertTrue(pageResponseDTO.getContent().size() == 1);

        LinkedHashMap<String, String> content = (LinkedHashMap<String, String>)(Object)pageResponseDTO.getContent().get(0);
        Assert.assertTrue(content.get("title").equals(TEST_NEWS_TITLE));
        Assert.assertTrue(content.get("summary").equals(TEST_NEWS_SUMMARY));
    }

    @Test
    public void findAllReadNewsAsUser() {
        BDDMockito.
            given(newsRepository.findAllReadByUsername(anyString(), any())).
            willReturn(new PageImpl<>(Arrays.asList(
                News.builder()
                    .id(TEST_NEWS_ID)
                    .title(TEST_NEWS_TITLE)
                    .text(TEST_NEWS_TEXT)
                    .summary(TEST_NEWS_SUMMARY)
                    .publishedAt(TEST_NEWS_PUBLISHED_AT)
                    .build())));
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(NEWS_ENDPOINT + NEWS_FIND_ALL_PARAMS, true)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        PageResponseDTO<SimpleNewsDTO> pageResponseDTO = response.as(new PageResponseDTO<SimpleNewsDTO>().getClass());
        Assert.assertTrue(pageResponseDTO.getContent().size() == 1);

        LinkedHashMap<String, String> content = (LinkedHashMap<String, String>)(Object)pageResponseDTO.getContent().get(0);
        Assert.assertTrue(content.get("title").equals(TEST_NEWS_TITLE));
        Assert.assertTrue(content.get("summary").equals(TEST_NEWS_SUMMARY));
    }

    @Test
    public void findSpecificNewsUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .when().get(NEWS_ENDPOINT + SPECIFIC_NEWS_PATH, TEST_NEWS_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void findSpecificNewsAsUser() {
        Optional<News> news = Optional.of(News.builder()
            .id(TEST_NEWS_ID)
            .title(TEST_NEWS_TITLE)
            .text(TEST_NEWS_TEXT)
            .summary(TEST_NEWS_SUMMARY)
            .publishedAt(TEST_NEWS_PUBLISHED_AT)
            .build());

        BDDMockito.
            given(newsRepository.findOneById(TEST_NEWS_ID)).
            willReturn(news);

        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(NEWS_ENDPOINT + SPECIFIC_NEWS_PATH, TEST_NEWS_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(DetailedNewsDTO.class), is(DetailedNewsDTO.builder()
            .id(TEST_NEWS_ID)
            .title(TEST_NEWS_TITLE)
            .text(TEST_NEWS_TEXT)
            .summary(TEST_NEWS_SUMMARY)
            .publishedAt(TEST_NEWS_PUBLISHED_AT)
            .build()));
    }

    @Test
    public void findSpecificNonExistingNewsNotFoundAsUser() {
        BDDMockito.
            given(newsRepository.findOneById(TEST_NEWS_ID)).
            willReturn(Optional.empty());
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .when().get(NEWS_ENDPOINT + SPECIFIC_NEWS_PATH, TEST_NEWS_ID)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void publishNewsUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(DetailedNewsDTO.builder()
                .id(TEST_NEWS_ID)
                .title(TEST_NEWS_TITLE)
                .text(TEST_NEWS_TEXT)
                .publishedAt(TEST_NEWS_PUBLISHED_AT)
                .build())
            .when().post(NEWS_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void publishNewsUnauthorizedAsUser() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(DetailedNewsDTO.builder()
                .id(TEST_NEWS_ID)
                .title(TEST_NEWS_TITLE)
                .text(TEST_NEWS_TEXT)
                .summary(TEST_NEWS_SUMMARY)
                .publishedAt(TEST_NEWS_PUBLISHED_AT)
                .build())
            .when().post(NEWS_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void publishNewsAsAdmin() {
        BDDMockito.
            given(newsRepository.save(any(News.class))).
            willReturn(News.builder()
                .id(TEST_NEWS_ID)
                .title(TEST_NEWS_TITLE)
                .text(TEST_NEWS_TEXT)
                .summary(TEST_NEWS_SUMMARY)
                .publishedAt(TEST_NEWS_PUBLISHED_AT)
                .build());
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validAdminTokenWithPrefix)
            .body(DetailedNewsDTO.builder()
                .title(TEST_NEWS_TITLE)
                .text(TEST_NEWS_TEXT)
                .summary(TEST_NEWS_SUMMARY)
                .build())
            .when().post(NEWS_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(SimpleNewsDTO.class), is(SimpleNewsDTO.builder()
            .id(TEST_NEWS_ID)
            .title(TEST_NEWS_TITLE)
            .summary(TEST_NEWS_SUMMARY)
            .publishedAt(TEST_NEWS_PUBLISHED_AT)
            .build()));
    }
}
