package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.EventType;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Profile("generateData")
@Component
@Order(7)
public class EventDataGenerator implements DataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_EVENTS_TO_GENERATE_SEATS = 12;
    private static final int NUMBER_OF_EVENTS_TO_GENERATE_SECTORS = 13;
    private final EventRepository eventRepository;

    private final Faker faker;

    public EventDataGenerator(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
        faker = new Faker();
    }

    @Override
    public void generate() {
        if (eventRepository.count() > 0) {
            LOGGER.info("events already generated");
        } else {
            LOGGER.info("generating {} performance entries", NUMBER_OF_EVENTS_TO_GENERATE_SEATS, NUMBER_OF_EVENTS_TO_GENERATE_SECTORS);

            for (int i = 0; i < NUMBER_OF_EVENTS_TO_GENERATE_SEATS; i++) {
                Event event = new Event(faker.pokemon().name(), EventType.SEAT, faker.harryPotter().quote());
                eventRepository.save(event);
            }

            for (int i = 0; i < NUMBER_OF_EVENTS_TO_GENERATE_SECTORS; i++) {
                Event event = new Event(faker.pokemon().name(), EventType.SECTOR, faker.harryPotter().quote());
                eventRepository.save(event);
            }
        }

    }
}
