package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Sector;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.SectorCategory;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SectorCategoryRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SectorRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.List;

@Profile("generateData")
@Component
public class SectorDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_SECTORS_TO_GENERATE = 30;

    private final SectorRepository sectorRepository;
    private final SectorCategoryRepository sectorCategoryRepository;
    private final Faker faker;

    public SectorDataGenerator(SectorRepository sectorRepository, SectorCategoryRepository sectorCategoryRepository) {
        this.sectorRepository = sectorRepository;
        this.sectorCategoryRepository = sectorCategoryRepository;
        faker = new Faker();
    }

    @PostConstruct
    private void generateSectors() {
        if (sectorCategoryRepository.count() == 0) {
            LOGGER.info("Could not generate sectors, because there are no sector categories!");
        }
        else if (sectorRepository.count() > 0) {
            LOGGER.info("Sectors already generated");
        } else {
            LOGGER.info("Generating {} sector entries", NUMBER_OF_SECTORS_TO_GENERATE);
            List<SectorCategory> sectorCategories = sectorCategoryRepository.findAll();

            for (int i = 0; i < NUMBER_OF_SECTORS_TO_GENERATE; i++) {
                final var sector = new Sector();
                sector.setCategory(sectorCategories.get(faker.number().numberBetween(0, sectorCategories.size())));
                LOGGER.debug("Saving sector {}", sector);
                sectorRepository.save(sector);
            }
        }
    }
}
