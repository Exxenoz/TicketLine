package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator.legacy;

import at.ac.tuwien.inso.sepm.ticketline.server.datagenerator.DataGenerator;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Sector;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SeatRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SectorRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Profile("generateData")
@Component
@Order(2)
public class SeatDataGenerator implements DataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_SEATS_TO_GENERATE = 300;

    private final SeatRepository seatRepository;
    private final SectorRepository sectorRepository;
    private final Faker faker;

    public SeatDataGenerator(SeatRepository seatRepository, SectorRepository sectorRepository) {
        this.seatRepository = seatRepository;
        this.sectorRepository = sectorRepository;
        faker = new Faker();
    }

    @Override
    public void generate() {
        if (sectorRepository.count() == 0) {
            LOGGER.info("Could not generate seats, because there are no sectors!");
        }
        else if (seatRepository.count() > 0) {
            LOGGER.info("Seats already generated");
        } else {
            LOGGER.info("Generating {} seat entries", NUMBER_OF_SEATS_TO_GENERATE);
            List<Sector> sectors = sectorRepository.findAll();
            final int seatsPerSector = NUMBER_OF_SEATS_TO_GENERATE / sectors.size();

            for (int i = 0, x = 0, y = 0; i < NUMBER_OF_SEATS_TO_GENERATE; i++, x++) {
                final var seat = new Seat();
                seat.setPositionX(x);
                seat.setPositionY(y);
                seat.setSector(sectors.get(y));
                if ((i % seatsPerSector) == 0 && i > 0) {
                    x = 0;
                    y++;
                }
                LOGGER.debug("Saving seat {}", seat);
                seatRepository.save(seat);
            }
        }
    }
}
