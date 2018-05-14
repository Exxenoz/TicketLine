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
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

@Profile("generateData")
@Component
@Order(10)
public class PerformanceDataGenerator implements DataGenerator {

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

    @Override
    public void generate() {
        //performanceRepository.deleteAll();
        if (performanceRepository.count() > 0) {
            LOGGER.info("performances already generated");
        } else {
            LOGGER.info("generating {} performance entries", NUMBER_OF_PERFORMANCES_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_PERFORMANCES_TO_GENERATE; i++) {
                final var event = eventRepository.save(new Event(Collections.emptySet(), faker.pokemon().name(), EventType.SEAT, "Event description"));


                final var address = new Address(faker.lorem().characters(3, 25), faker.lorem().characters(3, 25), faker.lorem().characters(3, 25), faker.lorem().characters(3, 25), faker.lorem().characters(4, 5));
                BigDecimal price = new BigDecimal(2.2);
                final var performance = new Performance(event, faker.lorem().characters(3, 10), price, LocalDateTime.now(), LocalDateTime.now(), address);

                LOGGER.debug("saving performance {}", performance);

                performanceRepository.save(performance);
            }
        }
    }
}
