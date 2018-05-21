package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Address;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ArtistRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private final Faker faker;

    public PerformanceDataGenerator(PerformanceRepository performanceRepository, EventRepository eventRepository, ArtistRepository artistRepository) {
        this.performanceRepository = performanceRepository;
        this.eventRepository = eventRepository;
        this.artistRepository = artistRepository;
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

                final var address = new Address(faker.lordOfTheRings().location(), faker.address().streetName(), faker.address().city(), faker.address().country(), faker.address().zipCode());
                BigDecimal price = new BigDecimal(2.2);
                LocalDateTime startTime = LocalDateTime.ofInstant(faker.date().between(Date.from(now().toInstant(ZoneOffset.UTC)), Date.from(now().plusYears(5).toInstant(ZoneOffset.UTC))).toInstant(), ZoneId.of("UTC"));

                final var performance = new Performance(
                    events.get(faker.number().numberBetween(0, events.size() - 1)),
                    artistSet,
                    faker.esports().event(),
                    price,
                    startTime,
                    startTime.plusMinutes(faker.number().numberBetween(30, 4 * 60)),
                    address);

                LOGGER.debug("saving performance {}", performance);

                performanceRepository.save(performance);
            }
        }
    }
}
