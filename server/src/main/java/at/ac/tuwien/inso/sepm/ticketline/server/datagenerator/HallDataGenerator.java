package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.HallRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SeatRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SectorCategoryRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SectorRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Profile("generateData")
@Component
@Order(5)
public class HallDataGenerator implements DataGenerator {

    private final static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final static int NUMBER_OF_HALLS = 25;

    private final static int SECTOR_COUNT_MIN = 4;
    private final static int SECTOR_WIDTH_MIN = 5;
    private final static int SECTOR_HEIGHT_MIN = 3;
    private final static int SECTOR_COUNT_MAX = 8;
    private final static int SECTOR_WIDTH_MAX = 10;
    private final static int SECTOR_HEIGHT_MAX = 6;

    private final HallRepository hallRepository;
    private final SectorCategoryRepository sectorCategoryRepository;
    private final SectorRepository sectorRepository;
    private final SeatRepository seatRepository;

    private final Faker faker;

    public HallDataGenerator(HallRepository hallRepository, SectorCategoryRepository sectorCategoryRepository, SectorRepository sectorRepository,
                             SeatRepository seatRepository) {

        this.hallRepository = hallRepository;
        this.sectorCategoryRepository = sectorCategoryRepository;
        this.sectorRepository = sectorRepository;
        this.seatRepository = seatRepository;

        faker = new Faker();
    }

    @Override
    public void generate() {
        if (hallRepository.count() > 0) {
            LOGGER.info("Halls already generated.");
        } else {
            LOGGER.info("Generating {} hall entries", NUMBER_OF_HALLS);
            for (int i = 0; i < NUMBER_OF_HALLS; i++) {
                final var hall = new Hall();

                // Set random name
                hall.setName(String.format("The Hall of %s", faker.lordOfTheRings().character()));

                // ..and add random address
                LocationAddress address = new LocationAddress();
                address.setLocationName(faker.lordOfTheRings().location());
                address.setCity(faker.address().city());
                address.setStreet(faker.address().streetName());
                address.setCountry(faker.address().country());
                address.setPostalCode(faker.address().zipCode());
                hall.setAddress(address);

                // Now we want to generate the sectors and seats
                int sectorCount = faker.number().numberBetween(SECTOR_COUNT_MIN, SECTOR_COUNT_MAX);

                //we want to keep all sectors same sized for now
                int sectorSeatsPerRow = faker.number().numberBetween(SECTOR_WIDTH_MIN, SECTOR_WIDTH_MAX);
                int sectorRows = faker.number().numberBetween(SECTOR_HEIGHT_MIN, SECTOR_HEIGHT_MAX);

                int x = 0;
                int y = 0;

                List<Sector> sectors = new ArrayList<>();
                for(int j = 0; j < sectorCount; j++) {
                    Sector s = new Sector();
                    s.setSeatsPerRow(sectorSeatsPerRow);
                    s.setRows(sectorRows);
                    s.setStartPositionX(x);
                    s.setStartPositionY(y);

                    // And set a sector category
                    if(sectorCategoryRepository.count() == 0) {
                        LOGGER.debug("Could net set sector category since the corresponding repository is empty");
                        SectorCategory sectorCategory = new SectorCategory();
                        sectorCategory.setBasePriceMod(new Long(500L));
                        sectorCategory.setName(faker.hipster().word());
                        s.setCategory(sectorCategory);
                        sectorCategoryRepository.save(sectorCategory);
                    } else {
                        s.setCategory(sectorCategoryRepository.findAll().get(faker.number()
                            .numberBetween(0, Math.toIntExact(sectorCategoryRepository.count()) - 1)));
                    }

                    // Save sector without seats
                    s = sectorRepository.save(s);

                    List<Seat> seats = new ArrayList<>(5);
                    // Add a few random seats
                    for(int k = 0; k < 5; k++) {
                        Seat seat = new Seat();

                        // Initialize position of seat in sector
                        int nextX = faker.number().numberBetween(0, sectorSeatsPerRow);
                        int nextY = faker.number().numberBetween(0, sectorRows);
                        seat.setPositionX(nextX);
                        seat.setPositionY(nextY);
//                        seat.setSector(s);

                        //Check if such a seat already exists, and just dont store it for now if it does.
                        for(Seat t: seats) {
                            if(t.getPositionX() == nextX && t.getPositionY() == nextY) {
                                seat = null;
                            }
                        }
                        if(seat != null) {
                            seats.add(seat);
                            seatRepository.save(seat);
                        }

                    }

                    // Set sector seats
                    s.setSeats(seats);

                    // Update seats of sector
                    sectorRepository.save(s);

                    // Make sure the sectors do not overlap
                    x = x + sectorSeatsPerRow + 1;
                    if(y % 2 == 0) {
                        y = 0;
                    } else {
                        y = y + sectorRows + 1;
                    }

                    sectors.add(s);
                }
                // Set sectors
                hall.setSectors(sectors);
                // ..and store
                hallRepository.save(hall);
            }
        }
    }
}
