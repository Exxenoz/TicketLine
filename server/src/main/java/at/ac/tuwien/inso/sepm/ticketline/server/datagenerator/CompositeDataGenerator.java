package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Profile("generateData")
@Component
public class CompositeDataGenerator implements DataGenerator {

    private final List<DataGenerator> generators;

    public CompositeDataGenerator(List<DataGenerator> generators) {
        this.generators = generators;
    }

    @PostConstruct
    @Override
    public void generate() {
        generators.forEach(DataGenerator::generate);
    }
}
