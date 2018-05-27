package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.SectorCategory;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.SectorCategoryRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;

@Profile("generateData")
@Component
@Order(1)
public class SectorCategoryDataGenerator implements DataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_SECTOR_CATEGORIES_TO_GENERATE = 5;

    private final SectorCategoryRepository sectorCategoryRepository;
    private final Faker faker;

    public SectorCategoryDataGenerator(SectorCategoryRepository sectorCategoryRepository) {
        this.sectorCategoryRepository = sectorCategoryRepository;
        faker = new Faker();
    }

    @Override
    public void generate() {
        if (sectorCategoryRepository.count() > 0) {
            LOGGER.info("Sector categories already generated");
        } else {
            LOGGER.info("Generating {} sector category entries", NUMBER_OF_SECTOR_CATEGORIES_TO_GENERATE);
            char sectorChar = 'A';
            for (int i = 0; i < NUMBER_OF_SECTOR_CATEGORIES_TO_GENERATE && sectorChar <= 'Z'; i++, sectorChar++) {
                final var sectorCategory = new SectorCategory();
                sectorCategory.setName(String.valueOf(sectorChar));
                sectorCategory.setBasePriceMod(BigDecimal.valueOf(faker.number().randomDouble(4, 0, 2)));
                LOGGER.debug("Saving sector category {}", sectorCategory);
                sectorCategoryRepository.save(sectorCategory);
            }
        }
    }
}
