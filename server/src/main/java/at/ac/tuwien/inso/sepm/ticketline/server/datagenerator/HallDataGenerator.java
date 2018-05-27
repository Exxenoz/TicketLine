package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.LocationAddress;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Sector;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.HallRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SectorRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Profile("generateData")
@Component
public class HallDataGenerator implements DataGenerator {

    private final static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_HALLS = 25;
    private final HallRepository hallRepository;
    private final SectorRepository sectorRepository;
    private final Faker faker;

    private final static boolean SINGLE_SECTOR_MODE = true;

    public HallDataGenerator(HallRepository hallRepository, SectorRepository sectorRepository) {
        this.hallRepository = hallRepository;
        this.sectorRepository = sectorRepository;
        faker = new Faker();
    }

    @Override
    public void generate() {
        if (hallRepository.count() > 0) {
            LOGGER.info("Halls already generated.");
        } else if (sectorRepository.findAll().size() == 0) {
            LOGGER.warn("Could not generate halls, since there are no sectors.");
        } else {
            LOGGER.info("Generating {} hall entries", NUMBER_OF_HALLS);
            for (int i = 0; i < NUMBER_OF_HALLS; i++) {
                final var hall = new Hall();
                //set random name
                hall.setName(String.format("The Hall of %s", faker.lordOfTheRings().character()));

                //set random sectors, pick a few..
                List<Sector> sectors = sectorRepository.findAll();
                List<Sector> currentSectors = new ArrayList<>();

                //.. in simple dev mode
                if (SINGLE_SECTOR_MODE) {
                    Sector mainSector = new Sector();

                    mainSector.setStartPositionX(0);
                    mainSector.setStartPositionY(0);

                    mainSector.setRows(6);
                    mainSector.setSeatsPerRow(6);

                    mainSector.setCategory(sectors.get(0).getCategory());
                    sectorRepository.save(mainSector);

                    currentSectors.add(mainSector);
                } else {
                    //.. or at random
                    Random r = new Random();
                    int sectorCount = r.nextInt(10);

                    for (int j = 0; j < sectorCount; i++) {
                        int si = r.nextInt(sectors.size());
                        currentSectors.add(sectors.get(si));
                    }
                }
                hall.setSectors(currentSectors);

                // and add address
                LocationAddress address = new LocationAddress();
                address.setLocationName(faker.lordOfTheRings().location());
                address.setCity(faker.address().city());
                address.setStreet(faker.address().streetName());
                address.setCountry(faker.address().country());
                address.setPostalCode(faker.address().zipCode());
                hall.setAddress(address);

                // and store
                hallRepository.save(hall);
            }
        }
    }
}
