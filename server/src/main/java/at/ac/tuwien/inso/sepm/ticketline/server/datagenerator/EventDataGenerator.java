package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.EventType;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ArtistRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Profile("generateData")
@Component
public class EventDataGenerator implements DataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_EVENTS_TO_GENERATE = 25;
    private final EventRepository eventRepository;
    private final ArtistRepository artistRepository;
    private final Faker faker;

    public EventDataGenerator(EventRepository eventRepository, ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
        this.eventRepository = eventRepository;
        faker = new Faker();
    }


    @Override
    public void generate() {
        if (eventRepository.count() > 0) {
            LOGGER.info("events already generated");
        } else {
            LOGGER.info("generating {} performance entries", NUMBER_OF_EVENTS_TO_GENERATE);

            //seats
            List<Artist> artists = new ArrayList<Artist>(artistRepository.findAll());
            for (int i = 0; i < NUMBER_OF_EVENTS_TO_GENERATE - 3; i++) {
                Set<Artist> artistSet = new HashSet<Artist>();
                artistSet.add(artists.get(i));
                artistSet.add(artists.get(i + 1));

                Event event = new Event(artistSet, faker.pokemon().name(), EventType.SEAT, faker.rickAndMorty().quote());
                eventRepository.save(event);
            }

            //sectors
            for (int i = 0; i < 3; i++) {
                Set<Artist> artistSet = new HashSet<>();
                artistSet.add(artists.get(i));
                artistSet.add(artists.get(i + 1));
                Event event = new Event(artistSet, faker.pokemon().name(), EventType.SECTOR, faker.rickAndMorty().quote());

                eventRepository.save(event);
            }
        }

    }
}
