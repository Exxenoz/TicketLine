package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.NewsRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

@Profile("generateData")
@Component
public class NewsDataGenerator implements DataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_NEWS_TO_GENERATE = 150;
    private final NewsRepository newsRepository;
    private final Faker faker;

    public NewsDataGenerator(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
        faker = new Faker();
    }

    @Override
    public void generate() {
        if (newsRepository.count() > 0) {
            LOGGER.info("News already generated");
        } else {
            LOGGER.info("Generating {} news entries", NUMBER_OF_NEWS_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_NEWS_TO_GENERATE; i++) {
                final var news = News.builder()
                    .title(faker.lorem().word())
                    .summary(faker.lorem().word() + " " + faker.lorem().word() + " " + faker.lorem().word())
                    .text("<html><head></head><body contenteditable=\"true\"><p>" + faker.lorem().paragraph(faker.number().numberBetween(5, 10)) + "</p></body></html>")
                    .publishedAt(
                        LocalDateTime.ofInstant(
                            faker.date()
                                .past(365 * 3, TimeUnit.DAYS).
                                toInstant(),
                            ZoneId.systemDefault()
                        ))
                    .build();
                LOGGER.debug("Saving news {}", news);
                newsRepository.save(news);
            }
        }
    }

}
