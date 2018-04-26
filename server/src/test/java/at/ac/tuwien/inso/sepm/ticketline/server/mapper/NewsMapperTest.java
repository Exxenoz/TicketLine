package at.ac.tuwien.inso.sepm.ticketline.server.mapper;

import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.news.NewsMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class NewsMapperTest {

    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper")
    public static class NewsMapperTestContextConfiguration {
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private NewsMapper newsMapper;

    private static final long NEWS_ID = 1L;
    private static final String NEWS_TITLE = "Headline";
    private static final String NEWS_TEXT = "This is a very long text containing all the contents of the news" +
        " and a lot of other more or less useful information.";
    private static final String NEWS_SUMMARY = "This is a very long text containing all the";
    private static final LocalDateTime NEWS_PUBLISHED_AT =
        LocalDateTime.of(2016, 1, 1, 12, 0, 0, 0);

    @Test
    public void shouldMapNewsToSimpleNewsDTOShorteningTextToSummary() {
        News news = News.builder()
            .id(NEWS_ID)
            .publishedAt(NEWS_PUBLISHED_AT)
            .title(NEWS_TITLE)
            .text(NEWS_TEXT)
            .build();
        SimpleNewsDTO simpleNewsDTO = newsMapper.newsToSimpleNewsDTO(news);
        assertThat(simpleNewsDTO).isNotNull();
        assertThat(simpleNewsDTO.getId()).isEqualTo(1L);
        assertThat(simpleNewsDTO.getPublishedAt()).isEqualTo(NEWS_PUBLISHED_AT);
        assertThat(simpleNewsDTO.getTitle()).isEqualTo(NEWS_TITLE);
        assertThat(simpleNewsDTO.getSummary()).isEqualTo(NEWS_SUMMARY);
    }

    @Test
    public void shouldMapNewsToSimpleNewsDTONotShorteningTextToSummary() {
        News news = News.builder()
            .id(NEWS_ID)
            .publishedAt(NEWS_PUBLISHED_AT)
            .title(NEWS_TITLE)
            .text(NEWS_SUMMARY)
            .build();
        SimpleNewsDTO simpleNewsDTO = newsMapper.newsToSimpleNewsDTO(news);
        assertThat(simpleNewsDTO).isNotNull();
        assertThat(simpleNewsDTO.getId()).isEqualTo(1L);
        assertThat(simpleNewsDTO.getPublishedAt()).isEqualTo(NEWS_PUBLISHED_AT);
        assertThat(simpleNewsDTO.getTitle()).isEqualTo(NEWS_TITLE);
        assertThat(simpleNewsDTO.getSummary()).isEqualTo(NEWS_SUMMARY);
    }

    @Test
    public void shouldMapNewsToDetailedNewsDTO() {
        News news = News.builder()
            .id(NEWS_ID)
            .publishedAt(NEWS_PUBLISHED_AT)
            .title(NEWS_TITLE)
            .text(NEWS_TEXT)
            .build();
        DetailedNewsDTO detailedNewsDTO = newsMapper.newsToDetailedNewsDTO(news);
        assertThat(detailedNewsDTO).isNotNull();
        assertThat(detailedNewsDTO.getId()).isEqualTo(1L);
        assertThat(detailedNewsDTO.getPublishedAt()).isEqualTo(NEWS_PUBLISHED_AT);
        assertThat(detailedNewsDTO.getTitle()).isEqualTo(NEWS_TITLE);
        assertThat(detailedNewsDTO.getText()).isEqualTo(NEWS_TEXT);
    }

    @Test
    public void shouldMapDetailedNewsDTOToNews() {
        DetailedNewsDTO detailedNewsDTO = DetailedNewsDTO.builder()
            .id(1L)
            .publishedAt(NEWS_PUBLISHED_AT)
            .title(NEWS_TITLE)
            .text(NEWS_TEXT)
            .build();
        News news = newsMapper.detailedNewsDTOToNews(detailedNewsDTO);
        assertThat(news).isNotNull();
        assertThat(news.getId()).isEqualTo(1L);
        assertThat(news.getPublishedAt()).isEqualTo(NEWS_PUBLISHED_AT);
        assertThat(news.getTitle()).isEqualTo(NEWS_TITLE);
        assertThat(news.getText()).isEqualTo(NEWS_TEXT);
    }

}
