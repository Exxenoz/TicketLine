package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ArtistRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;

@Profile("generateData")
@Component
@Order(5)
public class ArtistDataGenerator implements DataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_ARTISTS_TO_GENERATE = 25;
    private final ArtistRepository artistRepository;
    private Faker faker;

    public ArtistDataGenerator(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
        faker = new Faker();
    }

    @PostConstruct
    @Override
    public void generate() {
        if (artistRepository.count() > 0) {
            LOGGER.info("artists already generated");
        } else {
            LOGGER.info("generating {} artist entries", NUMBER_OF_ARTISTS_TO_GENERATE);

            for (int i = 0; i < NUMBER_OF_ARTISTS_TO_GENERATE; i++) {
                artistRepository.save(new Artist(faker.rockBand().name(), String.valueOf(i)));
            }
        }
    }
}
