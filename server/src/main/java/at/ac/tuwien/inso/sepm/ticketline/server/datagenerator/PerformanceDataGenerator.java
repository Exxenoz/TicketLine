package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ArtistRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.HallRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

import static java.time.LocalDateTime.now;

@Profile("generateData")
@Component
@Order(10)
public class PerformanceDataGenerator implements DataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_PERFORMANCES_TO_GENERATE = 25;
    private final ArtistRepository artistRepository;
    private final PerformanceRepository performanceRepository;
    private final EventRepository eventRepository;
    private final HallRepository hallRepository;
    private final Faker faker;

    public PerformanceDataGenerator(PerformanceRepository performanceRepository, EventRepository eventRepository,
                                    ArtistRepository artistRepository, HallRepository hallRepository) {
        this.performanceRepository = performanceRepository;
        this.eventRepository = eventRepository;
        this.artistRepository = artistRepository;
        this.hallRepository = hallRepository;
        faker = new Faker();
    }

    @Override
    public void generate() {
        if (performanceRepository.count() > 0) {
            LOGGER.info("performances already generated");
        } else {
            LOGGER.info("generating {} performance entries", NUMBER_OF_PERFORMANCES_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_PERFORMANCES_TO_GENERATE; i++) {

                final List<Event> events = eventRepository.findAll();

                List<Artist> artists = new ArrayList<Artist>(artistRepository.findAll());
                Set<Artist> artistSet = new HashSet<Artist>();
                int artistOffset = faker.number().numberBetween(0, artists.size() - 2);
                artistSet.add(artists.get(artistOffset));
                artistSet.add(artists.get(artistOffset + 1));

                final var address = new LocationAddress(faker.lordOfTheRings().location(), faker.address().streetName(), faker.address().city(), faker.address().country(), faker.address().zipCode());

                BigDecimal price = new BigDecimal(2.2);
                LocalDateTime startTime = LocalDateTime.ofInstant(faker.date().between(Date.from(now().toInstant(ZoneOffset.UTC)), Date.from(now().plusYears(5).toInstant(ZoneOffset.UTC))).toInstant(), ZoneId.of("UTC"));

                final var performance = new Performance(
                    events.get(faker.number().numberBetween(0, events.size() - 1)),
                    artistSet,
                    faker.esports().event(),
                    price,
                    startTime,
                    Duration.ofMinutes(30),
                    address);

                // Simply adding a random hall
                if(hallRepository.findAll().size() > 0) {
                    List<Hall> halls = hallRepository.findAll();
                    performance.setHall(halls.get(new Random().nextInt(halls.size())));
                } else {
                    LOGGER.debug("Could not save hall for performance, since halls are not generated.");
                }

                LOGGER.debug("Saving performance {}", performance);
                performanceRepository.save(performance);
            }
        }
    }
}
