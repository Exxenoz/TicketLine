package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Address;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.EventType;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Performance;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.PerformanceRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;

@Profile("generateData")
@Component
public class PerformanceDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_PERFORMANCES_TO_GENERATE = 25;

    private final PerformanceRepository performanceRepository;
    private final EventRepository eventRepository;
    private final Faker faker;

    public PerformanceDataGenerator(PerformanceRepository performanceRepository, EventRepository eventRepository) {
        this.performanceRepository = performanceRepository;
        this.eventRepository = eventRepository;
        faker = new Faker();
    }

    @PostConstruct
    private void generatePerformance() {
        if (performanceRepository.count() > 0) {
            LOGGER.info("performances already generated");
        } else {
            LOGGER.info("generating {} performance entries", NUMBER_OF_PERFORMANCES_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_PERFORMANCES_TO_GENERATE; i++) {
                final var event = eventRepository.save(new Event(Collections.emptySet(), "Event Name", EventType.SEAT, "Event description"));

                final var address = new Address(faker.lorem().characters(3, 25), faker.lorem().characters(3, 25), faker.lorem().characters(3, 25), faker.lorem().characters(3, 25), faker.lorem().characters(4, 5));
                final var performance = new Performance(event, "Perf", 12.5d, LocalDateTime.now(), LocalDateTime.now(), address);

                LOGGER.debug("saving performance {}", performance);

                performanceRepository.save(performance);
            }
        }
    }

}
